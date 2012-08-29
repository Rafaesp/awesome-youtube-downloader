package es.rafaespillaque.ayd;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ResultRow extends Panel{
	public ResultRow() {
		setPreferredSize(new Dimension(640, 40));
		setLayout(null);
		
		JLabel lblNombre = new JLabel("<html><p>Antes de Que Cuente Diez - Antes de Que Cuente Diez - Fito y Fitipaldis</p></html>");
		lblNombre.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblNombre.setBounds(0, 0, 330, 40);
		add(lblNombre);
		
		JLabel lblNombreEnYoutube = new JLabel("Nombre en Youtube");
		lblNombreEnYoutube.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblNombreEnYoutube.setBounds(330, 0, 230, 40);
		add(lblNombreEnYoutube);
		
		JButton btnDescargar = new JButton("Descargar");
		btnDescargar.setFont(new Font("Dialog", Font.PLAIN, 10));
		btnDescargar.setBounds(560, 10, 100, 20);
		add(btnDescargar);
		
	}
}
