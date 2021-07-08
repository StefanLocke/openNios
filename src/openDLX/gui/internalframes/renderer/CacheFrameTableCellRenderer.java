package openDLX.gui.internalframes.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Label;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class CacheFrameTableCellRenderer extends ChangeableFrameTableCellRenderer{
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		MyTableModel model = (MyTableModel) table.getModel();
		int coloredRow = model.getRowToColor();
		int coloredData= model.getDataToColor();
		//comp.setBackground(model.DEFAULT_COLOR);
		if (coloredRow == row) comp.setBackground(model.getRowColor());
		if (coloredRow == row && coloredData == column) comp.setBackground(model.getDataColor());
		JLabel jComp = (JLabel)comp;
		jComp.setBorder(new MatteBorder(0, 0, 0, 1, Color.BLACK));
		jComp.setHorizontalAlignment(SwingConstants.CENTER);
		jComp.setBorder(new MatteBorder(
				row==0?1:0,
				0,
				column!=0?1:0,
				1,
				Color.BLACK));
		return comp;
	}

}
