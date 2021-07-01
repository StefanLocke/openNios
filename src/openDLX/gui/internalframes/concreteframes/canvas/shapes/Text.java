package openDLX.gui.internalframes.concreteframes.canvas.shapes;

import java.awt.Graphics2D;

public class Text extends Shapes {

	String text;
	public Text(int x, int y,String text) {
		super(x, y, 0, 0);
		this.text = text;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawString(text, x, y);
	}

}
