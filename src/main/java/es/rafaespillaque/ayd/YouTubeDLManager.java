package es.rafaespillaque.ayd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import es.rafaespillaque.ayd.model.Song;

public class YouTubeDLManager implements Runnable{

	private String options;
	private Song song;
	private boolean update = false;
	
	public YouTubeDLManager() {
		this.update = true;
	}
	
	public YouTubeDLManager(Song song) {
		this("--extract-audio --audio-format mp3", 
				song);
	}
	
	public YouTubeDLManager(String options, Song song) {
		super();
		this.options = options;
		this.song = song;
	}


	public void run() {
		if(update){
			update();
			return;
		}
		if(song.getUrl() != null){
			download();
		}else{
			//Reintentamos durante 10 segundos
			int retries = 10;
			while(retries > 0){
				try {
					Thread.sleep(1000);
					if(song.getUrl() != null){
						download();
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private void download() {
		Runtime rt = Runtime.getRuntime();
		try {
			String command = Utils.getCurrentPath() + "\\youtube-dl.exe " + 
								options + " -o \"" + song.getArtist() + " - " + song.getTitle() + ".%(ext)s\" " + song.getUrl();
			String path = "PATH=%PATH%;" + Utils.getCurrentPath() + "\\ffmpeg";
			Logger.getGlobal().fine(command);
			final Process pr = rt.exec(command, new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                Logger.getGlobal().fine(line);
			                song.setDownloadStatus(line);
			                song.notifyObservers("status");
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader inputErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			        String line = null; 

			        try {
			            while ((line = inputErr.readLine()) != null) {
			            	Logger.getGlobal().warning(line);
			                song.setDownloadStatus(line);
			                song.notifyObservers("status");
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();

			pr.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		Runtime rt = Runtime.getRuntime();
		try {
			String command = Utils.getCurrentPath() + "\\youtube-dl.exe -U";
			String path = "PATH=%PATH%;" + Utils.getCurrentPath() + "\\ffmpeg";
			Logger.getGlobal().fine(command);
			final Process pr = rt.exec(command, new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                Logger.getGlobal().fine(line);
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader inputErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			        String line = null; 

			        try {
			            while ((line = inputErr.readLine()) != null) {
			            	Logger.getGlobal().warning(line);
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();

			pr.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
