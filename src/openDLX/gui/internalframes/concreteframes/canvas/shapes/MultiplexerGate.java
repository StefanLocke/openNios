package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

public class MultiplexerGate extends Shapes {

	public MultiplexerGate(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillPolygon(new int[] {x - width/2,x - width/2,x + width/2,x + width/2},new int[] {y - height/2,y + height/2 ,y + height/2 - 10,y - height/2+ 10}, 4);
		g.setColor(Color.BLACK);
		g.drawPolygon(new int[] {x - width/2,x - width/2,x + width/2,x + width/2},new int[] {y - height/2,y + height/2 ,y + height/2 - 10,y - height/2+ 10}, 4);	
	}
	
}
