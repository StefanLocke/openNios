package openDLX.gui.internalframes.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import openDLX.gui.internalframes.util.NotSelectableTableModel;

public class MyTableModel extends NotSelectableTableModel {
	
	int row = -1;
	int data = -1;
	Color RowColor = DEFAULT_COLOR;
	Color DataColor = DEFAULT_COLOR;
	static public Color DEFAULT_COLOR = Color.WHITE;
	
	public MyTableModel() {
		super();
	}
	public void setRowColor(int row, Color c) {
			
	      this.row = row;
	     RowColor = c;
	       fireTableRowsUpdated(row, row);
	    }
	public Color getRowColor() {
		return RowColor;
    }
	
	public int getRowToColor() {
		return row;
	}
	
	public Color getDataColor() {
		return DataColor;
	}
	public int getDataToColor() {
		return data;
	}
	public void setBoxColor(int row,int col, Color c) {
	      this.data = col;
	      DataColor = c;
	      fireTableRowsUpdated(row, row);
	 }
	
	
}
