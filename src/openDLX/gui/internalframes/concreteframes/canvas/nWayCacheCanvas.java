package openDLX.gui.internalframes.concreteframes.canvas;

import javax.swing.JTable;

public class nWayCacheCanvas extends TableCanvas {
	
	public nWayCacheCanvas(JTable table,int wayCount,int setSize) {
		super(table,new AddressPanel(3, new int[] {32-setSize-2,setSize,2}));
	}

}
