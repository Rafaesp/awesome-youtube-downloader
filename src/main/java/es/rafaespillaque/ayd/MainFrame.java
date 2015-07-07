package es.rafaespillaque.ayd;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import es.rafaespillaque.ayd.ITunesSearcher.ITunesSearchListener;
import es.rafaespillaque.ayd.model.Song;

public class MainFrame extends JFrame implements Observer{

	private static final long serialVersionUID = 6571927323567562621L;

	private static final int WIDTH = 960;
	private static final int HEIGHT = 760;
	private static final Song BACK_SONG;
	static{
		BACK_SONG = new Song();
		BACK_SONG.setTitle("Volver");
	}

	private JTextField txtSearch;
	private JLabel lblTitle;
	private JLabel lblAlbum;
	private JLabel lblArtist;
	private JLabel lblYtTitle;
	private JLabel lblYtDescription;
	private JButton btnSearch;
	private JRadioButton rdbtnAlbums;
	private JRadioButton rdbtnArtists;
	private JTable table;
	private ButtonGroup rdbtnSearchMode;
	private JLabel lblSelectedDownloadPath;
	private JLabel lblGoYoutube;
	private JLabel lblLog;
	private JButton btnDownload;
	
	private ITunesSearcher itSearcher;
	private YouTubeSearcher ytSearcher;
	private SongTableModel songTableModel;
	private static File downloadPath;
	private boolean goYoutubeEnabled = false;
	private List<Song> songs;
	private List<Song> lastSongs;

	public static void main(String[] args) {
		Level logLevel = Level.parse(Utils.getProp("logging.level", "ALL"));
		LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(logLevel);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(consoleHandler);
		if(Utils.getProp("logging.file.enabled", Boolean.TRUE)){
			try {
				FileHandler fileHandler = new FileHandler("log.txt");
				fileHandler.setFormatter(new SimpleFormatter());
				fileHandler.setLevel(logLevel);
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(fileHandler);
			} catch (IOException e) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
			}
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Start");
					MainFrame frame = new MainFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
				}
			}
		});
	}

	public MainFrame(){
		setResizable(false);
		itSearcher = new ITunesSearcher(createItunesSearchListener());
		ytSearcher = new YouTubeSearcher();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		}
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Rectangle screen = ge.getMaximumWindowBounds();

		setSize(WIDTH, HEIGHT);
		setLocation(screen.width / 2 - WIDTH / 2, screen.height/ 2 - HEIGHT/ 2);
		getContentPane().setLayout(null);

		buildLeftPanel();

		buildScrollPane();
		
		buildDetailsArea();

		buildMenuBar();
	}

	private void buildLeftPanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 445);
		getContentPane().add(panel);
		panel.setLayout(null);

		txtSearch = new JTextField();
		txtSearch.setBounds(12, 24, 277, 34);
		txtSearch.setColumns(10);
		createEnterKeyListener();
		panel.add(txtSearch);

		JLabel lblBuscar = new JLabel("Buscar");
		lblBuscar.setBounds(12, 0, 87, 24);
		panel.add(lblBuscar);

		JRadioButton rdbtnSongs = new JRadioButton("Canciones");
		rdbtnSongs.setBounds(8, 97, 149, 23);
		rdbtnSongs.setActionCommand(ITunesSearcher.MODE_SONG+"");
		rdbtnSongs.setSelected(true);
		panel.add(rdbtnSongs);

		rdbtnAlbums = new JRadioButton("Disco");
		rdbtnAlbums.setBounds(8, 124, 149, 23);
		rdbtnAlbums.setActionCommand(ITunesSearcher.MODE_ALBUM+"");
		panel.add(rdbtnAlbums);

		rdbtnArtists = new JRadioButton("Artista");
		rdbtnArtists.setBounds(8, 152, 149, 23);
		rdbtnArtists.setActionCommand(ITunesSearcher.MODE_ARTIST+"");
		panel.add(rdbtnArtists);

		rdbtnSearchMode = new ButtonGroup();
		rdbtnSearchMode.add(rdbtnSongs);
		rdbtnSearchMode.add(rdbtnAlbums);
		rdbtnSearchMode.add(rdbtnArtists);

		btnSearch = new JButton("Buscar");
		btnSearch.setBounds(93, 183, 117, 25);
		createSearchListener();
		panel.add(btnSearch);

		JSeparator separator = new JSeparator();
		separator.setBounds(8, 220, 289, 2);
		panel.add(separator);

		JLabel lblRutaDeDescarga = new JLabel("Ruta de descarga");
		lblRutaDeDescarga.setBounds(12, 234, 277, 24);
		panel.add(lblRutaDeDescarga);
		
		downloadPath = new File(Utils.getCurrentPath() + File.separator + "ayd");
		
		lblSelectedDownloadPath = new JLabel(downloadPath.getAbsolutePath());
		lblSelectedDownloadPath.setBounds(12, 262, 277, 25);
		panel.add(lblSelectedDownloadPath);

		JButton btnChangePath = new JButton("Cambiar ruta");
		btnChangePath.setBounds(79, 287, 138, 25);
		createChangePathListener(btnChangePath);
		panel.add(btnChangePath);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(8, 337, 289, 2);
		panel.add(separator_1);
	}
	
	private void buildScrollPane() {
		songTableModel = new SongTableModel(
				new String[]{ "Nombre", "Artista", "YouTube", "%"});
		
		table = new JTable(songTableModel);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		columnModel.getColumn(0).setPreferredWidth(267);
		columnModel.getColumn(1).setPreferredWidth(125);
		columnModel.getColumn(2).setPreferredWidth(228);
		columnModel.getColumn(3).setPreferredWidth(40);
		
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ListSelectionModel selectionModel = table.getSelectionModel();
		createTableSelectionListener(selectionModel);
		createTableClickListener(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(300, 0, 660, 450);
		table.setFillsViewportHeight(true);
		
		getContentPane().add(scrollPane);
	}

	private void buildDetailsArea() {
		JLabel lblDetalle1 = new JLabel("Detalle");
		lblDetalle1.setFont(new Font("Dialog", Font.BOLD, 18));
		lblDetalle1.setBounds(604, 462, 83, 25);
		getContentPane().add(lblDetalle1);
		
		JLabel lblDetalle2 = new JLabel("Título:");
		lblDetalle2.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle2.setBounds(20, 517, 70, 25);
		getContentPane().add(lblDetalle2);
		
		JLabel lblDetalle3 = new JLabel("Disco:");
		lblDetalle3.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle3.setBounds(20, 544, 70, 25);
		getContentPane().add(lblDetalle3);
		
		JLabel lblDetalle4 = new JLabel("Artista:");
		lblDetalle4.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle4.setBounds(20, 571, 70, 25);
		getContentPane().add(lblDetalle4);
		
		JLabel lblDetalle5 = new JLabel("Título:");
		lblDetalle5.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle5.setBounds(20, 626, 70, 25);
		getContentPane().add(lblDetalle5);
		
		JLabel lblDetalle6 = new JLabel("Descripción:");
		lblDetalle6.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle6.setBounds(20, 653, 103, 25);
		getContentPane().add(lblDetalle6);
		
		lblGoYoutube = new JLabel("Ir a Youtube");
		lblGoYoutube.setFont(new Font("Dialog", Font.BOLD, 12));
		lblGoYoutube.setBounds(594, 487, 93, 25);
		createGoYoutubeListener();
		getContentPane().add(lblGoYoutube);
		
		lblLog = new JLabel("Log");
		lblLog.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLog.setBounds(700, 487, 93, 25);
		createOpenLogListener();
		getContentPane().add(lblLog);
		
		lblTitle = new JLabel("");
		lblTitle.setBounds(136, 517, 870, 25);
		getContentPane().add(lblTitle);
		
		lblAlbum = new JLabel("");
		lblAlbum.setBounds(136, 544, 870, 25);
		getContentPane().add(lblAlbum);
		
		lblArtist = new JLabel("");
		lblArtist.setBounds(136, 571, 870, 25);
		getContentPane().add(lblArtist);
		
		lblYtTitle = new JLabel("");
		lblYtTitle.setBounds(136, 626, 870, 25);
		getContentPane().add(lblYtTitle);
		
		lblYtDescription = new JLabel("");
		lblYtDescription.setVerticalAlignment(SwingConstants.TOP);
		lblYtDescription.setVerticalTextPosition(SwingConstants.TOP);
		lblYtDescription.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblYtDescription.setBounds(136, 653, 820, 54);
		getContentPane().add(lblYtDescription);
		
		btnDownload = new JButton("Descargar");
		btnDownload.setBounds(348, 463, 117, 25);
		btnDownload.setEnabled(false);
		createDownloadListener();
		getContentPane().add(btnDownload);
	}


	private void createDownloadListener() {
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				int[] selectedRows = table.getSelectedRows();
				
				List<Song> selectedSongs = new ArrayList<Song>(selectedRows.length);

				for (int i = 0; i < selectedRows.length; i++) {
					selectedSongs.add(songs.get(selectedRows[i]));
				}
				
				for (Song song : selectedSongs) {
					new Thread(new YouTubeDLManager(song)).start();
				}
			}
		});
	}

	private void buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuArchivo = new JMenu("Opciones");
		menuBar.add(menuArchivo);

		JMenuItem menuItemUpdate = new JMenuItem("update youtube-dl");
		menuArchivo.add(menuItemUpdate);
		menuItemUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new YouTubeDLManager()).start();
			}
		});
	}
	
	private void createEnterKeyListener(){
		txtSearch.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent keyevent) {
			}
			
			public void keyReleased(KeyEvent keyevent) {
				if(keyevent.getKeyCode() == KeyEvent.VK_ENTER){
					if(keyevent.isControlDown()){
						rdbtnSearchMode.setSelected(rdbtnArtists.getModel(), true);
					}else if(keyevent.isShiftDown()){
						rdbtnSearchMode.setSelected(rdbtnAlbums.getModel(), true);
					}
					btnSearch.doClick();
				}
			}
			
			public void keyPressed(KeyEvent keyevent) {
			}
		});
	}
	
	private void createSearchListener() {
		btnSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MainFrame.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				itSearcher.setTerm(txtSearch.getText());
				
				Integer mode = new Integer (rdbtnSearchMode.getSelection().getActionCommand());
				itSearcher.setType(mode);
				itSearcher.search();
				
				MainFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
	
	private void createChangePathListener(JButton btnChangePath) {
		btnChangePath.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(downloadPath);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(MainFrame.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					downloadPath = fc.getSelectedFile();
					lblSelectedDownloadPath.setText(downloadPath.getAbsolutePath());
				}
			}
		});
	}

	private void createTableSelectionListener(ListSelectionModel selectionModel) {
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent event) {
				if(!event.getValueIsAdjusting()){
					int[] selected = table.getSelectedRows();
					if(selected.length == 1){
						Song song = songTableModel.getSongs().get(selected[0]);
						lblTitle.setText(song.getTitle());
						lblArtist.setText(song.getArtist());
						lblAlbum.setText(song.getAlbum());
						lblYtTitle.setText(song.getYtTitle());
						lblYtDescription.setText(song.getYtDescription());
						
						lblGoYoutube.setForeground(Color.BLUE);
						lblLog.setForeground(Color.BLUE);
						goYoutubeEnabled = true;
						
						btnDownload.setEnabled(true);
					}else if(selected.length > 1){
						lblTitle.setText("");
						lblArtist.setText("");
						lblAlbum.setText("");
						lblYtTitle.setText("");
						lblYtDescription.setText("");
						
						lblGoYoutube.setForeground(Color.BLACK);
						lblLog.setForeground(Color.BLACK);
						goYoutubeEnabled = false;
						
						btnDownload.setEnabled(true);
					}else if(selected.length <= 0){
						lblTitle.setText("");
						lblArtist.setText("");
						lblAlbum.setText("");
						lblYtTitle.setText("");
						lblYtDescription.setText("");
						
						lblGoYoutube.setForeground(Color.BLACK);
						lblLog.setForeground(Color.BLACK);
						goYoutubeEnabled = false;
						
						btnDownload.setEnabled(false);
					}
				}
			}
		});
	}
	
	private void createTableClickListener(JTable table){
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	if(row != -1){
		        		if(lastSongs != null && row == 0){
		        			//Volviendo desde las alternativas
		        			songTableModel.setSongs(lastSongs);
		        			songs = lastSongs;
		        			lastSongs = null;
		        		}else{
		        			//Primera vez, Guardamos las actuales y mostramos las alternativas 
		        			Song song = songs.get(row);
			            	songTableModel.setSongs(song.getAlternativeSongs());
			            	if(!songTableModel.getSongs().isEmpty() && songTableModel.getSongs().get(0) != BACK_SONG){
			            		songTableModel.addSongAsFirst(BACK_SONG);
			            	}
			            	lastSongs = songs;
			            	songs = song.getAlternativeSongs();
		        		}
		            }
		        }
		    }
		});
	}
	
	private void createGoYoutubeListener() {
		lblGoYoutube.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
				lblGoYoutube.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			public void mouseEntered(MouseEvent e) {
				if(goYoutubeEnabled){
					lblGoYoutube.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				if(goYoutubeEnabled){
					int selected = table.getSelectedRow();
					try {
						Desktop.getDesktop().browse(new URI(songTableModel.getSongs().get(selected).getUrl()));
					} catch (IOException e) {
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
					} catch (URISyntaxException e) {
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
					}
				}
			}
		});
	}
	
	private void createOpenLogListener(){
		lblLog.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
				lblLog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			public void mouseEntered(MouseEvent e) {
				if(goYoutubeEnabled){
					lblLog.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				if(goYoutubeEnabled){ //misma lógica
					int selected = table.getSelectedRow();
					String downloadLog = songs.get(selected).getDownloadLog();
		        	new LogDialog(MainFrame.this, "<html>" + downloadLog + "</html>");
				}
			}
		});
	}

	public ITunesSearchListener createItunesSearchListener(){
		return new ITunesSearchListener(){
			public void onSearchFinished(List<Song> songs) {
				MainFrame.this.songs = songs;
				for (Song song : songs) {
					song.addObserver(MainFrame.this);
				}
				songTableModel.setSongs(songs);
				ytSearcher.search(songs);
			}
		};
	}
	
	public void update(Observable observable, Object obj) {
		if(lastSongs == null){//Sólo si la tabla tiene la lista original actualmente
			Song song = (Song)observable;
			if(obj.equals("youtube") || obj.equals("status")){
				int index = songTableModel.getSongs().indexOf(song);
				songTableModel.fireTableRowsUpdated(index, index);
			}
		}
	}

	public static File getDownloadPath() {
		return downloadPath;
	}

	public class LogDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		public LogDialog(JFrame parent, String message) {
			super(parent, "Log", true);
			if (parent != null) {
				Dimension parentSize = parent.getSize();
				Point p = parent.getLocation();
				setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
			}
			JPanel messagePane = new JPanel();
			messagePane.add(new JLabel(message));
			getContentPane().add(messagePane);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					} else {
						super.keyReleased(e);
					}
				}
			});
			pack();
			setVisible(true);
		}
	}
}
