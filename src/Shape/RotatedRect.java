
/*
 * RotatedRect.java
 *
 * Jun 24, 2016
 */
package Shape;

import Game.Direction;
import World.Render.Camera;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/* 
 * @author Per Eresund
 */
public class RotatedRect extends Rect{
	public static double getRotatedX(int xpos, int ypos, int rotX, int rotY, double rot) {
		double dx1 = rotX - xpos;
		double dy1 = rotY - ypos;
		double len = Math.sqrt(dx1*dx1 + dy1*dy1);
		double rot1 = getAngle(rotX-xpos, rotY-ypos);
		double rot2 = rot - rot1;
		double dx2 = -len * Math.cos(rot2);
		double dx = dx1+dx2;
		double worldX = xpos+dx;
		return worldX;
	}
	public static double getRotatedY(int xpos, int ypos, int rotX, int rotY, double rot) {
		double dx1 = rotX - xpos;
		double dy1 = rotY - ypos;
		double len = Math.sqrt(dx1*dx1 + dy1*dy1);
		double rot1 = getAngle(rotX-xpos, rotY-ypos);
		double rot2 = rot - rot1;
		double dy2 = len * Math.sin(rot2);
		double dy = dy1+dy2;
		double worldY = ypos+dy;
		return worldY;
	}
	
	/*
	 * Makes radians be in the area of 0 to 2*PI.
	 */
	public static double normalizeRadians(double rot) {
		while (true) {
			if (rot < 0) {
				rot += Math.PI*2D;
			}
			else if (rot > Math.PI*2D) {
				rot -= Math.PI*2D;
			} else break;
		}
		return rot;
	}
	
	public double rotation;
	
	public RotatedRect(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	public RotatedRect(int x, int y, int w, int h, boolean centered) {
		super(x, y, w, h, centered);
	}
	
	public double getRotatedX(int rotX, int rotY, double rot, Corner c) {
		return getRotatedX(super.getX(c), super.getY(c), rotX, rotY, rot);
	}
	
	public double getRotatedY(int rotX, int rotY, double rot, Corner c) {
		return getRotatedY(super.getX(c), super.getY(c), rotX, rotY, rot);
	}
	
	public double getRotatedX(int rotX, int rotY, double rot, Direction d) {
		return getRotatedX(super.getX(d), super.getY(d), rotX, rotY, rot);
	}
	
	public double getRotatedY(int rotX, int rotY, double rot, Direction d) {
		return getRotatedY(super.getX(d), super.getY(d), rotX, rotY, rot);
	}
	
	@Override
	public int getX(Corner corner) {
		if (rotation == 0) return super.getX(corner);
		
		return (int)(super.getCenterX() +
					(super.getX(corner) - super.getCenterX()) * Math.cos(rotation) +
					(super.getY(corner) - super.getCenterY()) * Math.sin(-rotation));
	}
	@Override
	public int getY(Corner corner) {
		if (rotation == 0) return super.getY(corner);
		
		return (int)(super.getCenterY() -
					(super.getX(corner) - super.getCenterX()) * Math.sin(-rotation) +
					(super.getY(corner) - super.getCenterY()) * Math.cos(rotation));
	}
	
	@Override
	public int getWidth()	{
		if (rotation == 0) return super.getWidth();
		return getRightX() - getLeftX();
	}
	@Override
	public int getHeight()	{
		if (rotation == 0) return super.getHeight();
		return getBottomY() - getTopY();
	}
	
	public double getCenterX(int rotX, int rotY, double rot) {
		return getRotatedX(getCenterX(), getCenterY(), rotX, rotY, rot);
	}
	
	public double getCenterY(int rotX, int rotY, double rot) {
		return getRotatedY(getCenterX(), getCenterY(), rotX, rotY, rot);
	}
	
	@Override
	public int getLeftX()	{
		if (rotation == 0) return super.getLeftX();
		
		Corner corner0 = Corner.TOP_LEFT;
		Corner corner1 = Corner.TOP_RIGHT;
		Corner corner2 = Corner.BOTTOM_LEFT;
		Corner corner3 = Corner.BOTTOM_RIGHT;
		
		int x0 = getX(corner0);
		int x1 = getX(corner1);
		int x2 = getX(corner2);
		int x3 = getX(corner3);
		
		return Math.min(x0, Math.min(x1, Math.min(x2, x3)));
	}
	
	@Override
	public int getRightX()	{
		if (rotation == 0) return super.getRightX();
		
		Corner corner0 = Corner.TOP_LEFT;
		Corner corner1 = Corner.TOP_RIGHT;
		Corner corner2 = Corner.BOTTOM_LEFT;
		Corner corner3 = Corner.BOTTOM_RIGHT;
		
		int x0 = getX(corner0);
		int x1 = getX(corner1);
		int x2 = getX(corner2);
		int x3 = getX(corner3);
		
		return Math.max(x0, Math.max(x1, Math.max(x2, x3)));
	}
	
	@Override
	public int getTopY()	{
		if (rotation == 0) return super.getTopY();
		
		Corner corner0 = Corner.TOP_LEFT;
		Corner corner1 = Corner.TOP_RIGHT;
		Corner corner2 = Corner.BOTTOM_LEFT;
		Corner corner3 = Corner.BOTTOM_RIGHT;
		
		int y0 = getY(corner0);
		int y1 = getY(corner1);
		int y2 = getY(corner2);
		int y3 = getY(corner3);
		
		return Math.min(y0, Math.min(y1, Math.min(y2, y3)));
	}
	
	@Override
	public int getBottomY()	{
		if (rotation == 0) return super.getBottomY();
		
		Corner corner0 = Corner.TOP_LEFT;
		Corner corner1 = Corner.TOP_RIGHT;
		Corner corner2 = Corner.BOTTOM_LEFT;
		Corner corner3 = Corner.BOTTOM_RIGHT;
		
		int y0 = getY(corner0);
		int y1 = getY(corner1);
		int y2 = getY(corner2);
		int y3 = getY(corner3);
		
		return Math.max(y0, Math.max(y1, Math.max(y2, y3)));
	}
	
	public void setAngleTo(int centerX, int centerY) {
		rotation = getAngleTo(centerX, centerY);
	}
	public void rotate() {
		SGL GL = Renderer.get();
		float rotation = (float)Math.toDegrees(this.rotation);
        if (rotation != 0) {	//Rotate
	        GL.glTranslatef(getCenterX(), getCenterY(), 0.0f); 
	        GL.glRotatef((float)rotation, 0.0f, 0.0f, 1.0f); 
	        GL.glTranslatef(-getCenterX(), -getCenterY(), 0.0f);
        }
	}
	public void unrotate() {
		SGL GL = Renderer.get();
		float rotation = (float)Math.toDegrees(this.rotation);
        if (rotation != 0) {	//Un-rotate
	        GL.glTranslatef(getCenterX(), getCenterY(), 0.0f); 
	        GL.glRotatef(-(float)rotation, 0.0f, 0.0f, 1.0f); 
	        GL.glTranslatef(-getCenterX(), -getCenterY(), 0.0f);
        }
	}
	
	public void rotate(Camera cam) {
		SGL GL = Renderer.get();
		float rotation = (float)Math.toDegrees(this.rotation);
        if (rotation != 0) {	//Rotate
	        GL.glTranslatef(cam.getRenderX(getCenterX()), cam.getRenderY(getCenterY()), 0.0f); 
	        GL.glRotatef((float)rotation, 0.0f, 0.0f, 1.0f); 
	        GL.glTranslatef(-cam.getRenderX(getCenterX()), -cam.getRenderY(getCenterY()), 0.0f);
        }
	}
	public void unrotate(Camera cam) {
		SGL GL = Renderer.get();
		float rotation = (float)Math.toDegrees(this.rotation);
        if (rotation != 0) {	//Un-rotate
	        GL.glTranslatef(cam.getRenderX(getCenterX()), cam.getRenderY(getCenterY()), 0.0f); 
	        GL.glRotatef(-(float)rotation, 0.0f, 0.0f, 1.0f); 
	        GL.glTranslatef(-cam.getRenderX(getCenterX()), -cam.getRenderY(getCenterY()), 0.0f);
        }
	}
	
	
//	private void drawRect(int x, int y, int w, int h) {
//		SGL GL = Renderer.get();
//		GL.glDisable(GL11.GL_TEXTURE_2D);
//		GL.glBegin(GL11.GL_LINE_LOOP);
//		GL.glVertex2f(x, y);
//		GL.glVertex2f(x+w, y);
//		GL.glVertex2f(x+w, y+h);
//		GL.glVertex2f(x, y+h);
////		GL.glVertex2f(x, y);
//		GL.glEnd();
//		GL.glEnable(GL11.GL_TEXTURE_2D);
//	}
	
	@Override
	public void draw(Graphics g, Camera cam) {
//		g.drawRect(cam.getRenderX(getLeftX()), cam.getRenderY(getTopY()), getWidth(), getHeight());
		
//		rotate(cam);
		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), w, h);
//		unrotate(cam);
		
//		int sizeX = 2, sizeY = 2;
//		float x, y, x1, y1, x2, y2;
//		double rotation = -this.rotation;
//		x1 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		y1 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		x2 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		y2 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		g.drawLine(cam.getRenderX(x1), cam.getRenderY(y1), cam.getRenderX(x2), cam.getRenderY(y2));
//		
//		x1 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		y1 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		x2 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		y2 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		g.drawLine(cam.getRenderX(x1), cam.getRenderY(y1), cam.getRenderX(x2), cam.getRenderY(y2));
//		
//		x1 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		y1 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		x2 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		y2 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		g.drawLine(cam.getRenderX(x1), cam.getRenderY(y1), cam.getRenderX(x2), cam.getRenderY(y2));
//		
//		x1 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		y1 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		x2 = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		y2 = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		g.drawLine(cam.getRenderX(x1), cam.getRenderY(y1), cam.getRenderX(x2), cam.getRenderY(y2));
//		
//		x = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		y = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_LEFT);
//		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), sizeX, sizeY);
//		x = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		y = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.BOTTOM_RIGHT);
//		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), sizeX, sizeY);
//		x = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		y = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_LEFT);
//		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), sizeX, sizeY);
//		x = (float)getRotatedX(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		y = (float)getRotatedY(super.getCenterX(), super.getCenterY(), rotation, Corner.TOP_RIGHT);
//		g.drawRect(cam.getRenderX(x), cam.getRenderY(y), sizeX, sizeY);
		rotation -= cam.getRotation();
	}
}