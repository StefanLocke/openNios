package openSim.gui.internalframes.factories.tableFactories;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import openSim.gui.internalframes.renderer.CacheFrameTableCellRenderer;
import openSim.gui.internalframes.renderer.CenteredHeaderRenderer;
import openSim.gui.internalframes.renderer.ChangeableFrameTableCellRenderer;
import openSim.gui.internalframes.renderer.MyTableModel;
import openSim.gui.internalframes.util.NotSelectableTableModel;
import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.RiscVCache;
import riscvSimulator.caches.SimpleCache;
import riscvSimulator.caches.SingleWayCache;
import riscvSimulator.caches.lineCache.LineCache;
import riscvSimulator.caches.wayCache.nWayCache;
import riscvSimulator.values.RiscVValue32;

public class CacheTableFactory extends TableFactory {
	
	public static final int LINECACHE_SET_COL = 0;
	public static final int LINECACHE_VALID_COL = 1;
	public static final int LINECACHE_TAG_COL = 2;
	public static final int LINECACHE_DATA_OFFSET = 3;
	
	public static final int NWAY_SET_COL = 0;
	public static final int NWAY_WAY_SIZE = 3;
	public static final int NWAY_WAY_OFFSET = 1;
	public static final int NWAY_VALID_OFFSET = 0;
	public static final int NWAY_TAG_OFFSET = 1;
	public static final int NWAY_DATA_OFFSET = 2;
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
			
			table = new JTable();
			model = new MyTableModel(table);
			table.setModel(model);
			table.setFocusable(false);
			model.addColumn("Set");
			for (int i = 0 ; i < cache.wayCount ; i++) {
				model.addColumn("WAY[" + i + "].Valid");
				model.addColumn("WAY[" + i + "].Tag");
				model.addColumn("WAY[" + i + "].Value");
			}
			
			for (int j = 0 ; j < cache.getSize() ; j++) {
				model.addRow(new Object[cache.wayCount]);
				//LinkedList<String> rowStrings = new LinkedList<>();
				for (int i = 0 ; i < cache.wayCount ; i++) {
					RiscVValue32 value = new RiscVValue32(cache.findByte(i,j, 4).getUnsignedValue() << 24 | 
							(cache.findByte(i,j, 3).getUnsignedValue() << 16 | 
									(cache.findByte(i,j, 2).getUnsignedValue() << 8 | 
											(cache.findByte(i,j, 1).getUnsignedValue()))));
					model.setValueAt("0x" +j,j,NWAY_SET_COL);
					model.setValueAt("0", j, NWAY_WAY_OFFSET + NWAY_VALID_OFFSET + i * NWAY_WAY_SIZE);
					model.setValueAt("0x" +Long.toHexString(cache.findTag(i, j)),j,NWAY_WAY_OFFSET + NWAY_TAG_OFFSET + i * NWAY_WAY_SIZE);
					model.setValueAt("0x" +value.getUnsignedValue(),j,NWAY_WAY_OFFSET + NWAY_DATA_OFFSET + i * NWAY_WAY_SIZE);
				}
			}
			TableColumnModel tcm = table.getColumnModel();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setFocusable(false);
			table.getTableHeader().setReorderingAllowed(false);
			table.getTableHeader().setResizingAllowed(false);
	        table.setDefaultRenderer(Object.class, new CacheFrameTableCellRenderer());
	        table.setShowGrid(false);
	        table.setRowMargin(0);
	        table.getTableHeader().setDefaultRenderer(new CenteredHeaderRenderer());
	        table.setPreferredSize(new Dimension(500,table.getPreferredSize().height));
	        tcm.setColumnMargin(0);
	        
	        ((MyTableModel)model).init();
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
		if (cache instanceof LineCache) {
			LineCache cache = (LineCache)this.cache;
			
			table = new JTable();
			model = new MyTableModel(table);
			table.setModel(model);
			
			table.setFocusable(false);
			model.addColumn("SET");
			model.addColumn("VALID");
			model.addColumn("TAG");
			for (int i = 0 ; i < Math.pow(2, cache.offsetLength) ; i++) {
				model.addColumn("DATA[" + i + "]");
			}
			
			for (int i = 0; i < cache.getSize() ; i++) {
				model.addRow(new Object[((int) Math.pow(2, cache.offsetLength))+2]);
				model.setValueAt("0x" + Integer.toHexString(i), i, LINECACHE_SET_COL);
				model.setValueAt("0", i, LINECACHE_VALID_COL);
				model.setValueAt("0x0", i, LINECACHE_TAG_COL);
				for (int j = 0; j < Math.pow(2, cache.offsetLength) ; j++) {
					model.setValueAt("0x0", i, j+LINECACHE_DATA_OFFSET);
				}
			}
			TableColumnModel tcm = table.getColumnModel();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setFocusable(false);
			table.getTableHeader().setReorderingAllowed(false);
			table.getTableHeader().setResizingAllowed(false);
	        table.setDefaultRenderer(Object.class, new CacheFrameTableCellRenderer());
	        table.setShowGrid(false);
	        table.setRowMargin(0);
	        table.setPreferredSize(new Dimension(500,table.getPreferredSize().height));
	        table.getTableHeader().setDefaultRenderer(new CenteredHeaderRenderer());
	        tcm.setColumnMargin(0);
	        ((MyTableModel)model).init();
	        System.out.println("Created Table");
			return table;
		}
		return null;
	}

}
