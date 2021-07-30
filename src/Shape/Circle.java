/*
 * Circle.java
 *
 * Apr 20, 2016
 */
package Shape;

import World.Render.Camera;
import Game.Direction;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class Circle extends Shape {
	private static final double CORNER = 0.70710678118654752440084436210485;	//sqrt(2)/2 or cos(<corner>)
	
	public int x, y;	//Centerpoint
	public int r;
	
	public Circle(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}

	@Override
	public void setX(int x) { this.x = x; }
	@Override
	public void setY(int y) { this.y = y; }
	public void setR(int r) { this.r = r; }
	
	@Override
	public int getX(Direction d) {
		switch(d) {
			case NORTH:			return getCenterX();
			case SOUTH:			return getCenterX();
			case WEST:			return getLeftX();
			case EAST:			return getRightX();
			case NORTH_WEST:	return getCenterX() - (int)(getWidth()/2D * CORNER);
			case NORTH_EAST:	return getCenterX() + (int)(getWidth()/2D * CORNER);
			case SOUTH_WEST:	return getCenterX() - (int)(getWidth()/2D * CORNER);
			case SOUTH_EAST:	return getCenterX() + (int)(getWidth()/2D * CORNER);
				
			default:			return getCenterX();
		}
	}
	
	@Override
	public int getY(Direction d) {
		switch(d) {
			case NORTH:			return getTopY();
			case SOUTH:			return getBottomY();
			case WEST:			return getCenterY();
			case EAST:			return getCenterY();
			case NORTH_WEST:	return getCenterY() - (int)(getHeight()/2D * CORNER);
			case NORTH_EAST:	return getCenterY() - (int)(getHeight()/2D * CORNER);
			case SOUTH_WEST:	return getCenterY() + (int)(getHeight()/2D * CORNER);
			case SOUTH_EAST:	return getCenterY() + (int)(getHeight()/2D * CORNER);
				
			default:			return getCenterY();
		}
	}
	
	@Override
	public int getWidth()	{ return r*2;	}
	@Override
	public int getHeight()	{ return r*2;	}
	
	@Override
	public int getLeftX()	{ return x-r;	}
	@Override
	public int getCenterX()	{ return x;		}
	@Override
	public int getRightX()	{ return x+r;	}
	
	@Override
	public int getTopY()	{ return y-r;	}
	@Override
	public int getCenterY()	{ return y;		}
	@Override
	public int getBottomY()	{ return y+r;	}
	
	public int getDistFromEdgeInwards(int x, int y, int w, int h, Direction side) {
		switch (side) {
			case NORTH:
				return y + h - (this.y + this.r);
			case SOUTH:
				return y + (this.y - this.r);
			case WEST:
				return x + (this.x - this.r);
			case EAST:
				return x + w - (this.x + this.r);
		}
		
		return 0;
	}
	
//	public int getDistFromEdgeOutwards(int x, int y, int w, int h, Direction side) {
//		return -getDistFromEdgeInwards(x, y, w, h, side);
//	}
	
	public float getDistToPoint(float x, float y) {
		return (float)Math.sqrt(x*x+y*y);
	}
	
	@Override
    public boolean intersectsRect(Rect b) {
		return b.intersectsCircle(this);
	}

	@Override
	public boolean intersectsCircle(Circle b) {
		return intersectsCircle(b.x, b.y, b.r);
	}
	
    public boolean intersectsCircle(int x, int y, int r) {
		int dx = this.x - x;
		int dy = this.y - y;
		int radii = this.r + r;
		
		return dx * dx + dy * dy <= radii * radii;
    }

	@Override
	public void arrangeToSide(Shape s, Direction d) {
		switch (d) {
			case NORTH:
				x = s.getX(d);
				y = s.getY(d) + this.r;
				break;
			case SOUTH:
				x = s.getX(d);
				y = s.getY(d) - this.r;
				break;
			case WEST:
				x = s.getX(d) - this.r;
				y = s.getY(d);
				break;
			case EAST:
				x = s.getX(d) + this.r;
				y = s.getY(d);
				break;
			case NORTH_WEST:
				x = s.getX(d) - this.r;
				y = s.getY(d) + this.r;
				break;
			case NORTH_EAST:
				x = s.getX(d) + this.r;
				y = s.getY(d) + this.r;
				break;
			case SOUTH_WEST:
				x = s.getX(d) - this.r;
				y = s.getY(d) - this.r;
				break;
			case SOUTH_EAST:
				x = s.getX(d) + this.r;
				y = s.getY(d) - this.r;
				break;
		}
	}

	@Override
	public void drawImg(Graphics g, Image img, boolean scaled, boolean fit) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void draw(Graphics g) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void draw(Graphics g, Camera cam) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}