package openDLX.gui.internalframes.factories.tableFactories;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import openDLX.gui.internalframes.renderer.ChangeableFrameTableCellRenderer;
import openDLX.gui.internalframes.util.NotSelectableTableModel;
import riscvSimulator.RiscVMemory;

public class CacheTableFactory extends TableFactory {

	private RiscVMemory memory;
	
	public CacheTableFactory(RiscVMemory memory) {
		this.memory = memory;
	}
	
	
	@Override
	public JTable createTable() {
		model = new NotSelectableTableModel();
		table = new JTable(model);
		table.setFocusable(false);
		model.addColumn("Cached Address");
		model.addColumn("Cached Value");
		List<Long> adresses = memory.getCachedAddresses();
		for (int i = 0; i < 5; i++) {
			if (i < adresses.size()) {
				model.addRow(new Object[] {
						adresses.get(i),memory.loadWord(adresses.get(i), false)
						}
					);
			}
			else {
				model.addRow(new Object[] {
						"---" ,"---" 
						}
					);
			}
			
		}
		
		TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setMaxWidth(60);
        tcm.getColumn(1).setMaxWidth(150);
        table.setDefaultRenderer(Object.class, new ChangeableFrameTableCellRenderer());

		return table;
	}

}
