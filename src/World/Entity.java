
/*
 * Entity.java
 *
 * Jan 29, 2017
 */
package World;

import Shape.Rect;
import World.Render.Renderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public abstract class Entity {
	public final Rect rect;
	protected final Image img;
	protected Color color;
	private final World world;
	protected boolean passable;
	
	public Entity(World world, Color color, int w, int h, boolean passable) {
		this.world = world;
		this.img = null;
		this.color = color;
		this.rect = new Rect(-1, -1, w, h);
		this.passable = passable;
	}
	
	public Entity(World world, Image img, int w, int h, boolean passable) {
		this.world = world;
		this.img = img;
		this.color = null;
		this.rect = new Rect(-1, -1, w, h);
		this.passable = passable;
	}
	
	protected World world() { return world; }
	
	public boolean			collidesWith(Entity other)	{ return collidesWith(other.rect); }
	public boolean			collidesWith(Rect other)	{ return collidesWith(rect.x, rect.y, rect.w, rect.h); }
	public abstract boolean	collidesWith(int x, int y, int w, int h);
	
	public abstract void collideWith(Entity e);
	
	public abstract void setPos(int x, int y, boolean centered);
	public abstract boolean place();
//	public void render(Graphics g, int x, int y);
//	public void render(Graphics g);
	
	public void render(Graphics g, int x, int y) {
		if (img != null) g.drawImage(img, x, y);
		else {
			Renderer.fillRect(x, y, rect.w, rect.h, color);
		}
	}
	
	public void render(Graphics g) {
		render(g, rect.x, rect.y);
	}
}