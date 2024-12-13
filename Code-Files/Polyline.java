import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jacob Bacus, compelted functionality for CS10 Winter 2024
 */
public class Polyline implements Shape {
	List<Point> points;
	Color color;

	/**
	 * Creates polyline with one point
	 */
	public Polyline (int x1, int y1, Color color) {
		points = new ArrayList<>();
		points.add(new Point(x1, y1));
		this.color = color;
	}

	/**
	 * Creates polyline with two points
	 */
	public Polyline (int x1, int y1, int x2, int y2, Color color) {
		points = new ArrayList<>();
		points.add(new Point(x1, y1));
		points.add(new Point(x2, y2));
		this.color = color;
	}

	/**
	 * adds the next point to the polyline
	 */
	public void addNextPoint(int x, int y) {
		points.add(new Point(x, y));
	}

	@Override
	public void moveBy(int dx, int dy) {
		for (Point point : points) {
			point.x += dx;
			point.y += dy;
		}
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public boolean contains(int x, int y) {
		boolean res = false;
		for (int i = 1; i < points.size(); i++) {
			if (Segment.pointToSegmentDistance(x, y, points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y) <= 3) {
				res = true;
			}
		}

		return res;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		for (int i = 1; i < points.size(); i++) {
			g.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y);
		}
	}

	@Override
	public String toString() {
		String res = "polyline";
		for (Point point : points) { // loops over all points to add them to string
			res = res + " " + point.x + " " + point.y;
		}
		res = res + " " + color.getRGB();
		return res;
	}

	public static void main(String[] args) {
		Polyline myLine = new Polyline(0, 0, 1, 1, new Color(24));
		myLine.addNextPoint(2, 2);
		myLine.addNextPoint(3, 3);
		myLine.addNextPoint(2, 2);
		myLine.addNextPoint(1, 1);

		System.out.println(myLine.contains(5, 5));
		System.out.println(myLine);
	}
}
