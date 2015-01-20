package es.rafaespillaque.ayd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
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
		this(Utils.getProp("youtube-dl.options", "--extract-audio --audio-format mp3"), song);
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
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
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
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine(command);
			final Process pr = rt.exec(command, new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine(line);
			                song.setDownloadStatus(line);
			                song.notifyObservers("status");
			            }
			        } catch (IOException e) {
			            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
			        }
			    }
			}).start();
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader inputErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			        String line = null; 

			        try {
			            while ((line = inputErr.readLine()) != null) {
			            	Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(line);
			                song.setDownloadStatus(line);
			                song.notifyObservers("status");
			            }
			        } catch (IOException e) {
			            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
			        }
			    }
			}).start();

			pr.waitFor();
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		} catch (InterruptedException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		}
	}
	
	private void update(){
		Runtime rt = Runtime.getRuntime();
		try {
			String command = Utils.getCurrentPath() + "\\youtube-dl.exe -U";
			String path = "PATH=%PATH%;" + Utils.getCurrentPath() + "\\ffmpeg";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine(command);
			final Process pr = rt.exec(command, new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine(line);
			            }
			        } catch (IOException e) {
			            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
			        }
			    }
			}).start();
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader inputErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			        String line = null; 

			        try {
			            while ((line = inputErr.readLine()) != null) {
			            	Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(line);
			            }
			        } catch (IOException e) {
			            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
			        }
			    }
			}).start();

			pr.waitFor();
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		} catch (InterruptedException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		}
	}
	
}
