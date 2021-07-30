/*
 * Shape.java
 *
 * Apr 20, 2016
 */
package Shape;

import Game.Direction;
import World.Render.Camera;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public abstract class Shape {
	public void arrangeToSide(Shape s, Direction d) {
		switch (d) {
			case NORTH:
				setX(s.getX(d));
				setY(s.getY(d) - getHeight());
				break;
			case SOUTH:
				setX(s.getX(d));
				setY(s.getY(d));
				break;
			case WEST:
				setX(s.getX(d) - getWidth());
				setY(s.getY(d));
				break;
			case EAST:
				setX(s.getX(d));
				setY(s.getY(d));
				break;
				
			case NORTH_WEST:
				setX(s.getX(d) - getWidth());
				setY(s.getY(d) - getHeight());
				break;
			case NORTH_EAST:
				setX(s.getX(d));
				setY(s.getY(d) - getHeight());
				break;
			case SOUTH_WEST:
				setX(s.getX(d) - getWidth());
				setY(s.getY(d));
				break;
			case SOUTH_EAST:
				setX(s.getX(d));
				setY(s.getY(d));
				break;
		}
	};
	
	public abstract boolean intersectsCircle(Circle b);
	public abstract boolean intersectsRect(Rect b);
	
	public abstract void setX(int x);
	public abstract void setY(int y);
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract int getLeftX();
	public abstract int getCenterX();
	public abstract int getRightX();
	public abstract int getTopY();
	public abstract int getCenterY();
	public abstract int getBottomY();
	
	public abstract int getX(Direction d);
	public abstract int getY(Direction d);
	
	
	public static double getAngle(double dx, double dy) {
		double angle;
		if (dx == 0) {
			angle = (dy >= 0 ? Math.PI/2D : Math.PI*3D/2D);
		} else {
			angle = Math.atan2(dy, dx);
		}
		return angle;
	}
	public double getAngleTo(int centerX, int centerY) {
		double dx = centerX - this.getCenterX();
		double dy = centerY - this.getCenterY();
		return getAngle(dx, dy);
	}
	public double getAngleTo(Shape b) {
		return getAngleTo(b.getCenterX(), b.getCenterY());
	}
	
	public abstract void drawImg(Graphics g, Image img, boolean scaled, boolean fit);
	public abstract void draw(Graphics g);
	public abstract void draw(Graphics g, int x, int y);
	public abstract void draw(Graphics g, Camera cam);
	
}