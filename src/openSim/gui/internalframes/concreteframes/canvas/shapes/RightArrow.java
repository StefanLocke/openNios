package openSim.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Graphics2D;

import openSim.gui.internalframes.concreteframes.canvas.TableCanvas;

public class RightArrow extends Shapes{

	public RightArrow(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setStroke(TableCanvas.ARROW_LINE_STROKE);
		g.drawLine(x, y, x + width, y);
		g.drawLine(x+width, y, x+width - width/4, y + width/4);
		g.drawLine(x+width, y, x+width - width/4, y - width/4);
		g.setStroke(TableCanvas.LINE_STROKE);
	}

}
