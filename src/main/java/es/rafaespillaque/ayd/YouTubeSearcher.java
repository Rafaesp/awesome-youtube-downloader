package es.rafaespillaque.ayd;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.rafaespillaque.ayd.model.Song;

public class YouTubeSearcher {

	private static final String BASE_URL = "http://gdata.youtube.com/feeds/api/videos?alt=json&q=";

	
	public void search(final List<Song> songs){
		new Thread(new Runnable() {
			public void run() {
				for (Song song : songs) {
					search(song);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
					}
				}
			}
		}).start();
	}
	
	public void search(final Song song){
		new Thread(new Runnable() {
			
			public void run() {
				URL url;
				
				String q = song.getArtist()+" "+song.getTitle();
				
				try {
					url = new URL(BASE_URL + URLEncoder.encode(q, "UTF-8"));
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINE, "Petición a YouTube en la URL: " + url.toString());
					String json = Utils.read(url.openConnection(Utils.getProxy()).getInputStream());
					parseJSON(json, song);
				} catch (MalformedURLException e) {
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
				} catch (IOException e) {
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
				}
			}
		}).start();
	}
	
	private void parseJSON(String json, Song song){
		try {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINE, "YouTube devuelve: \n" + json);
			JSONObject root = new JSONObject(json);
			JSONObject feed = root.getJSONObject("feed");
			JSONArray entry = feed.getJSONArray("entry");
			
			for(int i = 0; i< entry.length(); i++){
				Song currentSong;
				if(i == 0){
					currentSong = song;
				}else{
					currentSong = new Song();
				}
				
				JSONObject video = entry.getJSONObject(i);
				
				JSONObject prop = video.getJSONObject("title");
				currentSong.setYtTitle(prop.getString("$t"));
				
				prop = video.getJSONObject("content");
				currentSong.setYtDescription(prop.getString("$t"));
				
				JSONArray propLink = video.getJSONArray("link");
				
				currentSong.setUrl(propLink.getJSONObject(0).getString("href"));
				
				if(i != 0){
					currentSong.setArtist(song.getArtist());
					currentSong.setTitle(song.getTitle());
					song.addAlternativeSong(currentSong);
				}
			}
			
			
			song.notifyObservers("youtube");
			
		} catch (JSONException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "JSONException con json: " + json, e);
		}
	}
	
}
