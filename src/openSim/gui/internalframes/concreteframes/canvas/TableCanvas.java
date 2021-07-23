package openSim.gui.internalframes.concreteframes.canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import openSim.gui.internalframes.concreteframes.canvas.shapes.AndGate;
import openSim.gui.internalframes.concreteframes.canvas.shapes.EqualsGate;
import openSim.gui.internalframes.concreteframes.canvas.shapes.LeftArrow;
import openSim.gui.internalframes.concreteframes.canvas.shapes.MultiplexerGate;
import openSim.gui.internalframes.concreteframes.canvas.shapes.RightArrow;
import openSim.gui.internalframes.concreteframes.canvas.shapes.Shapes;
import openSim.gui.internalframes.concreteframes.canvas.shapes.Text;
import openSim.gui.internalframes.renderer.CacheFrameTableCellRenderer;
import openSim.gui.internalframes.renderer.MyTableModel;

public class TableCanvas extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Stroke LINE_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final Stroke ARROW_LINE_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final Color LINE_COLOR = Color.BLACK;
	
	
	JTable table;
	protected JPanel tablePanel ;
	protected AddressPanel address;
	
	protected int textFieldOffset = 200;
	protected int outputX = 50; //100 + tableX + table width
	
	
	
	
	
	public TableCanvas(JTable table,AddressPanel address) {
		
			this.setLayout(null);
			this.table = table;
			tablePanel = new  JPanel();
			tablePanel.setLayout(new BorderLayout());
			tablePanel.add(table.getTableHeader(),BorderLayout.NORTH);
			tablePanel.add(table,BorderLayout.CENTER);
			this.address = address;
			
			table.setMaximumSize(new Dimension(getTableWidth(),getTableHeight()));
			
			this.add(tablePanel);
		
			this.add(address);
			
			this.tablePanel.setBounds(50, 20,table.getPreferredSize().width, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height);
			this.address.setBounds(50, getTableY() + getTableHeight() + textFieldOffset  ,address.getPreferredSize().width , address.getPreferredSize().height);
			
			
		
		}
	public TableCanvas(JTable table) {
		this(table, new AddressPanel(3, new int[]{27,3,2}));
	}

		
		protected int getTableX() {
			return tablePanel.getX();
		}
		
		protected int getTableY() {
			return tablePanel.getY();
		}
		

		protected int getTableWidth() {
			return tablePanel.getWidth();
		}
		
		protected int getTableHeight() {
			return tablePanel.getHeight();
		}
		public AddressPanel getAddressField() {
			return address;
		}
		
		protected int getColumnWidth(int colNumber) {
			return table.getColumnModel().getColumn(colNumber).getWidth();
		}
		
		protected int getRowX(int rowNumber) {
			int r = table.getTableHeader().getHeight();
			for (int i = 0 ; i < rowNumber; i++) {
				r = r + table.getRowHeight(i);
			}
			return r;
		}
		
		protected int getColumnX(int colNumber) {
			int r = 0;
			for (int i = 0 ; i < colNumber; i++) {
				r = r + table.getColumnModel().getColumn(i).getWidth();
			}
			return r;
		}
		
		
		@Override
		protected void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			repaint();
		}
		
		
}
