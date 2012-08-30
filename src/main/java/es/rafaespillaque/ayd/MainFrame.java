package es.rafaespillaque.ayd;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 6571927323567562621L;

	private static final int WIDTH = 960;

	private JTextField txtSearch;
	private JLabel lblTitle;
	private JLabel lblAlbum;
	private JLabel lblArtist;
	private JLabel lblYtTitle;
	private JLabel lblYtDescription;
	private JButton btnBuscar;
	
	private ITunesSearcher searcher;

	private SongTableModel songTableModel;

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
		searcher = new ITunesSearcher();
		
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

		JRadioButton rdbtnNewRadioButton = new JRadioButton("Canciones");
		rdbtnNewRadioButton.setBounds(8, 97, 149, 23);
		panel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Discos");
		rdbtnNewRadioButton_1.setBounds(8, 124, 149, 23);
		panel.add(rdbtnNewRadioButton_1);

		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Artistas");
		rdbtnNewRadioButton_2.setBounds(8, 152, 149, 23);
		panel.add(rdbtnNewRadioButton_2);

		JRadioButton rdbtnTodo = new JRadioButton("Todo");
		rdbtnTodo.setBounds(8, 70, 149, 23);
		panel.add(rdbtnTodo);

		btnBuscar = new JButton("Buscar!");
		btnBuscar.setBounds(93, 183, 117, 25);
		btnBuscar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				searcher.setTerm(txtSearch.getText());
				songTableModel.setSongs(searcher.search());
			}
		});
		panel.add(btnBuscar);

		JSeparator separator = new JSeparator();
		separator.setBounds(8, 220, 289, 2);
		panel.add(separator);

		JLabel lblRutaDeDescarga = new JLabel("Ruta de descarga");
		lblRutaDeDescarga.setBounds(12, 234, 277, 24);
		panel.add(lblRutaDeDescarga);

		JButton btnCambiarRuta = new JButton("Cambiar ruta");
		btnCambiarRuta.setBounds(82, 285, 138, 25);
		// TODO: Asi ase abren links
		// btnCambiarRuta.addActionListener(new ActionListener() {
		//
		// public void actionPerformed(ActionEvent arg0) {
		// try {
		// Desktop.getDesktop().browse(new URI("http://www.google.es"));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
		panel.add(btnCambiarRuta);

		JLabel lblCasdasdasdasdasd = new JLabel("c:/asdasd/asdasd/asd");
		lblCasdasdasdasdasd.setBounds(12, 262, 277, 15);
		panel.add(lblCasdasdasdasdasd);

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

		// JPanel resultsPanel = new JPanel();
		//
		// JPanel outer = new JPanel();
		// outer.add( resultsPanel );
		//
		// JScrollPane scrollPane = new JScrollPane(outer);
		// scrollPane.getVerticalScrollBar().setUnitIncrement(40);
		// resultsPanel.setLayout(new GridLayout(50, 1, 0, 0));
		//
		// scrollPane.setBounds(300, 0, 660, 745);
		// getContentPane().add(scrollPane);
		//
		// for(int i = 0; i<50;++i){
		// ResultRow row = new ResultRow();
		// resultsPanel.add(row);
		// }

		songTableModel = new SongTableModel(
				new String[]{ "Nombre", "Artista", "YouTube", "Descargar" });
		
		JTable table = new JTable(songTableModel);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(300, 0, 660, 450);
		table.setFillsViewportHeight(true);
		
		getContentPane().add(scrollPane);
		
		JLabel lblDetalle1 = new JLabel("Detalle");
		lblDetalle1.setFont(new Font("Dialog", Font.BOLD, 18));
		lblDetalle1.setBounds(604, 462, 83, 25);
		getContentPane().add(lblDetalle1);
		
		JLabel lblDetalle2 = new JLabel("Título:");
		lblDetalle2.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle2.setBounds(387, 517, 70, 15);
		getContentPane().add(lblDetalle2);
		
		JLabel lblDetalle3 = new JLabel("Disco:");
		lblDetalle3.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle3.setBounds(387, 544, 70, 15);
		getContentPane().add(lblDetalle3);
		
		JLabel lblDetalle4 = new JLabel("Artista:");
		lblDetalle4.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle4.setBounds(387, 571, 70, 15);
		getContentPane().add(lblDetalle4);
		
		JLabel lblDetalle5 = new JLabel("Título:");
		lblDetalle5.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle5.setBounds(387, 626, 70, 15);
		getContentPane().add(lblDetalle5);
		
		JLabel lblDetalle6 = new JLabel("Descripción:");
		lblDetalle6.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle6.setBounds(387, 653, 103, 15);
		getContentPane().add(lblDetalle6);
		
		JLabel lblDetalle7 = new JLabel("Ir a Youtube");
		lblDetalle7.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDetalle7.setBounds(594, 487, 93, 15);
		getContentPane().add(lblDetalle7);
		
		lblTitle = new JLabel("");
		lblTitle.setBounds(503, 517, 431, 15);
		getContentPane().add(lblTitle);
		
		lblAlbum = new JLabel("");
		lblAlbum.setBounds(503, 544, 431, 15);
		getContentPane().add(lblAlbum);
		
		lblArtist = new JLabel("");
		lblArtist.setBounds(503, 571, 431, 15);
		getContentPane().add(lblArtist);
		
		lblYtTitle = new JLabel("");
		lblYtTitle.setBounds(503, 626, 431, 15);
		getContentPane().add(lblYtTitle);
		
		lblYtDescription = new JLabel("");
		lblYtDescription.setBounds(502, 653, 432, 54);
		getContentPane().add(lblYtDescription);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmAsd = new JMenuItem("Asd");
		mnArchivo.add(mntmAsd);

	}
}
