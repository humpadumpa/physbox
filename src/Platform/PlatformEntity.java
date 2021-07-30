
/*
 * MovingEntity.java
 *
 * Feb 1, 2017
 */
package Platform;

import Game.Lock;
import Shape.Rect;
import World.Entity;
import World.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public abstract class PlatformEntity extends Entity {
	public PlatformEntity(PlatformWorld world, Color color, int w, int h, boolean passable) {
		super(world, color, w, h, passable);
	}
	public PlatformEntity(PlatformWorld world, Image img, int w, int h, boolean passable) {
		super(world, img, w, h, passable);
	}
	
	@Override
	protected PlatformWorld world()					{ return (PlatformWorld)super.world(); }
	
	@Override
	public boolean collidesWith(Entity other)		{ return collidesWith(other.rect); }
	@Override
	public boolean collidesWith(Rect other)			{ return collidesWith(rect.x, rect.y, rect.w, rect.h); }
	@Override
	public boolean collidesWith(int x, int y, int w, int h) {
		return (passable) ? false : rect.intersectsRect(x, x+w-1, y+h-1, y);
	}

	@Override
	public void collideWith(Entity e) {}
	
	public double getVx() { return 0; }
	public double getVy() { return 0; }
	
	@Override
	public void setPos(int x, int y, boolean centered) {
		if (centered) {
			x -= rect.w/2;
			y -= rect.h/2;
		}
		rect.x = x;
		rect.y = y;
	}
	
	@Override
	public boolean place() {
		if (world().canBePlaced(this)) {
			world().place(this);
			return true;
		} else return false;
	}
	
//	@Override
//	public void render(Graphics g, int x, int y) {
//		if (img != null) g.drawImage(img, x, y);
//		else {
//			g.setColor(color);
//			rect.fill(g, x, y);
//		}
//	}
//	
//	@Override
//	public void render(Graphics g) {
//		render(g, rect.x, rect.y);
//	}
}