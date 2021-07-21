package openDLX.gui.internalframes.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class CenteredHeaderRenderer extends ChangeableFrameTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JLabel jComp = (JLabel)comp;
		jComp.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		return comp;
	}

}
