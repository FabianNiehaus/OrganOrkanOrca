package user;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.*;

public class GraphPlotter extends Frame {

	public interface Funktion {
		public abstract int f(int x);
		
		default public boolean testDefinedValue(int x) {
			try {
				int y = f(x);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

	
	//private Canvas canvas = null;
	JPanel canvas = null;
	private Funktion funktion = null;
	private int xScale = 1;
	private int yScale = 1;
	
	
	public GraphPlotter() {
		super("Graph-Plotter");
		
		setLayout(new FlowLayout());
		canvas = new MyGraphCanvas();
		canvas.setPreferredSize(new Dimension(420, 420));
		add(canvas);
		
		pack();
		setVisible(true);
	}

	public void setFunktion(Funktion f) {
		this.funktion = f;
		this.canvas.repaint();
	}

	public void setXScale(int xScale) {
		this.xScale = xScale;
	}

	public void setYScale(int yScale) {
		this.yScale = yScale;
	}

	class MyGraphCanvas extends JPanel {
		
		private final static int X_AXIS_OFFSET = 20;
		private final static int Y_AXIS_OFFSET = 20;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Dimension size = getSize();
			
			g.setColor(Color.white);
			g.fillRect(0, 0, size.width, size.height);			
			
			plot(g);
		}		
		
		private void plot(Graphics g) {
			g.setColor(Color.black);
			
			Dimension size = getSize();

			// X/Y-Achsen
			g.drawLine(0+(X_AXIS_OFFSET/2), size.height-Y_AXIS_OFFSET, size.width-(X_AXIS_OFFSET/2), size.height-Y_AXIS_OFFSET);
			g.drawLine(0+X_AXIS_OFFSET, size.height-(Y_AXIS_OFFSET/2), 0+X_AXIS_OFFSET, 0+(Y_AXIS_OFFSET/2));
			
			// Funktion
			if (funktion != null) {
				Point p1 = null, p2 = null;
				for (int x=0; x*xScale<size.width-2*X_AXIS_OFFSET; x++) {

					// Test: Aufruf einer Default-Methode
					boolean defined = funktion.testDefinedValue(x);
					if (defined) {
						// Funktionswert für aktuelles x berechnen.
						// (funktion ist Eigenschaften der äußeren Klasse "GraphPlotter",
						// auf die die innere Klasse "MyGraphCanvas" zugreifen darf.
						// Zugriff in ausgeschriebener Syntax: GraphPlotter.this.funktion.f(x);
						int y = funktion.f(x); 

						// Zielpunkt der nächsten Linie anhand von Funktionswert berechnen.
						// (xScale und yScale sind Eigenschaften der äußeren Klasse "GraphPlotter",
						// auf die die innere Klasse "MyGraphCanvas" zugreifen darf.)
						p2 = new Point(xScale*x+X_AXIS_OFFSET, size.height-Y_AXIS_OFFSET-yScale*y); 

						// Zeichne Linie zwischen zwei Punkten, wenn beide definiert sind:
						if (p1 != null) {
//							System.out.println("Drawing line: [" + p1.x + "/" + p1.y + " -> " + p2.x + "/" + p2.y + "]");
							g.drawLine(p1.x, p1.y, p2.x, p2.y);
						}

						// Neuer Funktionswert wird Startpunkt für nächste Linie
						p1 = p2; 
					} else {
						// Funktion ist an Stelle x nicht definiert:
						// Startpunkt der Linie zurücksetzen (null) und in einem folgendem 
						// Schleifendurchlauf neu berechnen lassen.
						p1 = null;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		GraphPlotter plotter = new GraphPlotter();
		plotter.setXScale(10);
		plotter.setYScale(1);

		// Funktion als Objekt einer anonymen Klasse
		plotter.setFunktion(new Funktion() {
			@Override
			public int f(int x) {
				return x*x;
			}
		});
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		plotter.setXScale(1);
		plotter.setYScale(1);
		// Funktion als Lambda-Expression
		plotter.setFunktion(x -> x);
	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Funktion mit undefinierten Werten als Lambda-Expression
		plotter.setXScale(2);
		plotter.setFunktion(x -> (x < 50 || x > 100) ? (int) (10*Math.cos(x)) : x/0);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		plotter.setVisible(false);
		plotter.dispose();
	}
}
