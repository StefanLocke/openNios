package openSim.gui.internalframes.factories;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ComponentFactory {
	 static public JButton createButton(String name, String tooltip, int mnemonic, String icon_path) 
	    {
	        JButton button = new JButton();
	        URL icon_url;
			try {
				icon_url = new URL(icon_path);
				button.setIcon(new ImageIcon(icon_url)); 
			} catch (MalformedURLException e) {
				button.setText(name);
				e.printStackTrace();
			}
	      
	           
	       
	        button.setMnemonic(mnemonic); 
	        button.setToolTipText(tooltip);
	        button.setFocusable(false);
	        return button;
	    }

}
