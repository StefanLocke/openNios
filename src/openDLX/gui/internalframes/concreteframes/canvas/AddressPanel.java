package openDLX.gui.internalframes.concreteframes.canvas;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AddressPanel extends JPanel{

	
	
	private JTextField[] fields;
	private int[] sizes;
	public AddressPanel(int sectionAmmount,int[] sizes) {
		this.sizes = sizes;
		fields = new JTextField[sectionAmmount];
		for (int i = 0; i < sectionAmmount; i++) {
			JTextField field = new JTextField(generateZeros(sizes[i]));
			field.setFocusable(false);
			this.add(field);
			fields[i] = field;
		}
		/*
		tagField = new JTextField("0000000000000000000000000");
		setField = new JTextField("000");
		offsetField = new JTextField("00");
		selectorField = new JTextField("00");
		*/
		this.setBackground(Color.BLACK);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
	}
	
	public int getSectionMiddle(int section) {
		return fields[section].getX() + fields[section].getWidth()/2;
	}
	
	
	public int getTotalWidth() {
		return  this.getWidth();
	}
	public int getTotalHeight() {
		return this.getHeight();
	}
	
	
	public void setContent(int section, Long value) {
		String s = String.format("%1$"+ sizes[section] +"s",Long.toBinaryString(value)).replace(" ", "0");
		fields[section].setText(s);
	}
	
	private String generateZeros(int ammount) {
		String s = "";
		for (int i = 0 ; i < ammount ; i ++) {
			s =s + "0"; 
		}
		return s;
	}
}
