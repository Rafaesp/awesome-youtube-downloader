package es.rafaespillaque.ayd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import es.rafaespillaque.ayd.model.Song;

public class DownloaderConverter implements Runnable{

	private String command;
	private String path;
	private Song song;
	
	public DownloaderConverter(Song song) {
		this(Utils.getCurrentPath() + "\\youtube-dl.exe --extract-audio --audio-format mp3" 
					+ " -o \"" + song.getArtist() + " - " + song.getTitle() + ".%(ext)s\" ", 
				"PATH=%PATH%;" + Utils.getCurrentPath() + "\\ffmpeg",
				song);
	}
	
	public DownloaderConverter(String command, String path, Song song) {
		super();
		this.command = command;
		this.path = path;
		this.song = song;
	}


	public void run() {
		download();
	}


	private void download() {
		Runtime rt = Runtime.getRuntime();
		try {
			final Process pr = rt.exec(command + song.getUrl(), new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                System.out.println(line);
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
			            	System.out.println(line);
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
	
}
