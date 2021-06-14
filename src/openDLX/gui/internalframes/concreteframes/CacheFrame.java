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

import openDLX.gui.MainFrame;
import openDLX.gui.internalframes.OpenDLXSimInternalFrame;
import openDLX.gui.internalframes.factories.tableFactories.CacheTableFactory;
import openDLX.gui.internalframes.util.TableSizeCalculator;
import riscvSimulator.RiscVMemory;

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
		 	cacheTable = new CacheTableFactory(memory).createTable();
		 	JScrollPane scrollpane = new JScrollPane(cacheTable);
		 	scrollpane.setFocusable(false);
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
		List<Long> addresses = memory.getCachedAddresses();
		for (int i = 0; i < 5; i++ ) {
			if (i < addresses.size()) {
				cacheTable.getModel().setValueAt(addresses.get(i), i, 0);
				cacheTable.getModel().setValueAt(memory.loadWord(addresses.get(i), false), i, 1);
			}
			else {
				cacheTable.getModel().setValueAt("---", i, 0);
				cacheTable.getModel().setValueAt("---", i, 1);
			}
		}
		
	}

	@Override
	public void clean() {
		setVisible(false);
        dispose();
		
	}

}
