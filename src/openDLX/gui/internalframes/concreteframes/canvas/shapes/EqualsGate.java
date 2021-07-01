package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

public class EqualsGate extends Shapes {
	

	public EqualsGate(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawOval(x - width/2,y - height/2, 
				width, height);
		g2d.setColor(Color.WHITE);
		g2d.fillOval(x - width/2,y - height/2, 
				width, height);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x-7, y-3, x+7, y-3);
		g2d.drawLine(x-7, y+3, x+7, y+3);
	}
	
}
