package openDLX.gui.internalframes.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import openDLX.gui.internalframes.util.NotSelectableTableModel;

public class MyTableModel extends NotSelectableTableModel {
	
	
	Color RowColor = DEFAULT_COLOR;
	Color DataColor = DEFAULT_COLOR;
	static public Color DEFAULT_COLOR = Color.WHITE;
	
	JTable table;
	int width;
	int height;
	Color[][] colors;
	
	public MyTableModel(JTable table) {
		super();
		this.table = table;
		width = table.getColumnCount();
		height = table.getRowCount();
		colors = new Color[width][height];
		resetColors();
	}
	
	public void init() {
		width = table.getColumnCount();
		height = table.getRowCount();
		colors = new Color[width][height];
		resetColors();
	}
	
	public void resetColors() {
		for (int i = 0; i < width ; i ++) {
			for (int j = 0; j < height ; j++ )  {
				colors[i][j] = DEFAULT_COLOR;
			}
				
		}
	}
	
	public void changeColor(int x, int y, Color color) {
		colors[x][y] = color;
	}
	
	public void changeRowColor(int row, Color color) {
		for (int i = 0; i < width ; i++ ) {
			colors[i][row] = color;
		}
	}
	
	public Color getColorOf(int x,int y) {
		return colors[x][y];
	}
}
