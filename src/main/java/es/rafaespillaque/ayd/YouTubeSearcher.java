package es.rafaespillaque.ayd;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.rafaespillaque.ayd.model.Song;

public class YouTubeSearcher {

	private static final String BASE_URL = "http://gdata.youtube.com/feeds/api/videos?alt=json&q=";

	
	public void search(List<Song> songs){
		for (Song song : songs) {
			search(song);
		}
	}
	
	public void search(final Song song){
		new Thread(new Runnable() {
			
			public void run() {
				URL url;
				
				String q = song.getArtist()+" "+song.getTitle();
				
				try {
					url = new URL(BASE_URL + URLEncoder.encode(q, "UTF-8"));
					String json = Utils.read(url.openConnection().getInputStream());
					parseJSON(json, song);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void parseJSON(String json, Song song){
		try {
			JSONObject root = new JSONObject(json);
			JSONObject feed = root.getJSONObject("feed");
			JSONArray entry = feed.getJSONArray("entry");
			
			//TODO Recorrer el array para buscar que el autor sea discográfica
			JSONObject video = entry.getJSONObject(0);
			
			JSONObject prop = video.getJSONObject("title");
			song.setYtTitle(prop.getString("$t"));
			
			prop = video.getJSONObject("content");
			song.setYtDescription(prop.getString("$t"));
			
			JSONArray propLink = video.getJSONArray("link");
			
			song.setUrl(propLink.getJSONObject(0).getString("href"));
			
			song.notifyObservers("youtube");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
