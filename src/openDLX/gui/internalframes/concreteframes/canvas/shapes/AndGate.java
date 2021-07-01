package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

public class AndGate extends Shapes {

	public AndGate(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillArc(x -width/2,y - height/2, width, height, 90, -180);
		g.setColor(Color.BLACK);
		g.drawLine(x, y - height/2, x , y + height/2);
		g.drawArc(x -width/2,y - height/2, width, height, 90, -180);
		
	}
	
}
