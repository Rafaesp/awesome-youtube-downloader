package es.rafaespillaque.ayd;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import es.rafaespillaque.ayd.model.Song;

public class SongTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -4867683494989037354L;
	
	private List<Song> songs;
	private String[] columnNames;
	
	public SongTableModel(String[] columns){
		columnNames = columns;
		songs = new LinkedList<Song>();
		fireTableDataChanged();
	}
	
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return songs.size();
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
			case 0:
				return songs.get(row).getTitle();
			case 1:
				return songs.get(row).getArtist();
			case 2:
				return songs.get(row).getYtTitle();
			default:
				return null;
		}
	}
	
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
}
