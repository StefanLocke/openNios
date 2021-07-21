package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

public class OrGate extends Shapes{

	public OrGate(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.inX = x;
		this.inY = y-height/2;
		this.inYbis = y+height/2;
	}
	
	int inX;
	int inY;
	int inYbis;
	
	public OrGate(int inX,int inY,int inYbis,int width,int height) {
		super(inX, (inY + inYbis) /2,width, Math.abs(inY-inYbis)*2);
		this.inX = inX;
		this.inY = inY;
		this.inYbis = inYbis;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillArc(x -width/2,y - height/2, width, height, 90, -180);
		g.setColor(Color.BLACK);
		g.drawArc(x,y - height/2, 5, height, 90, -180);
		g.drawArc(x -width/2,y - height/2, width, height, 90, -180);
	}
	
	public int getInX() {
		return inX;
	}
	
	public int getInY() {
		return inY;
	}
	
	public int getInYBis() {
		return inYbis;
	}

}
