package es.rafaespillaque.ayd;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
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

	private JTextField txtSearch;
	private JLabel lblTitle;
	private JLabel lblAlbum;
	private JLabel lblArtist;
	private JLabel lblYtTitle;
	private JLabel lblYtDescription;
	private JButton btnSearch;
	private JTable table;
	private ButtonGroup rdbtnSearchMode;
	private JLabel lblSelectedDownloadPath;
	private JLabel lblGoYoutube;
	private JButton btnDownload;
	
	private ITunesSearcher itSearcher;
	private YouTubeSearcher ytSearcher;
	private SongTableModel songTableModel;
	private File downloadPath;
	private boolean goYoutubeEnabled = false;
	private List<Song> songs;

	public static void main(String[] args) {
		Level logLevel = Level.parse(Utils.getProp("logging.level", "ALL"));
		LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(logLevel);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		Logger.getGlobal().addHandler(consoleHandler);
		if(Utils.getProp("logging.file.enabled", Boolean.FALSE)){
			try {
				FileHandler fileHandler = new FileHandler("log.txt");
				fileHandler.setFormatter(new SimpleFormatter());
				fileHandler.setLevel(logLevel);
				Logger.getGlobal().addHandler(fileHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Logger.getGlobal().fine("Start");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
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
			e.printStackTrace();
		}
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Rectangle screen = ge.getMaximumWindowBounds();

		setSize(960, screen.height);
		setLocation(screen.width / 2 - WIDTH / 2, 0);
		getContentPane().setLayout(null);

		buildLeftPanel();

		buildScrollPane();
		
		buildDetailsArea();

		buildMenuBar();
	}

	private void buildLeftPanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 745);
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

		JRadioButton rdbtnAlbums = new JRadioButton("Disco");
		rdbtnAlbums.setBounds(8, 124, 149, 23);
		rdbtnAlbums.setActionCommand(ITunesSearcher.MODE_ALBUM+"");
		panel.add(rdbtnAlbums);

		JRadioButton rdbtnArtists = new JRadioButton("Artista");
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
		
		downloadPath = new File("user.home"+File.separator+"ayd");
		
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
		lblDetalle2.setBounds(387, 517, 70, 25);
		getContentPane().add(lblDetalle2);
		
		JLabel lblDetalle3 = new JLabel("Disco:");
		lblDetalle3.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle3.setBounds(387, 544, 70, 25);
		getContentPane().add(lblDetalle3);
		
		JLabel lblDetalle4 = new JLabel("Artista:");
		lblDetalle4.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle4.setBounds(387, 571, 70, 25);
		getContentPane().add(lblDetalle4);
		
		JLabel lblDetalle5 = new JLabel("Título:");
		lblDetalle5.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle5.setBounds(387, 626, 70, 25);
		getContentPane().add(lblDetalle5);
		
		JLabel lblDetalle6 = new JLabel("Descripción:");
		lblDetalle6.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle6.setBounds(387, 653, 103, 25);
		getContentPane().add(lblDetalle6);
		
		lblGoYoutube = new JLabel("Ir a Youtube");
		lblGoYoutube.setFont(new Font("Dialog", Font.BOLD, 12));
		lblGoYoutube.setBounds(594, 487, 93, 25);
		createGoYoutubeListener();
		getContentPane().add(lblGoYoutube);
		
		lblTitle = new JLabel("");
		lblTitle.setBounds(503, 517, 431, 25);
		getContentPane().add(lblTitle);
		
		lblAlbum = new JLabel("");
		lblAlbum.setBounds(503, 544, 431, 25);
		getContentPane().add(lblAlbum);
		
		lblArtist = new JLabel("");
		lblArtist.setBounds(503, 571, 431, 25);
		getContentPane().add(lblArtist);
		
		lblYtTitle = new JLabel("");
		lblYtTitle.setBounds(503, 626, 431, 25);
		getContentPane().add(lblYtTitle);
		
		lblYtDescription = new JLabel("");
		lblYtDescription.setVerticalAlignment(SwingConstants.TOP);
		lblYtDescription.setVerticalTextPosition(SwingConstants.TOP);
		lblYtDescription.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblYtDescription.setBounds(502, 653, 432, 54);
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

		JMenuItem menuItemConfig = new JMenuItem("Config");
		menuArchivo.add(menuItemConfig);
		menuItemConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
						goYoutubeEnabled = true;
						
						btnDownload.setEnabled(true);
					}else if(selected.length > 1){
						lblTitle.setText("");
						lblArtist.setText("");
						lblAlbum.setText("");
						lblYtTitle.setText("");
						lblYtDescription.setText("");
						
						lblGoYoutube.setForeground(Color.BLACK);
						goYoutubeEnabled = false;
						
						btnDownload.setEnabled(true);
					}else if(selected.length <= 0){
						lblTitle.setText("");
						lblArtist.setText("");
						lblAlbum.setText("");
						lblYtTitle.setText("");
						lblYtDescription.setText("");
						
						lblGoYoutube.setForeground(Color.BLACK);
						goYoutubeEnabled = false;
						
						btnDownload.setEnabled(false);
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
			
			public void mouseClicked(MouseEvent e) {
				if(goYoutubeEnabled){
					int selected = table.getSelectedRow();
					try {
						Desktop.getDesktop().browse(new URI(songTableModel.getSongs().get(selected).getUrl()));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
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
		Song song = (Song)observable;
		if(obj.equals("youtube") || obj.equals("status")){
			int index = songTableModel.getSongs().indexOf(song);
			songTableModel.fireTableRowsUpdated(index, index);
		}
	}

	
}
