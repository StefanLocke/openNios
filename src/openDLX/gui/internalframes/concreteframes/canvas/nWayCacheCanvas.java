package openDLX.gui.internalframes.concreteframes.canvas;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTable;

import openDLX.gui.internalframes.concreteframes.canvas.shapes.AndGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.EqualsGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.MultiplexerGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.OrGate;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.Shapes;
import openDLX.gui.internalframes.concreteframes.canvas.shapes.Text;
import openDLX.gui.internalframes.factories.tableFactories.CacheTableFactory;
import openDLX.gui.internalframes.renderer.MyTableModel;

public class nWayCacheCanvas extends TableCanvas {
	
	public nWayCacheCanvas(JTable table,int wayCount,int setSize) {
		super(table,new AddressPanel(3, new int[] {32-setSize-2,setSize,2}));
		this.wayCount = wayCount;
		this.setSize = setSize;
		this.lastSet = 0;
		this.lastWay = 0;
		this.lastOperationType = false;
		this.lastHitMiss = 0;
	}
	
	int lastSet;
	int lastWay;
	boolean lastOperationType;
	int lastHitMiss;
	MultiplexerGate multiGate;
	OrGate orGate;
	int wayCount;
	int setSize;
	
	
	public void displayEvent(long address,long tag, long set, int way,long selector,boolean read,boolean hit) {
		this.address.setContent(0,tag);
		this.address.setContent(1,set);
		this.address.setContent(2,selector);
		this.address.setAddress(address);
		lastOperationType = read;
		lastWay = way;
		lastSet = (int)set;
		if (hit) this.lastHitMiss = 1; else this.lastHitMiss = 0;
		ColorTable();
	}
	
	private void ColorTable() {
		int row = lastSet;
		int col = (lastWay*2) + 2;
		boolean read = lastOperationType;
		MyTableModel model = (MyTableModel) table.getModel();
		model.resetColors();
		model.changeRowColor(row, Color.YELLOW);
		if (read) { 
			if (lastHitMiss > 0) 
				model.changeColor(col, row,Color.GREEN); 
			else  
				model.changeColor(col,row,Color.RED);
		}
		else {
			model.changeColor(col,row,Color.ORANGE);
		}
		
	}
	
	
	
	
	
	@Override
	public void paintComponent(Graphics g ) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setStroke(new BasicStroke(2));
		
		Point p = new Point(getTableX() + getTableWidth() + 20, getTableY() + getTableHeight() + 37);
		multiGate  = new MultiplexerGate(p.x, p.y, 20, 60);
		orGate = new OrGate(p.x, p.y+textFieldOffset, 40, 30);
		drawDataLines(g2d);
		drawTagLines(g2d);
		drawSetLine(lastSet, LINE_COLOR, g2d);
		multiGate.draw(g2d);
		orGate.draw(g2d);
	}
	
	private void drawSetLine(int row,Color color, Graphics2D g) { //TODO set specific row
		Point tmp1;
		if (row < 0) tmp1 = new Point(getTableX(), getTableY() + getTableHeight()/2);
		else tmp1 = new Point(getTableX(), getTableY() + getRowX(row) + table.getRowHeight()/2);
		
		Point tmp2 = new Point(tmp1,-20,0);
		Point tmp3 = new Point(tmp2.x,address.getY() + address.getHeight() + 20);
		Point tmp4 = new Point(address.getX() + address.getSectionMiddle(1), tmp3.y);
		Point tmp5 = new Point(tmp4,0,-20);
		drawLine(tmp1, tmp2,g);
		drawLine(tmp2, tmp3,g);
		drawLine(tmp4, tmp3,g);
		drawLine(tmp4, tmp5,g);
		
	}
	
	
	
	
	
	
	private void drawDataLines(Graphics2D g) {
		for (int i = 0; i < wayCount; i++) {
			if (i == lastWay) drawDataLine(i,Color.GREEN,g); else drawDataLine(i,Color.BLACK,g);
		}
		
		if (lastWay >= 0) g.setColor(Color.GREEN);
		Point p2 = new Point(outputX + getTableX() + getTableWidth(),multiGate.y);
		Text text = new Text(p2.x+5, p2.y+5,"DATA OUT");
		g.drawLine(p2.x, p2.y, multiGate.x, multiGate.y);
		g.setColor(Color.BLACK);
		text.draw(g);
	
		
	
	}
	
	private void drawDataLine(int col,Color color, Graphics2D g) {
		g.setColor(color);
		Point tmp1 = new Point(getTableX() + getColumnX(CacheTableFactory.NWAY_WAY_OFFSET + CacheTableFactory.NWAY_DATA_OFFSET + col * CacheTableFactory.NWAY_WAY_SIZE) + getColumnWidth(CacheTableFactory.NWAY_WAY_OFFSET + CacheTableFactory.NWAY_DATA_OFFSET + col * CacheTableFactory.NWAY_WAY_SIZE)/2, getTableY() + getTableHeight());
		Point tmp2 = new Point(tmp1,0, 60 - (int)(60/wayCount)*col);
		Point tmp3 = new Point(getTableX() + getTableWidth() + 20, tmp2.y);
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);
		g.drawLine(tmp2.x, tmp2.y, tmp3.x, tmp3.y);	
		g.setColor(Color.BLACK);
	}
	
	private void drawTagLines(Graphics2D g) {
		Point tmp1 = new Point(address.getX() + address.getSectionMiddle(0), address.getY());
		Point tmp2 = new Point(tmp1,0,-20);
		Point tmp3 = new Point(getTableX() + getColumnX((wayCount-1)*CacheTableFactory.NWAY_WAY_SIZE + CacheTableFactory.NWAY_TAG_OFFSET + CacheTableFactory.NWAY_WAY_OFFSET ) + getColumnWidth((wayCount-1)*CacheTableFactory.NWAY_WAY_SIZE + CacheTableFactory.NWAY_TAG_OFFSET + CacheTableFactory.NWAY_WAY_OFFSET )/2,tmp2.y);
		drawLine(tmp1, tmp2, g);
		drawLine(tmp3, tmp2, g);
		drawTagLine(0, g);
		drawTagLine(1, g);
		orGate = new OrGate(getTableX() + getTableWidth(), address.getY()-50, 40, 30);
		Point tmp4 = new Point(orGate.x-40,address.getY()-60);
		Point tmp5 = new Point(tmp4.x,(tmp4.y+multiGate.y)/2);
		Point tmp6 = new Point(multiGate.x,tmp5.y);
		Point tmp7 = new Point(multiGate.x,multiGate.y);
		Point tmp8 = new Point(getTableX() + getTableWidth() +outputX, orGate.y);
		Point tmp9 = new Point(orGate.x, orGate.y);
		drawLine(tmp4, tmp5, g);
		drawLine(tmp6, tmp5, g);
		drawLine(tmp6, tmp7, g);
		drawLine(tmp6, tmp7, g);
		drawLine(tmp8, tmp9, g);
		
		Text text = new Text(tmp8.x+5, tmp8.y+5,"HIT/MISS");
		text.draw(g);
		//AndGate gate = new AndGate(getTableX() + getTableWidth() + 20, getTableY()+getTableHeight() + 80 + 0*25, getTableY()+getTableHeight() + 80 + 1*25, 30, 30);
		//gate.draw(g);
	}
	private void drawTagLine(int way,Graphics2D g) {
		Point tmp1 = new Point(getTableX() + getColumnX(CacheTableFactory.NWAY_WAY_OFFSET + CacheTableFactory.NWAY_TAG_OFFSET + way * CacheTableFactory.NWAY_WAY_SIZE) + getColumnWidth(CacheTableFactory.NWAY_WAY_OFFSET + CacheTableFactory.NWAY_TAG_OFFSET + way * CacheTableFactory.NWAY_WAY_SIZE)/2 , getTableY()+getTableHeight());
		Point tmp2 = new Point(tmp1.x,address.getY()-20);
		Point gateTmp = new Point(tmp2.x, tmp2.y - 20*(wayCount-way));
		Shapes s  = new EqualsGate(gateTmp.x, gateTmp.y, 30, 30);
		Point tmp5;
		tmp5 = new Point(getTableX() + getTableWidth() ,gateTmp.y);
		
		
		drawLine(tmp1, tmp2, g);
		drawLine(gateTmp, tmp5, g);
		s.draw(g);
		
		
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
