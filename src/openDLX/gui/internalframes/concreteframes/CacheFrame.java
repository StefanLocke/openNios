package openDLX.gui.internalframes.concreteframes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import openDLX.gui.MainFrame;
import openDLX.gui.internalframes.OpenDLXSimInternalFrame;
import openDLX.gui.internalframes.factories.tableFactories.CacheTableFactory;
import openDLX.gui.internalframes.util.TableSizeCalculator;
import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.RiscVCache;
import riscvSimulator.caches.SingleWayCache;
import riscvSimulator.caches.nWayCache;
import riscvSimulator.values.RiscVValue32;

@SuppressWarnings("serial")
public class CacheFrame extends OpenDLXSimInternalFrame{

	
	private RiscVMemory memory;
	private JTable cacheTable;
	
	public CacheFrame(String name) {
		super(name, false);
		this.memory = MainFrame.getInstance().getSimulator().getMemory();
		initialize();
	}

	
	
	 @Override
	    protected void initialize()
	    {
		 	super.initialize();
		 	System.out.println("Creating CacheFrame");
		 	cacheTable = new CacheTableFactory(memory.getCache()).createTable();
		 	JScrollPane scrollpane = new JScrollPane(cacheTable);
		 	scrollpane.setFocusable(false);
		 	scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 	cacheTable.setFillsViewportHeight(true);
		 	TableSizeCalculator.setDefaultMaxTableSize(scrollpane, cacheTable,
	                TableSizeCalculator.SET_SIZE_BOTH);
	        //config internal frame
	        setLayout(new BorderLayout());
	        add(scrollpane, BorderLayout.CENTER);
	        pack();
	        setVisible(true);
	    }
	
	@Override
	public void update() {
		TableModel model = cacheTable.getModel();
		if (memory.getCache() instanceof SingleWayCache) {
			SingleWayCache cache = (SingleWayCache) memory.getCache();
			for (int i = 0; i < cache.getSize(); i++) {
				RiscVValue32 value = new RiscVValue32(((SingleWayCache) cache).findByte(i, 4).getUnsignedValue() << 24 | 
						(((SingleWayCache) cache).findByte(i, 3).getUnsignedValue() << 16 | 
								(((SingleWayCache) cache).findByte(i, 2).getUnsignedValue() << 8 | 
										(((SingleWayCache) cache).findByte(i, 1).getUnsignedValue()))));
				model.setValueAt(cache.findTag(i),i,SingleWayCache.tagColumn);
				model.setValueAt(value,i,SingleWayCache.valueColumn);
			}
		}
		if (memory.getCache() instanceof nWayCache) {
			nWayCache cache = (nWayCache) memory.getCache();
			for (int i = 0 ; i < cache.wayCount ; i++) {
				for (int j = 0 ; j < cache.getSize() ; j++) {
					RiscVValue32 value = new RiscVValue32(cache.findByte(i,j, 4).getUnsignedValue() << 24 | 
							(cache.findByte(i,j, 3).getUnsignedValue() << 16 | 
									(cache.findByte(i,j, 2).getUnsignedValue() << 8 | 
											(cache.findByte(i,j, 1).getUnsignedValue()))));
					model.setValueAt(cache.findTag(i, j),j,1 + 2*i);
					model.setValueAt(value.getUnsignedValue(),j,1 + 2*i + 1);
				}
			}
		}
		
	}

	@Override
	public void clean() {
		setVisible(false);
        dispose();
		
	}

}
