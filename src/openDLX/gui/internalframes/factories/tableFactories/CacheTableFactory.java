package openDLX.gui.internalframes.factories.tableFactories;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import openDLX.gui.internalframes.renderer.ChangeableFrameTableCellRenderer;
import openDLX.gui.internalframes.util.NotSelectableTableModel;
import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.RiscVCache;
import riscvSimulator.caches.SimpleCache;
import riscvSimulator.caches.SingleWayCache;
import riscvSimulator.caches.nWayCache;
import riscvSimulator.values.RiscVValue32;

public class CacheTableFactory extends TableFactory {

	private RiscVCache cache;
	
	public CacheTableFactory(RiscVCache cache) {
		this.cache = cache;
	}
	
	
	@Override
	public JTable createTable() {
		if (cache instanceof SingleWayCache) {
			
			model = new NotSelectableTableModel();
			table = new JTable(model);
			table.setFocusable(false);
			model.addColumn("Set");
			model.addColumn("Tag");
			model.addColumn("Value");
			for (int i = 0; i < cache.getSize(); i++) {
				RiscVValue32 value = new RiscVValue32(((SingleWayCache) cache).findByte(i, 4).getUnsignedValue() << 24 | 
						(((SingleWayCache) cache).findByte(i, 3).getUnsignedValue() << 16 | 
								(((SingleWayCache) cache).findByte(i, 2).getUnsignedValue() << 8 | 
										(((SingleWayCache) cache).findByte(i, 1).getUnsignedValue()))));
				model.addRow(new Object[] {
						"0x" + Integer.toHexString(i),"0x" +Long.toHexString(((SingleWayCache) cache).findTag(i)),value
				});
			}
			
			
			TableColumnModel tcm = table.getColumnModel();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        table.setDefaultRenderer(Object.class, new ChangeableFrameTableCellRenderer());
	
			return table;
		}
		if (cache instanceof nWayCache) {
			nWayCache cache = (nWayCache)this.cache;
			model = new NotSelectableTableModel();
			table = new JTable(model);
			table.setFocusable(false);
			model.addColumn("Set");
			for (int i = 0 ; i < cache.wayCount ; i++) {
				model.addColumn("Way " + i + " -- Tag");
				model.addColumn("Way " + i + " -- Value");
			}
			
			for (int j = 0 ; j < cache.getSize() ; j++) {
				model.addRow(new Object[cache.wayCount]);
				//LinkedList<String> rowStrings = new LinkedList<>();
				for (int i = 0 ; i < cache.wayCount ; i++) {
					RiscVValue32 value = new RiscVValue32(cache.findByte(i,j, 4).getUnsignedValue() << 24 | 
							(cache.findByte(i,j, 3).getUnsignedValue() << 16 | 
									(cache.findByte(i,j, 2).getUnsignedValue() << 8 | 
											(cache.findByte(i,j, 1).getUnsignedValue()))));
					model.setValueAt("0x" +j,j,0);
					model.setValueAt("0x" +Long.toHexString(cache.findTag(i, j)),j,1 + 2*i);
					model.setValueAt(value.getUnsignedValue(),j,1 + 2*i + 1);
				}
			}
			TableColumnModel tcm = table.getColumnModel();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        table.setDefaultRenderer(Object.class, new ChangeableFrameTableCellRenderer());
	        System.out.println("Created Table");
			return table;
		}
		if (cache instanceof SimpleCache) {
			
			model = new NotSelectableTableModel();
			table = new JTable(model);
			table.setFocusable(false);
			model.addColumn("Cached Address");
			model.addColumn("Cached Value");
			List<Long> adresses = cache.getCachedAddresses();
			for (int i = 0; i < cache.getSize(); i++) {
				if (i < adresses.size()) {
					model.addRow(new Object[] {
							adresses.get(i),"Modify CacheTable FActory"
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
		return null;
	}

}
