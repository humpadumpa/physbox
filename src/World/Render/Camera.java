/*
 * Camera.java
 *
 * Dec 14, 2015
 */
package World.Render;

import Game.Direction;
import Shape.Rect;
import Shape.RotatedRect;
import Shape.Shape;
import World.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class Camera {
	protected static final double ZOOM_SPEED = 1.05;
	
	protected final RotatedRect rect;		//Position of camera in the world and size of camera on the screen
	protected final RotatedRect screenRect;	//Position and size of camera on the screen 
	protected double xbuf, ybuf;			//Position buffers
	protected double scale;					//Scaling applied to rendering
	protected double rotation;				//Rotation applied to rendering
	
	/*
	 * If the camera uses approximate calculations for intersections. Everything
	 * that should intersect, will be calculated as such, but in some cases,
	 * things outside of the camera rect can still be intersected. Can speed
	 * things up when doing calculations if the camera is rotated.
	 */
	public boolean approximate = false;
	
	//Scaled temp positions.
	protected int scaledXWest, scaledXEast, scaledYSouth, scaledYNorth;
	
	public Camera(int w, int h, int screenX, int screenY) {
		rect = new RotatedRect(0, 0, w, h, false);
		screenRect = new RotatedRect(screenX, screenY, w, h);
		this.scale = 1D;
		this.rotation = 0D;
		setScaledTemps();
	}
	
	protected void setScaledTemps() {
		scaledXWest = rect.getScaledX(Direction.WEST, scale);
		scaledXEast = rect.getScaledX(Direction.EAST, scale);
		scaledYSouth = rect.getScaledY(Direction.SOUTH, scale);
		scaledYNorth = rect.getScaledY(Direction.NORTH, scale);
	}
	
	public void zoomIn() {
		setScale(scale * ZOOM_SPEED);
	}
	
	public void zoomOut() {
		setScale(scale / ZOOM_SPEED);
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		setScaledTemps();
	}
	
	public float getScale() {
		return (float)scale;
	}
	
	public void setRotation(double rotation) {
		rotation = RotatedRect.normalizeRadians(rotation);
		
		this.rotation = rotation;
		rect.rotation = -rotation;
		setScaledTemps();
	}
	
	public void rotate() {
		screenRect.rotation = rotation;
		screenRect.rotate();
		screenRect.rotation = 0;
	}
	
	public void unrotate() {
		rect.rotation = -rotation;
		screenRect.rotation = rotation;
		screenRect.unrotate();
		screenRect.rotation = 0;
	}
	
	public void scale(Graphics g) {
		g.scale((float)scale, (float)scale);
	}
	
	public void unscale(Graphics g) {
		g.scale(1f/(float)scale, 1f/(float)scale);
	}
	
	public void scaleAfter(Shape s, float minimum) {
		scaleAfter(s.getWidth(), s.getHeight(), minimum);
	}
	
	public void scaleAfter(int w, int h, float minimum) {
		float xScale, yScale;
		xScale = (float)rect.w / (float)(w) / 1.1f;
		yScale = (float)rect.h / (float)(h) / 1.1f;

		setScale(Math.min(minimum, Math.min(xScale, yScale)));
	}
	
	public void scaleAfter(int x0, int y0, int x1, int y1, float minimum) {
		int w = Math.abs(x1-x0);
		int h = Math.abs(y1-y0);
		scaleAfter(w, h, minimum);
	}
	
	public boolean intersects(Rect b) {
//		if (rotation == 0D) {
//			if (scale >= 1) {	//Possible optimization.
//				return b.intersectsRect(rect.x,
//										rect.y,
//										rect.w,
//										rect.h);
//			}
//			return b.intersectsRect((int)(getScaledX()),
//									(int)(getScaledY()),
//									(int)(rect.w / scale),
//									(int)(rect.h / scale));
//		}
		
//		if (scale >= 1) {	//Possible optimization.
//			return b.intersectsRect(rect.getLeftX(),
//									rect.getRightX(),
//									rect.getBottomY(),
//									rect.getTopY());
//		}
		if (approximate) {
			return b.intersectsRectApproximate(	scaledXWest,
												scaledXEast,
												scaledYSouth,
												scaledYNorth);
		} else {
			return b.intersectsRect(scaledXWest,
									scaledXEast,
									scaledYSouth,
									scaledYNorth);
		}
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public RotatedRect getRect() {
		return rect;
	}
	
	public RotatedRect getScreenRect() {
		return screenRect;
	}
	
	/*
	 * Returns the x position of the camera post-scaling.
	 */
	public int getScaledX() {
		return rect.getScaledX(Direction.NORTH_WEST, scale);
	}
	
	/*
	 * Returns the y position of the camera post-scaling.
	 */
	public int getScaledY() {
		return rect.getScaledY(Direction.NORTH_WEST, scale);
	}
	public int getScaledW() {
		return rect.getScaledW(scale);
	}
	public int getScaledH() {
		return rect.getScaledH(scale);
	}
	public int getScaledLength(int length) {
		return (int)(length/scale);
	}
	
	public void setClip(Graphics g) {
		g.setClip(screenRect.x, screenRect.y, screenRect.w, screenRect.h);
	}
	
	public void followPos(int x, int y, boolean centered) {
		int dx = x - rect.x, dy = y - rect.y;
		if (centered) {
			dx -= rect.w/2;
			dy -= rect.h/2;
		}
		if (Math.abs(dx) < 8 && Math.abs(dy) < 8) {
//			System.out.println("no");
			setPos(x, y, centered);
			return;
		}
		double speedX = dx / 50D + 2D*Math.signum(dx);
		double speedY = dy / 50D + 2D*Math.signum(dy);
		xbuf += speedX;
		ybuf += speedY;
		int newx = (centered ? rect.getCenterX() : rect.x) + (int)xbuf, newy = (centered ? rect.getCenterY() : rect.y) + (int)ybuf;
		setPos(newx, newy, centered);
		xbuf %= 1;
		ybuf %= 1;
	}
	
	public void setPos(int x, int y, boolean centered) {
		rect.x = x;
		rect.y = y;
		if (centered) {
			rect.x -= rect.w/2;
			rect.y -= rect.h/2;
		}
		setScaledTemps();
	}
	
	public void setPos(Rect b, boolean centered) {
		if (centered) {
			rect.x = b.getCenterX() - rect.w/2;
			rect.y = b.getCenterY() - rect.h/2;
		} else {
			rect.x = b.x;
			rect.y = b.y;
		}
		setScaledTemps();
	}
	
	public void setSize(int w, int h) {
		rect.w = w;
		rect.h = h;
		setScaledTemps();
	}
	
	public void renderEntity(Graphics g, Entity e) {
		e.render(g, getRenderX(e.rect.x), getRenderY(e.rect.y));
	}
	
	public void renderImage(Graphics g, Image img, Rect b) {
		g.drawImage(img, getRenderX(b.x), getRenderY(b.y));
	}
	
	public void renderImage(Graphics g, Image img, int x, int y, boolean centered) {
		if (centered) {
			x -= img.getWidth() / 2;
			y -= img.getHeight() / 2;
		}
		
		g.drawImage(img, x, y);
	}
	
//	public void renderImageResized(Graphics g, Image img, Rect b, int w, int h) {
//		g.drawImage(img, getRenderX(b.x), getRenderY(b.y));
//	}
	
	public int getInterfaceX(int origX) {
		return origX + screenRect.x;
	}
	
	public int getInterfaceY(int origY) {
		return origY + screenRect.y;
	}
	
	/*
	 * Translates world coordinates to screen coordinates.
	 */
	public int getRenderX(int worldX)		{ return (int)getRenderX((double)worldX); }
	public float getRenderX(float worldX)	{ return (float)getRenderX((double)worldX); }
	public double getRenderX(double worldX) {
		return worldX - (rect.x + rect.w/2d - (rect.w/2d + screenRect.x) / scale);
	}
	
	/*
	 * Translates world coordinates to screen coordinates.
	 */
	public int getRenderY(int worldY)		{ return (int)getRenderY((double)worldY); }
	public float getRenderY(float worldY)	{ return (float)getRenderY((double)worldY); }
	public double getRenderY(double worldY) {
		return worldY - (rect.y + rect.h/2d - (rect.h/2d + screenRect.y) / scale);
	}
	
	/*
	 * Translates screen coordinates to world coordinates.
	 */
	public int getWorldX(int screenX) {
		return (int)(getScaledX() + screenX / scale);
	}
	
	/*
	 * Translates screen coordinates to world coordinates.
	 */
	public int getWorldY(int screenY) {
		return (int)(getScaledY() + screenY / scale);
	}
	
	public void drawRect(Graphics g, int x, int y, int w, int h) {
//		w -= 1;
//		h -= 1;
		g.drawRect(getRenderX(x), getRenderY(y), w, h);
	}
	
	public void drawRect(Graphics g) {
		rect.w -= 1;
		rect.h -= 1;
		rect.draw(g, this);
		rect.h += 1;
		rect.w += 1;
	}
	
	public void fillRect(Graphics g, int x, int y, int w, int h) {
//		w--;
//		h--;
		Renderer.fillRect(getRenderX(x), getRenderY(y), w, h);
	}
}