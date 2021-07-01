package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Graphics2D;

public abstract class Shapes {
	public int x;
	public int y;
	public int height;
	public int width;
	public Shapes(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	abstract public void draw(Graphics2D g);
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int w) {
		this.width = w;
	}
	
	public void setHeight(int h) {
		this.height = h;
	}
	
}
