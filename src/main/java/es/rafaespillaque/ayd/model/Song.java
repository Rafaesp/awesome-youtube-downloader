package es.rafaespillaque.ayd.model;

import java.util.Observable;


public class Song extends Observable{
	private String title = "";
	private String album = "";
	private String artist = "";
	private String ytTitle = "";
	private String ytDescription = "";
	private String url = "";
	private String downloadStatus = "";
	private StringBuilder downloadLog = new StringBuilder();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title != null? title : "";
		setChanged();
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album != null? album : "";
		setChanged();
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist != null? artist : "";
		setChanged();
	}
	public String getYtTitle() {
		return ytTitle;
	}
	public void setYtTitle(String ytTitle) {
		this.ytTitle = ytTitle != null? ytTitle : "";
		setChanged();
	}
	public String getYtDescription() {
		return ytDescription;
	}
	public void setYtDescription(String ytDescription) {
		this.ytDescription = ytDescription != null? "<html><p>"+ytDescription+"</p></html>" : "";
		setChanged();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url!=null? url : "";
		setChanged();
	}
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadLog.append(downloadStatus);
		this.downloadLog.append("\n");
		int indexOf = downloadStatus.indexOf("%");
		if(indexOf != -1){
			this.downloadStatus = downloadStatus.substring(indexOf-4, indexOf+1).replaceAll(" ", "");
			System.out.println(this.downloadStatus);
		}else{
			if(downloadStatus.contains("ffmpeg")){
				this.downloadStatus = "OK";
			}else{
				this.downloadStatus = "0%";
			}
		}
				
		setChanged();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
