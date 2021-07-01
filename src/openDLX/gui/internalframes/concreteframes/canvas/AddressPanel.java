package openDLX.gui.internalframes.concreteframes.canvas;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AddressPanel extends JPanel{
	private JTextField tagField;
	private JTextField setField;
	private JTextField offsetField;
	private JTextField selectorField;
	
	public AddressPanel() {
		tagField = new JTextField("0000000000000000000000000");
		setField = new JTextField("000");
		offsetField = new JTextField("00");
		selectorField = new JTextField("00");
		tagField.setFocusable(false);
		setField.setFocusable(false);
		offsetField.setFocusable(false);
		selectorField.setFocusable(false);
		this.add(tagField);
		this.add(setField);
		this.add(offsetField);
		this.add(selectorField);
		this.setBackground(Color.BLACK);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
	}
	
	public int getTagMiddle() {
		return tagField.getX() + tagField.getWidth()/2;
	}
	
	public int getSetMiddle() {
		return setField.getX() + setField.getWidth()/2;
	}
	
	public int getOffsetMiddle() {
		return offsetField.getX() + offsetField.getWidth()/2;
	}
	
	public int getSelectorMiddle() {
		return selectorField.getX() + selectorField.getWidth()/2;
	}
	
	public int getTotalWidth() {
		return tagField.getWidth() + setField.getWidth() + offsetField.getWidth() + selectorField.getWidth();
	}
	public int getTotalHeight() {
		return tagField.getHeight();
	}
	
	public void setTag(Long tag) {
		String s = String.format("%1$25s",Long.toBinaryString(tag)).replace(" ", "0");
		tagField.setText(s);
	}
	public void setSet(Long set) {
		String s = String.format("%1$3s",Long.toBinaryString(set)).replace(" ", "0");
		setField.setText(s);
	}
	public void setOffset(Long offset) {
		String s = String.format("%1$2s",Long.toBinaryString(offset)).replace(" ", "0");
		offsetField.setText(s);
	}
	public void setSelector(Long selector) {
		String s = String.format("%1$2s",Long.toBinaryString(selector)).replace(" ", "0");
		selectorField.setText(s);
	}
}
