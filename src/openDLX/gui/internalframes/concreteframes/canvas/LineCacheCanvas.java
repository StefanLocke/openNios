package openDLX.gui.internalframes.concreteframes.canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import openDLX.gui.internalframes.concreteframes.canvas.shapes.EqualsGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.LeftArrow;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.MultiplexerGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.RightArrow;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.Shapes;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.Text;
import openDLX.gui.internalframes.renderer.MyTableModel;

public class LineCacheCanvas extends TableCanvas  {
	int coloredData;
	int coloredSet;
	int offsetSize;
	public int hitStatus = -1;
	
	public LineCacheCanvas(JTable table,int offsetSize) {
		super(table);
		
		coloredData = -1;
		coloredSet = -1;
		this.offsetSize = offsetSize;
	}

	private void hightlightDataWay(int way) {
		coloredData = way;
	}
	
	
	
	
	private void hightlightTab(int set,int data,boolean read,int hit) {
		coloredSet = set;
		MyTableModel model = (MyTableModel) table.getModel();
		model.setRowColor(set, Color.YELLOW);
		if (read) if (hit > 0) model.setBoxColor(set,data,Color.GREEN); else  model.setBoxColor(set,data,Color.RED);
		else model.setBoxColor(set,data,Color.ORANGE);
		
	}
	
	public void highlight(int set,int data,boolean read,int hit) {
		hightlightDataWay(data);
		hightlightTab(set,data+2,read,hit);
	}
	
	public void drawInputArrow(int set) {
		arrowDirection = false;
		arrowSet  = set;
	}
	
	public void drawOutputArrow(int set) {
		arrowDirection = true;
		arrowSet  = set;
	}
	
	private void drawArrow(int row,Graphics2D g) {
		Shapes s;
		if (arrowDirection) {
			s = new RightArrow(getTableX() + getTableWidth() + 10, getTableY() + getRowX(row) + table.getRowHeight()/2, 20, 10);
		}
		else {
			s = new LeftArrow(getTableX() + getTableWidth() + 10, getTableY() + getRowX(row) + table.getRowHeight()/2, 20, 10);
		}
		s.draw(g);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setStroke(new BasicStroke(2));
		
		Point p = new Point(getTableX() + getTableWidth() + 20, getTableY() + getTableHeight() + 37);
		MultiplexerGate gate = new MultiplexerGate(p.x, p.y, 20, 60);
		
		drawOffsetLine(Color.BLACK, g2d,gate);
		drawDataLines(g2d,gate);
		if (hitStatus < 0) drawEqualsResult(Color.BLACK, g2d);
		if (hitStatus == 0) drawEqualsResult(Color.RED, g2d);
		if (hitStatus == 1) drawEqualsResult(Color.GREEN, g2d);
		drawTagLine(Color.BLACK, g2d);
		drawSetLine(coloredSet,Color.BLACK, g2d);
		gate.draw(g2d);
		if (arrowSet >= 0) {
			drawArrow(arrowSet, g2d);
		}
		repaint();
	}
	
	
	
	
	private void drawDataLines(Graphics2D g,MultiplexerGate gate) {
		for (int i = 0; i < Math.pow(2, offsetSize); i++) {
			if (i == coloredData) drawDataLines(i,Color.GREEN,g); else drawDataLines(i,Color.BLACK,g);
		}
		
		if (coloredData >= 0) g.setColor(Color.GREEN);
		Point p2 = new Point(outputX + getTableX() + getTableWidth(),gate.y);
		Text text = new Text(p2.x+5, p2.y+5,"DATA OUT");
		g.drawLine(p2.x, p2.y, gate.x, gate.y);
		g.setColor(Color.BLACK);
		text.draw(g);
	
		
	
	}
	
	private void drawDataLines(int col,Color color, Graphics2D g) {
		g.setColor(color);
		Point tmp1 = new Point(getTableX() + getColumnX(col + 2) + getColumnWidth(col + 2)/2, getTableY() + getTableHeight());
		Point tmp2 = new Point(tmp1,0, 60 - (int)(60/ Math.pow(2, offsetSize) )*col);
		Point tmp3 = new Point(getTableX() + getTableWidth() + 20, tmp2.y);
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);
		g.drawLine(tmp2.x, tmp2.y, tmp3.x, tmp3.y);	
		g.setColor(Color.BLACK);
	}
	
	private void drawSetLine(int row,Color color, Graphics2D g) { //TODO set specific row
		Point tmp1;
		if (row < 0) tmp1 = new Point(getTableX(), getTableY() + getTableHeight()/2);
		else tmp1 = new Point(getTableX(), getTableY() + getRowX(row) + table.getRowHeight()/2);
		
		Point tmp2 = new Point(tmp1,-20,0);
		Point tmp3 = new Point(tmp2.x,address.getY() + address.getHeight() + 20);
		Point tmp4 = new Point(address.getX() + address.getSetMiddle(), tmp3.y);
		Point tmp5 = new Point(tmp4,0,-20);
		drawLine(tmp1, tmp2,g);
		drawLine(tmp2, tmp3,g);
		drawLine(tmp4, tmp3,g);
		drawLine(tmp4, tmp5,g);
		
	}
	
	private void drawOffsetLine(Color color, Graphics2D g,MultiplexerGate gate) { //TODO set specific row
		Point tmp1 = new Point(address.getX() + address.getOffsetMiddle(), address.getY() + address.getHeight());
		Point tmp2 = new Point(tmp1,0,20);
		Point tmp3 = new Point(gate.x,tmp2.y);
		Point tmp4 = new Point(gate.x,gate.y);
		drawLine(tmp1, tmp2, g);
		drawLine(tmp3, tmp2, g);
		drawLine(tmp4, tmp3, g);
	}
	
	
	private void drawTagLine(Color color, Graphics2D g) {
		Point tmp1 = new Point(address.getX() + address.getTagMiddle(), address.getY());
		Point tmp2 = new Point(tmp1,0,-30);
		
		Point tmp3 = new Point(getTableX() + getColumnX(1) + getColumnWidth(1)/2,getTableY() + getTableHeight());
		Point tmp4 = new Point(tmp3,0,20);
		Point tmp5 = new Point(tmp2.x,tmp4.y);
		EqualsGate equal = new EqualsGate(tmp2.x, tmp2.y-15, 30, 30);
		drawLine(tmp1, tmp2,g);
		drawLine(tmp3, tmp4,g);
		drawLine(tmp5, tmp4,g);
		drawLine(tmp5, tmp2,g);
		equal.draw(g);
		
	}
	
	private void drawEqualsResult(Color color, Graphics2D g) {
		g.setColor(color);
		Point tmp1 = new Point(address.getX() + address.getTagMiddle(), address.getY() -45);
		Point tmp2 = new Point(getTableX() + getTableWidth() + outputX,tmp1.y);
		Text text = new Text(tmp2.x+5, tmp2.y+5,"HIT/MISS");
		drawLine(tmp1, tmp2, g);
		g.setColor(LINE_COLOR);
		text.draw(g);
		
	}
	
	private void drawLine(Point p1, Point p2,Graphics2D g) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	class Point{
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Point(Point p, int xOffset,int yOffset) {
			this.x = p.x + xOffset;
			this.y = p.y + yOffset;
		}
	}
}
