package es.rafaespillaque.ayd;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import es.rafaespillaque.ayd.model.Song;
import javax.swing.SwingConstants;
import java.awt.Color;

public class MainFrame extends JFrame {

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



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		setResizable(false);
		itSearcher = new ITunesSearcher();
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
		panel.add(txtSearch);
		txtSearch.setColumns(10);

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

		btnSearch = new JButton("Buscar!");
		btnSearch.setBounds(93, 183, 117, 25);
		createSearchListener();
		panel.add(btnSearch);

		JSeparator separator = new JSeparator();
		separator.setBounds(8, 220, 289, 2);
		panel.add(separator);

		JLabel lblRutaDeDescarga = new JLabel("Ruta de descarga");
		lblRutaDeDescarga.setBounds(12, 234, 277, 24);
		panel.add(lblRutaDeDescarga);
		
		downloadPath = new File("user.home");
		
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

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(12, 392, 277, 24);
		panel.add(progressBar);

		JList list = new JList();
		list.setBounds(12, 471, 277, 234);
		panel.add(list);

		JLabel lblColaDeDescargas = new JLabel("Cola de descargas");
		lblColaDeDescargas.setBounds(12, 432, 277, 24);
		panel.add(lblColaDeDescargas);

		JLabel lblCancionActual = new JLabel("Canción actual");
		lblCancionActual.setBounds(12, 351, 277, 23);
		panel.add(lblCancionActual);
	}
	
	private void buildScrollPane() {
		songTableModel = new SongTableModel(
				new String[]{ "Nombre", "Artista", "YouTube"});
		
		table = new JTable(songTableModel);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		columnModel.getColumn(0).setPreferredWidth(267);
		columnModel.getColumn(1).setPreferredWidth(125);
		columnModel.getColumn(2).setPreferredWidth(267);
		
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
		getContentPane().add(btnDownload);
	}


	private void buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmAsd = new JMenuItem("Asd");
		mnArchivo.add(mntmAsd);
	}
	
	private void createSearchListener() {
		btnSearch.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				itSearcher.setTerm(txtSearch.getText());
				
				Integer mode = new Integer (rdbtnSearchMode.getSelection().getActionCommand());
				itSearcher.setType(mode);
				
				List<Song> songs = itSearcher.search();
				ytSearcher.search(songs.subList(0, 5));
				
				songTableModel.setSongs(songs);
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

	
}
