package openDLX.gui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class MainePanel extends JPanel {
	private JPanel container7;
	private JPanel container2;
	private JPanel container3;
	private JPanel container4;
	private JPanel container1;
	private JPanel container5;
	private JPanel container6;
	private JPanel container8;

	/**
	 * Create the panel.
	 */
	public MainePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane.setLeftComponent(splitPane_1);
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_1.setLeftComponent(splitPane_4);
		
		JSplitPane splitPane_5 = new JSplitPane();
		splitPane_5.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_4.setRightComponent(splitPane_5);
		
		JSplitPane splitPane_6 = new JSplitPane();
		splitPane_5.setLeftComponent(splitPane_6);
		
		container2 = new JPanel();
		splitPane_6.setLeftComponent(container2);
		container2.setLayout(new BorderLayout(0, 0));
		
		container3 = new JPanel();
		splitPane_6.setRightComponent(container3);
		container3.setLayout(new BorderLayout(0, 0));
		
		container4 = new JPanel();
		splitPane_5.setRightComponent(container4);
		container4.setLayout(new BorderLayout(0, 0));
		
		container1 = new JPanel();
		splitPane_4.setLeftComponent(container1);
		container1.setLayout(new BorderLayout(0, 0));
		
		container5 = new JPanel();
		splitPane_1.setRightComponent(container5);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setContinuousLayout(true);
		splitPane.setRightComponent(splitPane_2);
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_2.setRightComponent(splitPane_3);
		
		container8 = new JPanel();
		splitPane_3.setRightComponent(container8);
		container8.setLayout(new BorderLayout(0, 0));
		
		container7 = new JPanel();
		splitPane_3.setLeftComponent(container7);
		container7.setLayout(new BorderLayout(0, 0));
		
		container6 = new JPanel();
		splitPane_2.setLeftComponent(container6);
		container6.setLayout(new BorderLayout(0, 0));

	}

	public JPanel getContainer7() {
		return container7;
	}
	public JPanel getContainer2() {
		return container2;
	}
	public JPanel getContainer3() {
		return container3;
	}
	public JPanel getContainer4() {
		return container4;
	}
	public JPanel getContainer1() {
		return container1;
	}
	public JPanel getContainer5() {
		return container5;
	}
	public JPanel getContainer6() {
		return container6;
	}
	public JPanel getContainer8() {
		return container8;
	}
}
