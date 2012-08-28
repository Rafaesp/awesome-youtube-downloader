package es.rafaespillaque.ayd;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

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
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.GridLayout;

public class MainFrame extends JFrame {

	private static final int WIDTH = 960;

	private JPanel content;
	private JTextField textField;

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
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle screen = ge.getMaximumWindowBounds();

		System.out.print(screen.height+"");
		setSize(960, screen.height);
		setLocation(screen.width/2 - WIDTH/2, 0);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 301, 745);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 24, 277, 34);
		panel.add(textField);
		textField.setColumns(10);
		
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
		
		JButton btnBuscar = new JButton("Buscar!");
		btnBuscar.setBounds(93, 183, 117, 25);
		panel.add(btnBuscar);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 220, 289, 2);
		panel.add(separator);
		
		JLabel lblRutaDeDescarga = new JLabel("Ruta de descarga");
		lblRutaDeDescarga.setBounds(12, 234, 277, 24);
		panel.add(lblRutaDeDescarga);
		
		JButton btnCambiarRuta = new JButton("Cambiar ruta");
		btnCambiarRuta.setBounds(82, 285, 138, 25);
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
		
		JLabel lblCancinActual = new JLabel("Canción actual");
		lblCancinActual.setBounds(12, 351, 277, 23);
		panel.add(lblCancinActual);
		
		JPanel resultsPanel = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane(resultsPanel);
		resultsPanel.setLayout(new GridLayout(30, 1, 0, 0));
		
		scrollPane.setBounds(301, 0, 657, 719);
		getContentPane().add(scrollPane);
		
		for(int i = 0; i<30;++i){
			JLabel lbl = new JLabel(i+"");
			lbl.setSize(100, 277);
			resultsPanel.add(lbl);
		}
		
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmAsd = new JMenuItem("Asd");
		mnArchivo.add(mntmAsd);
		
	}
}
