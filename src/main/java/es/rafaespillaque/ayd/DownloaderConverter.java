package es.rafaespillaque.ayd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class DownloaderConverter implements Runnable{

	String command;
	String url;
	String path;
	
	
	public DownloaderConverter(String url) {
		super();
		this.command = "C:\\Users\\Rafa\\git\\awesome-youtube-downloader\\youtube-dl.exe --extract-audio --audio-format mp3 ";
		this.url = url;
		this.path = "PATH=%PATH%;C:\\Users\\Rafa\\git\\awesome-youtube-downloader\\ffmpeg";
	}
	
	public DownloaderConverter(String command, String url, String path) {
		super();
		this.command = command;
		this.url = url;
		this.path = path;
	}


	public void run() {
		download();
	}


	private void download() {
		Runtime rt = Runtime.getRuntime();
		try {
			final Process pr = rt.exec(command + url, new String[]{path});
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                System.out.println(line);
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			        String line = null; 

			        try {
			            while ((line = input.readLine()) != null) {
			                System.out.println(line);
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
