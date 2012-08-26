package es.rafaespillaque.ayd;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class MainFrame extends JFrame {

	private JPanel content;
	private JTable table;

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
		content = new JPanel();
		JScrollPane spane = new JScrollPane(content);
		content.setLayout(null);

		table = new JTable(new MyTableModel());
		table.setSize(400, 400);
		content.add(table);

		getContentPane().add(spane);
		setSize(400, 400);

	}

	private class MyTableModel extends AbstractTableModel {

		private String[] columnNames = new String[] { "Hola", "Mundo" };
		private Object[][] data = { new Object[] { "1", true },
				new Object[] { "3", false } };

		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		public int getRowCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		public Object getValueAt(int arg0, int arg1) {
			return data[arg0][arg1];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
				return true;
		}
		
		public void setValueAt(Object value, int row, int col) {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }

	}
}
