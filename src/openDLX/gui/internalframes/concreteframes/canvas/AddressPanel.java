package openDLX.gui.internalframes.concreteframes.canvas;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class AddressPanel extends JPanel{

	
	
	private JTextPane[] fields;
	private JTextPane text;
	private int[] sizes;
	public AddressPanel(int sectionAmmount,int[] sizes) {
		this.sizes = sizes;
		fields = new JTextPane[sectionAmmount];
		for (int i = 0; i < sectionAmmount; i++) {
			JTextPane field = new JTextPane();
			field.setText(generateZeros(sizes[i]));
			field.setFocusable(false);
			field.setBackground(null);
			field.setBorder(new LineBorder(Color.gray));
			this.add(field);
			fields[i] = field;
		}
		/*
		tagField = new JTextField("0000000000000000000000000");
		setField = new JTextField("000");
		offsetField = new JTextField("00");
		selectorField = new JTextField("00");
		*/
		
		text = new JTextPane();
		text.setText("= " + "0xFFFFFFFF");
		text.setFocusable(false);
		text.setBackground(null);
		this.add(text);
		((FlowLayout)this.getLayout()).setVgap(0);
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
	
	public void setAddress(long address) {
		text.setText("= 0x" + String.format("%1$08X",address ));
	}
	
	private String generateZeros(int ammount) {
		String s = "";
		for (int i = 0 ; i < ammount ; i ++) {
			s =s + "0"; 
		}
		return s;
	}
}
