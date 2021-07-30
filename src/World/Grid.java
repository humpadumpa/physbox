
/*
 * Grid.java
 *
 * Jan 29, 2017
 */
package World;

import Game.Direction;
import Shape.Rect;
import Phys.MovingEntity;
import World.Render.Camera;
import World.Render.Renderer;
import java.util.ArrayList;
import java.util.Arrays;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/* 
 * @author Per Eresund
 */
public class Grid<E extends Entity> {
	protected final int mapW, mapH;		//Map size in pixels
	protected final int gridW, gridH;	//Grid size in tiles
	protected final int tileW, tileH;	//Tile size in pixels
	
	protected Tile<E>[][] map;

	public Grid(int mapW, int mapH, int gridW, int gridH, int tileW, int tileH) {
		this.mapW = mapW;
		this.mapH = mapH;
		this.gridW = gridW;
		this.gridH = gridH;
		this.tileW = tileW;
		this.tileH = tileH;
		initGrid();
		System.gc();
	}
	
	protected void initGrid() {
		System.out.println("Initializing grid ...");
		this.map = new Tile[gridW][gridH];
		long t0 = System.currentTimeMillis();
		int seconds = 0;
		for (int x = 0; x < gridW; x++){
			for (int y = 0; y < gridH; y++) {
				map[x][y] = new Tile();
				long t = System.currentTimeMillis() - t0;
				if (t > seconds*1000) {
					seconds = (int)(t/1000)+1;
					System.out.println(((double)x/gridW*100D + (double)y/gridH*100D/gridW) + "%");
				}
			}
		}
		System.out.println("Done initializing grid ...");
	}
	
	public void add(E elem) {
		Rect rect = elem.rect;
		int left	= rect.getLeftX()/tileW,	right	= (rect.getRightX())/tileW;
		int top		= rect.getTopY()/tileH,		bottom	= (rect.getBottomY())/tileH;
		
		for (int tileX = left; tileX <= right; tileX++) {
			for (int tileY = top; tileY <= bottom; tileY++) {
				map[tileX][tileY].add(elem);
			}
		}
	}
	
	public void remove(E elem) {
		Rect rect = elem.rect;
		int left	= rect.getLeftX()/tileW,	right	= (rect.getRightX())/tileW;
		int top		= rect.getTopY()/tileH,		bottom	= (rect.getBottomY())/tileH;
		
		for (int tileX = left; tileX <= right; tileX++) {
			for (int tileY = top; tileY <= bottom; tileY++) {
				map[tileX][tileY].remove(elem);
			}
		}
	}
	
	public void move(E elem, int srcx, int srcy, int srcw, int srch) {
		Rect dest = elem.rect;

		int left0	= Rect.getLeftX(srcx, srcw)/tileW,	right0	= (Rect.getRightX(srcx, srcw))/tileW;
		int top0	= Rect.getTopY(srcy, srch)/tileH,	bottom0	= (Rect.getBottomY(srcy, srch))/tileH;
		
		int left1	= dest.getLeftX()/tileW,			right1	= (dest.getRightX())/tileW;
		int top1	= dest.getTopY()/tileH,				bottom1	= (dest.getBottomY())/tileH;
		
		for (int tileX = left0; tileX <= right0; tileX++) {
			for (int tileY = top0; tileY <= bottom0; tileY++) {
				/* All of the destination tiles can be skipped. */
				if (tileX < left1 || tileX > right1 || tileY < top1  || tileY > bottom1) {
					map[tileX][tileY].remove(elem);
				}
			}
		}
		
		for (int tileX = left1; tileX <= right1; tileX++) {
			for (int tileY = top1; tileY <= bottom1; tileY++) {
				/* All of the source tiles can be skipped. */
				if (tileX < left0 || tileX > right0 || tileY < top0 || tileY > bottom0) {
					map[tileX][tileY].add(elem);
				}
			}
		}
	}
	
	public boolean canBePlaced(E elem)			{ return canBePlaced(elem.rect); }
	public boolean canBePlaced(Rect rect)		{ return canBePlaced(rect.x, rect.y, rect.w, rect.h); }
	public boolean canBePlaced(int x, int y, int w, int h) {
		if (!isWithinBounds(x, y, w, h)) return false;
		if (getCollision(null, x, y, w, h) != null) return false;
		return true;
	}
	
	public boolean isWithinBounds(E elem)		{ return isWithinBounds(elem.rect); }
	public boolean isWithinBounds(Rect rect)	{ return isWithinBounds(rect.x, rect.y, rect.w, rect.h); }
	public boolean isWithinBounds(int x, int y, int w, int h) {
		if (Rect.getLeftX(x, w)		<	Rect.getLeftX(0, mapW))		return false;
		if (Rect.getTopY(y, h)		<	Rect.getTopY(0, mapH))		return false;
		if (Rect.getRightX(x, w)	>	Rect.getRightX(0, mapW))	return false;
		if (Rect.getBottomY(y, h)	>	Rect.getBottomY(0, mapH))	return false;
		return true;
	}
	
	public E getCollision(E elem)				{ return getCollision(elem, elem.rect); }
	public E getCollision(E elem, Rect rect)	{ return getCollision(elem, rect.x, rect.y, rect.w, rect.h); }
	public E getCollision(E elem, int x, int y, int w, int h) {
		int left	= Rect.getLeftX(x, w)/tileW,	right	= (Rect.getRightX(x, w))/tileW;
		int top		= Rect.getTopY(y, h)/tileH,		bottom	= (Rect.getBottomY(y, h))/tileH;
		
		for (int tileX = left; tileX <= right; tileX++) {
			for (int tileY = top; tileY <= bottom; tileY++) {
				E coll = map[tileX][tileY].getCollision(elem, x, y, w, h);
				if (coll != null) return coll;
			}
		}
		return null;
	}
	
	public E[] getNearby(Entity e)				{ return getNearby(e.rect); }
	public E[] getNearby(Rect rect)				{ return getNearby(rect.x, rect.y, rect.w, rect.h); }
	public E[] getNearby(int x, int y, int w, int h) {
		int left	= Math.max(0, x/tileW),	right	= Math.min(Rect.getRightX(x, w)/tileW, gridW-1);
		int top		= Math.max(0, y/tileH),	bottom	= Math.min(Rect.getBottomY(y, h)/tileH, gridH-1);
		
		E[] elements = (E[])new Entity[0];
		for (int tileX = left; tileX <= right; tileX++) {
			for (int tileY = top; tileY <= bottom; tileY++) {
				E[] src = map[tileX][tileY].getElements();
//				if (src.length == 0) continue;
				E[] dest = (E[])new Entity[elements.length + src.length];
				System.arraycopy(elements, 0, dest, 0, elements.length);
				System.arraycopy(src, 0, dest, elements.length, src.length);
				elements = dest;
			}
		}
		return elements;
	}
	
	public E[] getElements() {
		E[] elements = (E[])new Entity[0];
		for (int tileX = 0; tileX < gridW; tileX++) {
			for (int tileY = 0; tileY < gridH; tileY++) {
				E[] src = map[tileX][tileY].getElements();
				E[] dest = (E[])new Entity[elements.length + src.length];
				System.arraycopy(elements, 0, dest, 0, elements.length);
				System.arraycopy(src, 0, dest, elements.length, src.length);
				elements = dest;
			}
		}
		return elements;
	}
	
	public void renderTiles(Graphics g, Camera cam) {
//		cam.fillRect(g, 0, 0, mapW, mapH);
		Renderer.end();
		g.setColor(Color.white);
		cam.drawRect(g, 0-1, 0-1, mapW+1, mapH+1);
		Renderer.startRects();
		
//		int x = cam.getScaledX(), y = cam.getScaledY(), w = cam.getScaledW(), h = cam.getScaledH();
//		int left	= Math.max(0, x/tileW),	right	= Math.min(Rect.getRightX(x, w)/tileW, gridW-1);
//		int top		= Math.max(0, y/tileH),	bottom	= Math.min(Rect.getBottomY(y, h)/tileH, gridH-1);
		
//		for (int tileX = left; tileX <= right; tileX++) {
//			for (int tileY = top; tileY <= bottom; tileY++) {
////				boolean found = false;
////				
////				E[] elements = map[tileX][tileY].getElements();
////				for (E e : elements) {
////					if (e instanceof MovingEntity) {
////						found = true;
////						break;
////					}
////				}
////				
////				if (found) {
////					g.setColor(Color.white);
////				}
//				x = tileX*tileW;
//				y = tileY*tileH;
//				w = (int)(tileW*0.9);
//				if (x+w > mapW) w = mapW-x;
//				h = (int)(tileH*0.9);
//				if (y+h > mapH) h = mapH-y;
//				cam.fillRect(g, x, y, w, h);
////				if (found) {
////					g.setColor(Color.blue);
////				}
//			}
//		}
	}
}