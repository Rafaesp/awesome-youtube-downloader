package es.rafaespillaque.ayd.model;


public class Song {
	private String title = "";
	private String album = "";
	private String artist = "";
	private String ytTitle = "";
	private String ytDescription = "";
	private String url = "";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title != null? title : "";
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album != null? album : "";
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist != null? artist : "";
	}
	public String getYtTitle() {
		return ytTitle;
	}
	public void setYtTitle(String ytTitle) {
		this.ytTitle = ytTitle != null? ytTitle : "";
	}
	public String getYtDescription() {
		return ytDescription;
	}
	public void setYtDescription(String ytDescription) {
		this.ytDescription = ytDescription != null? "<html><p>"+ytDescription+"</p></html>" : "";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url!=null? url : "";
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
