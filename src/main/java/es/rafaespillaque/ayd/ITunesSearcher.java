package es.rafaespillaque.ayd;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.rafaespillaque.ayd.model.Song;

public class ITunesSearcher {
	
	public static final String BASE_URL = "http://itunes.apple.com/search?";
	public static final int MODE_SONG = 1;
	public static final int MODE_ALBUM = 2;
	public static final int MODE_ARTIST = 3;
	
	private Map<String, String> params;
	
	public ITunesSearcher(){
		params = new HashMap<String, String>();
	}
	
	public List<Song> search(){
		try {
			
		StringBuilder builder = new StringBuilder();
		builder.append("country=ES");
		builder.append("&lang=es_ES");
		builder.append("&media=music");
		builder.append("&entity=song");
		params.put("entity", "song");
		for(String key : params.keySet()){
			builder.append("&");
			builder.append(key);
			builder.append("=");
			builder.append(params.get(key));
		}
		
		URL url = new URL(BASE_URL + builder.toString());
		return parseJSON(read(url.openConnection().getInputStream()));
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new LinkedList<Song>();
	}
	
	public void setTerm(String term){
		try {
			params.put("term", URLEncoder.encode(term, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void setType(int type){
		switch (type) {
		case MODE_SONG:
			params.put("attribute", "songTerm");
			break;
		case MODE_ALBUM:
			params.put("attribute", "albumTerm");
			break;
		case MODE_ARTIST:
			params.put("attribute", "artistTerm");
			break;
		default:
			break;
		}
	}
	
	public void reset(){
		params.clear();
	}
	
	private String read(InputStream is){
		try {
	        return new Scanner(is).useDelimiter("\\A").next();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
	
	private List<Song> parseJSON(String json){
		List<Song> results = new LinkedList<Song>();
		System.out.println(json);
		try {
			JSONObject root = new JSONObject(json);
			JSONArray array = root.getJSONArray("results");
			
			for(int i = 0; i<array.length(); ++i){
				JSONObject result = array.getJSONObject(i);
				Song song = new Song();
//				if(currentType == TYPE_SONG){
					song.setTitle(result.getString("trackName"));
					song.setAlbum(result.getString("collectionName"));
//				}
				
				song.setArtist(result.getString("artistName"));
				
				results.add(song);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}

}
