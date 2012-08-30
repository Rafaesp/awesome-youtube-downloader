package es.rafaespillaque.ayd.model;

import java.net.URL;

public class Song {
	private String title;
	private String album;
	private String artist;
	private String ytTitle;
	private String ytDescription;
	private URL url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getYtTitle() {
		return ytTitle;
	}
	public void setYtTitle(String ytTitle) {
		this.ytTitle = ytTitle;
	}
	public String getYtDescription() {
		return ytDescription;
	}
	public void setYtDescription(String ytDescription) {
		this.ytDescription = ytDescription;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	

}
