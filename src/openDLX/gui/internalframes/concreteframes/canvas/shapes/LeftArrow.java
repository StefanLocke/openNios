package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import openDLX.gui.internalframes.concreteframes.canvas.TableCanvas;

public class LeftArrow extends Shapes{

	public LeftArrow(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g) {
		g.setStroke(TableCanvas.ARROW_LINE_STROKE);
		g.drawLine(x, y, x + width, y);
		g.drawLine(x, y, x + width/4, y + width/4);
		g.drawLine(x, y, x + width/4, y - width/4);
		g.setStroke(TableCanvas.LINE_STROKE);
	}

}
