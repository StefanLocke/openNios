package openDLX.gui.internalframes.concreteframes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableModel;

import openDLX.gui.MainFrame;
import openDLX.gui.internalframes.OpenDLXSimInternalFrame;
import openDLX.gui.internalframes.factories.tableFactories.CacheTableFactory;
import openDLX.gui.internalframes.util.TableSizeCalculator;
import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.SingleWayCache;
import riscvSimulator.caches.nWayCache;
import riscvSimulator.values.RiscVValue32;

@SuppressWarnings("serial")
public class CacheFrame extends OpenDLXSimInternalFrame implements ActionListener{

	
	private RiscVMemory memory;
	private JTable cacheTable;
	private JRadioButtonMenuItem hexRadio;
	private JRadioButtonMenuItem binRadio;
	private JRadioButtonMenuItem decRadio;
	private JPopupMenu popup;
	private JButton button;
	private int valueFormat; // 0 = hex, 1 = bin , 2 = dec
	
	public CacheFrame(String name) {
		super(name, false);
		valueFormat = 0;
		this.memory = MainFrame.getInstance().getSimulator().getMemory();
		initialize();
	}

	
	
	 @Override
	    protected void initialize()
	    {
		 	super.initialize();
		 
		 	JToolBar controls = new JToolBar();
		 	controls.setFloatable(false);
		 	controls.setRollover(false);
		 	controls.setFocusable(false);
		 	ButtonGroup group = new ButtonGroup();
		 	hexRadio = new JRadioButtonMenuItem("Values as Hex");
		 	binRadio = new JRadioButtonMenuItem("Values as Binary");
		 	decRadio = new JRadioButtonMenuItem("Values as Decimal");
		 	group.add(hexRadio);
		 	group.add(binRadio);
		 	group.add(decRadio);
		 	popup = new JPopupMenu();
		 	popup.add(hexRadio);
		 	popup.add(binRadio);
		 	popup.add(decRadio);
		 	button = createButton("Settings", "Show Settings", "/img/icons/tango/settings.png");
		 	button.addActionListener(this);
		 	controls.add(Box.createHorizontalGlue());
		 	controls.add(button);
		 
		 	
		 	hexRadio.addActionListener(this);
		 	binRadio.addActionListener(this);
		 	decRadio.addActionListener(this);
		 
		 	hexRadio.setSelected(true);
		 	
		 	
		 	
		 	System.out.println("Creating CacheFrame");
		 	cacheTable = new CacheTableFactory(memory.getCache()).createTable();
		 	JScrollPane scrollpane = new JScrollPane(cacheTable);
		 	scrollpane.setFocusable(false);
		 	scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 	cacheTable.setFillsViewportHeight(true);
		 	TableSizeCalculator.setDefaultMaxTableSize(scrollpane, cacheTable,
	                TableSizeCalculator.SET_SIZE_BOTH);
	        //config internal frame
			add(controls,BorderLayout.NORTH);
	        add(scrollpane, BorderLayout.CENTER);
	    
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
					switch (valueFormat) {
						case 1 : //BIN
							model.setValueAt("0x"+cache.findTag(i, j),j,1 + 2*i);
							model.setValueAt("0b"+Long.toBinaryString(value.getUnsignedValue()),j,1 + 2*i + 1);
							break;
						case 2 :
							model.setValueAt("0x"+cache.findTag(i, j),j,1 + 2*i);
							model.setValueAt(value.getUnsignedValue(),j,1 + 2*i + 1);
							break;
						default ://HEX
							model.setValueAt("0x"+cache.findTag(i, j),j,1 + 2*i);
							model.setValueAt("0x"+Long.toHexString(value.getUnsignedValue()),j,1 + 2*i + 1);
					}
					
					
				}
			}
		}
		
	}

	@Override
	public void clean() {
		setVisible(false);	
	}



	@Override
	public void actionPerformed(ActionEvent e) {
			if (e.getSource() == hexRadio) {
				valueFormat = 0;
			}
			if (e.getSource() == binRadio) {
				valueFormat = 1;
			}
			if (e.getSource() == decRadio) {
				valueFormat = 2;
			}
			if (e.getSource() == button) {
				popup.show(button,0,button.getWidth());
			}
			update();
			
	}
	
	private JButton createButton(String name, String tooltip, String icon_path) 
    {
        JButton button = new JButton();
        URL icon_url;
        if((icon_path != null) && ((icon_url = getClass().getResource(icon_path)) != null))
        {
            button.setIcon(new ImageIcon(icon_url));
        }
        else
        {
            button.setText(name);
        }
  
        button.setToolTipText(tooltip);
        button.setFocusable(false);
        return button;
    }

}
