/*
 * Rect.java
 *
 * Dec 14, 2015
 */
package Shape;

import Game.Direction;
import World.Render.Camera;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/*
 * @author Per Eresund
 */
public class Rect extends Shape {
    public static boolean intersectsRect(	int leftX0, int rightX0, int bottomY0, int topY0,
											int leftX1, int rightX1, int bottomY1, int topY1) {
		if (leftX0		>	rightX1)	return false;
		if (rightX0		<	leftX1)		return false;
		if (bottomY0	<	topY1)		return false;
		if (topY0		>	bottomY1)	return false;
        
        return true;
    }
	
	public static int getLeftX(int x, int w)	{ return x; }
	public static int getRightX(int x, int w)	{ return x+w-1; }
	public static int getTopY(int y, int h)		{ return y;	}
	public static int getBottomY(int y, int h)	{ return y+h-1; }
	
	public int x, y, w, h;
	
	public Rect() {
		this(0, 0, 0, 0);
	}
	
	public Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Rect(int x, int y, int w, int h, boolean centered) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if (centered) {
			this.x -= this.w/2;
			this.y -= this.h/2;
		}
	}

	@Override
	public void setX(int x) { this.x = x; }
	@Override
	public void setY(int y) { this.y = y; }
	public void setW(int w) { this.w = w; }
	public void setH(int h) { this.h = h; }
	
	@Override
	public int getX(Direction d) {
		switch(d) {
			case WEST: case NORTH_WEST:	case SOUTH_WEST:
				return getLeftX();
				
			case EAST: case NORTH_EAST:	case SOUTH_EAST:
				return getRightX();
				
			case NORTH:	case SOUTH: default:
				return getCenterX();
				
		}
	}
	
	@Override
	public int getY(Direction d) {
		switch(d) {
			case NORTH: case NORTH_WEST: case NORTH_EAST:
				return getTopY();
				
			case SOUTH: case SOUTH_WEST: case SOUTH_EAST:
				return getBottomY();
				
			case WEST: case EAST: default:
				return getCenterY();
		}
	}
	
	public int getLongestSide() {
		return Math.max(w, h);
	}
	
	@Override
	public int getWidth()	{ return w;		}
	@Override
	public int getHeight()	{ return h;		}
	
	@Override
	public int getLeftX()	{ return x;		}
	@Override
	public int getCenterX()	{ return x+w/2;	}
	@Override
	public int getRightX()	{ return x+w-1;	}
	
	@Override
	public int getTopY()	{ return y;		}
	@Override
	public int getCenterY()	{ return y+h/2;	}
	@Override
	public int getBottomY()	{ return y+h-1;	}
	
	public int getMidpointX(Rect b) {
		return (this.getCenterX() + b.getCenterX()) / 2;
	}
	public int getMidpointY(Rect b) {
		return (this.getCenterY() + b.getCenterY()) / 2;
	}
	
	public int getX(Corner corner) {
		switch(corner) {
			case BOTTOM_LEFT:	return x;
			case BOTTOM_RIGHT:	return x+w-1;
			case TOP_LEFT:		return x;
			case TOP_RIGHT:		return x+w-1;
			
			default:			return getCenterX();
		}
	}
	
	public int getY(Corner corner) {
		switch(corner) {
			case BOTTOM_LEFT:	return y+h-1;
			case BOTTOM_RIGHT:	return y+h-1;
			case TOP_LEFT:		return y;
			case TOP_RIGHT:		return y;
			
			default:			return getCenterY();
		}
	}
	
	/*
	 * Returns the x position of the rect post-scaling.
	 */
	public int getScaledX(Direction d, double scale) {
		switch(d) {
			case WEST: case NORTH_WEST:	case SOUTH_WEST:
				return (int)(getLeftX() + w/2d - (w/2d) / scale);
				
			case EAST: case NORTH_EAST:	case SOUTH_EAST:
				return (int)(getRightX() - w/2d + (w/2d) / scale);
				
			case NORTH:	case SOUTH: default:
				return getCenterX();
		}
	}
	
	/*
	 * Returns the y position of the rect post-scaling.
	 */
	public int getScaledY(Direction d, double scale) {
		switch(d) {
			case NORTH: case NORTH_WEST: case NORTH_EAST:
				return (int)(getTopY() + h/2d - (h/2d) / scale);
				
			case SOUTH: case SOUTH_WEST: case SOUTH_EAST:
				return (int)(getBottomY() - h/2d + (h/2d) / scale);
				
			case WEST: case EAST: default:
				return getCenterY();
		}
		
	}
	
	public int getScaledW(double scale) {
		return (int)(w/scale);
	}
	
	public int getScaledH(double scale) {
		return (int)(h/scale);
	}
	
	public boolean contains(Shape b) {
		if (getLeftX()		>	b.getLeftX())	return false;
		if (getRightX()		<	b.getRightX())	return false;
		if (getBottomY()	<	b.getBottomY())	return false;
		if (getTopY()		>	b.getTopY())	return false;
		
		return true;
	}
	
	@Override
    public boolean intersectsRect(Rect b) {
		if (getLeftX()		>	b.getRightX())	return false;
		if (getRightX()		<	b.getLeftX())	return false;
		if (getBottomY()	<	b.getTopY())	return false;
		if (getTopY()		>	b.getBottomY()) return false;
		
        return true;
//		return intersectsRect(b.x, b.y, b.w, b.h);
    }
	
    public boolean intersectsRectApproximate(int leftX, int rightX, int bottomY, int topY) {
		if (x-w/2+h/2		>	rightX)		return false;
		if (x+w+w/2-h/2		<	leftX)		return false;
		if (y+h+h/2-w/2		<	topY)		return false;
		if (y-h/2+w/2		>	bottomY)	return false;
		
//		if (x-w/2+h/2		>	rightX)		return false;
//		if (x+w+w/2-h/2		<	leftX)		return false;
//		if (y+h+h/2-w/2		<	topY)		return false;
//		if (y-h/2+w/2		>	bottomY)	return false;
        
        return true;
    }
	
    public boolean intersectsRect(int leftX, int rightX, int bottomY, int topY) {
		if (getLeftX()		>	rightX)		return false;
		if (getRightX()		<	leftX)		return false;
		if (getBottomY()	<	topY)		return false;
		if (getTopY()		>	bottomY)	return false;
        
        return true;
    }
	
//    public boolean intersectsRect(int x, int y, int w, int h) {
//		if (getLeftX()		>	x+w)	return false;
//		if (getRightX()		<	x)		return false;
//		if (getBottomY()	<	y)		return false;
//		if (getTopY()		>	y+h)	return false;
//        
//        return true;
//    }
    
    public boolean intersectsPos(int x, int y) {
        if (getLeftX()		>	x) return false;
        if (getRightX()		<	x) return false;
        if (getBottomY()	<	y) return false;
        if (getTopY()		>	y) return false;
        
        return true;
    }
	
	@Override
    public boolean intersectsCircle(Circle c) {
		if (!intersectsRect(c.getLeftX(), c.getTopY(), c.getWidth(), c.getHeight())) return false;
		
        return intersectsCircleAdv(c);
	}
	
    private boolean intersectsCircleAdv(Circle c) {
		double overlapSquared = getCornerOverlapSquared(c);
        return overlapSquared < 1d;
	}
	
	public double getRadius() {
		return Math.sqrt((w/2d)*(w/2d) + (h/2d)*(h/2d));
	}
	
	/**
	 * Calculates the distance between the point and the centerpoint of the
	 * rect.
	 *
	 * @param x The x position of the point.
	 * @param y The y position of the point.
	 *
	 * @return The distance between the point and the centerpoint of the rect.
	 *         Can only be positive or zero.
	 */
	public double getDistanceTo(int x, int y) {
		long dx = Math.abs(getCenterX() - x);
		long dy = Math.abs(getCenterY() - y);
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the distance between the centerpoints of two shapes.
	 *
	 * @param b The body.
	 *
	 * @return The distance between the centerpoint of the shapes. Can only be
	 *         positive or zero.
	 */
	public double getDistanceTo(Shape b) {
		long dx = Math.abs(getCenterX() - b.getCenterX());
		long dy = Math.abs(getCenterY() - b.getCenterY());
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the square of the distance between the centerpoints of two
	 * shapes. Used instead of getDistance() to save performance when only
	 * distance comparisons are necessary.
	 *
	 * @param s The body.
	 *
	 * @return The square of the distance between the centerpoints of the
	 *         shapes. Can only be positive or zero.
	 */
	public double getDistanceSquared(Shape s) {
		long dx = Math.abs(getCenterX() - s.getCenterX());
		long dy = Math.abs(getCenterY() - s.getCenterY());
		return dx*dx + dy*dy;
	}

	/**
	 * Calculates the square of the sum of the radii of the rect and the circle.
	 * Used instead of getRadiusDistance() to save performance when only
	 * distance comparisons are necessary (no need to use square root).
	 *
	 * @param c The circle.
	 *
	 * @return The square of the sum of the radii of the rect and the circle.
	 *		   Can only be positive or zero.
	 */
	public double getRadiusDistanceSquared(Circle c) {
		double rectRadius = getRadius();
		return (rectRadius + c.r)*(rectRadius + c.r);
	}
	
	/**
	 * Creates a big circle from the rect's centre to check if the circle is
	 * outside any of corners of the rect, which will mean there is no overlap.
	 *
	 * @param c The circle
	 *
	 * @return Overlap distance (negative for no overlap).
	 */
	public double getCornerOverlapSquared(Circle c) {
		double distSquare = getDistanceSquared(c);
		double radiusDistSquare = getRadiusDistanceSquared(c);
		return distSquare - radiusDistSquare;
	}
	
	public static double getAngle(double dx, double dy) {
		double angle;
		if (dx == 0) {
			angle = (dy >= 0 ? Math.PI/2D : Math.PI*3D/2D);
		} else {
			angle = Math.atan2(dy, dx);
		}
		return angle;
	}

	
	private static final double[] temp = new double[2];
	public static double[] addVectors(double radian0, double force0, double radian1, double force1) {
		double vx0 = Math.cos(radian0) * force0;
		double vy0 = Math.sin(radian0) * force0;
		double vx1 = Math.cos(radian1) * force1;
		double vy1 = Math.sin(radian1) * force1;
		double finalvx = vx0 + vx1;
		double finalvy = vy0 + vy1;
		double finalRadian = getAngle(finalvx, finalvy);
		double finalForce = Math.sqrt(finalvx*finalvx+finalvy*finalvy);
		temp[0] = finalRadian;
		temp[1] = finalForce;
		return temp;
	}
	
	public double getAngleTo(Rect b) {
		return getAngleTo(b.getCenterX(), b.getCenterY());
	}
	
//	public double getAngleTo(int centerX, int centerY) {
//		double dx = centerX - this.getCenterX();
//		double dy = centerY - this.getCenterY();
//		return getAngle(dx, dy);
//	}
	
	@Override
	public void drawImg(Graphics g, Image img, boolean scaled, boolean fit) {
		if (scaled) {
			float scaleX = (float)w / (float)img.getWidth();
			float scaleY = (float)h / (float)img.getHeight();
			int offsetX = 0, offsetY = 0;
			if (fit) {
				scaleX = Math.min(scaleX, scaleY);
				scaleY = scaleX;
				
				offsetX = (int)((w - scaleX*img.getWidth())/2f);
				offsetY = (int)((h - scaleY*img.getHeight())/2f);
			}
			g.scale(scaleX, scaleY);
//			g.drawImage(img, getScaledX(scaleX), getScaledY(scaleY));
			g.drawImage(img, (x+offsetX)/scaleX, (y+offsetY)/scaleY);
			g.scale(1f/scaleX, 1f/scaleY);
		} else {
			g.drawImage(img, x, y);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawRect(x, y, w-1, h-1);
	}
	
	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawRect(x, y, w, h);
	}
	
	@Override
	public void draw(Graphics g, Camera cam) {
		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), w, h);
	}
	
	public void fill(Graphics g, Camera cam) {
		g.fillRect(cam.getRenderX(x), cam.getRenderY(y), w, h);
	}
	
	public void fill(Graphics g) {
		g.fillRect(x, y, w, h);
	}
	
	public void fill(Graphics g, int x, int y) {
		g.fillRect(x, y, w, h);
	}
	@Override
	public void arrangeToSide(Shape s, Direction d) {
		switch (d) {
			case NORTH:
				x = s.getX(d);
				y = s.getY(d) - this.h;
				break;
			case SOUTH:
				x = s.getX(d);
				y = s.getY(d);
				break;
			case WEST:
				x = s.getX(d) - this.w;
				y = s.getY(d);
				break;
			case EAST:
				x = s.getX(d);
				y = s.getY(d);
				break;
				
			case NORTH_WEST:
				x = s.getX(d) - this.w;
				y = s.getY(d) - this.h;
				break;
			case NORTH_EAST:
				x = s.getX(d);
				y = s.getY(d) - this.h;
				break;
			case SOUTH_WEST:
				x = s.getX(d) - this.w;
				y = s.getY(d);
				break;
			case SOUTH_EAST:
				x = s.getX(d);
				y = s.getY(d);
				break;
		}
	}
}