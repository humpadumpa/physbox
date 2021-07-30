
/*
 * MovingEntity.java
 *
 * Feb 1, 2017
 */
package Platform;

import Agent.Controllable;
import Game.Direction;
import Game.Lock;
import Shape.Shape;
import ThreadPool.Task;
import ThreadPool.ThreadPool;
import World.Entity;
import World.Render.Renderer;
import World.World;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class MovingEntity extends PlatformEntity implements Controllable {
//	private static double calcVY(double gy, double f, double dt, double vy)	{ return (gy*dt + vy - f*vy*dt); }
//	private static double calcVX(double gx, double f, double dt, double vx)	{ return (gx*dt + vx - f*vx*dt); }
	private static double calcV(double g, double f, double dt, double v)	{ return (g*dt + v - f*v*dt); }
	
	private final Lock lock = new Lock();
	private final Lock moveLock = new Lock();
	private double vx, vy, xbuf, ybuf;
	private double addvx, addvy;
	private int ID;

	public MovingEntity(PlatformWorld world, Color color, int w, int h, boolean passable, int ID) {
		super(world, color, w, h, passable);
		vx = 0;
		vy = 0;
		if (color == Color.red) System.out.println("red id: " + ID);
		this.ID = ID;
	}
	public MovingEntity(PlatformWorld world, Image img, int w, int h, boolean passable) {
		super(world, img, w, h, passable);
		vx = 0;
		vy = 0;
	}

	@Override
	public Shape getShape() {
		return rect;
	}

	@Override
	public World getWorld() {
		return world();
	}
	
	@Override
	public double getVx() { return vx; }
	@Override
	public double getVy() { return vy; }
	
	

	@Override
	public void move(Direction d, double dt) {
		int increment = 100;
		boolean add = false;
		synchronized(moveLock) {
			switch(d) {
				case NORTH:
					addvy += -increment * (add ? 1 : dt);
					break;
				case WEST:
					addvx += -increment * (add ? 1 : dt);
					break;
				case SOUTH:
					addvy += increment * (add ? 1 : dt);
					break;
				case EAST:
					addvx += increment * (add ? 1 : dt);
					break;
			}
		}
	}
	
//	private static MovingEntity root = null;
	
	public void updateMovement(double gx, double gy, double f, double dt) {
		synchronized(moveLock) {
			vx += addvx;
			vy += addvy;
			addvx = 0;
			addvy = 0;
		}
		
		synchronized(lock) {
			vx = calcV(gx, f, dt, vx);
			vy = calcV(gy, f, dt, vy);
			setXbuf(xbuf, vx, dt);
			setYbuf(ybuf, vy, dt);
			updatePos();
		}
	}
	
	private void setXbuf(double add, double vx, double dt) {
		xbuf = add + vx*dt;
	}
	private void setYbuf(double add, double vy, double dt) {
		ybuf = add + vy*dt;
	}
	
	
	private void setIncrements() {
		double ratio;
		int maxX = rect.w/2;
		int maxY = rect.h/2;
		
		incrementX = (int)(xbuf);
		incrementY = (int)(ybuf);
		if (incrementX == 0) {
			incrementY = (int) (ybuf > 0
					? Math.min(maxY, ybuf)
					: Math.max(-maxY, ybuf));
		} else if (incrementY == 0) {
			incrementX = (int) (xbuf > 0
					? Math.min(maxX, xbuf)
					: Math.max(-maxX, xbuf));
		} else {
			ratio = xbuf/ybuf;
			incrementX = (int) (xbuf > 0
					? Math.min(maxX, xbuf)
					: Math.max(-maxX, xbuf));
			if (Math.abs(incrementX / ratio) > maxY) {
				incrementY = (int) (ybuf > 0
						? Math.min(maxY, ybuf)
						: Math.max(-maxY, ybuf));
				incrementX = (int)(incrementY * ratio);
			} else {
				incrementY = (int)(incrementX / ratio);
			}
		}
	}
	
	private int incrementX, incrementY;
	private void updatePos() {
		boolean collided = false, moved = false;
		int srcx = rect.x, srcy = rect.y;
		int newx, prevx;
		int newy, prevy;
		
		setIncrements();
		
		prevx = rect.x;
		newx = prevx + incrementX;
		prevy = rect.y;
		newy = prevy + incrementY;
		
		PlatformEntity e = null, laste = null;
		
		while (Math.abs(xbuf) >= 1 || Math.abs(ybuf) >= 1) {
			if (!world().isWithinBounds(newx, newy, rect.w, rect.h)) {
				return;
			}
			
			e = world().getCollision(this, newx, rect.y, rect.w, rect.h);
			if (e == null) {
				
				xbuf -= (newx - prevx);
				ybuf -= (newy - prevy);
				rect.x = prevx = newx;
				rect.y = prevy = newy;
				world().move(this, srcx, srcy, rect.w, rect.h);
				srcx = rect.x;
				srcy = rect.y;
				
				if (collided) {
					//setbufs
					
					if (laste instanceof Platform) {
						
					} else if (laste instanceof MovingEntity) {
						
					}
				}
				setIncrements();
				newx = rect.x + incrementX;
				newy = rect.y + incrementY;
				collided = false;
				moved = true;
			} else {
				laste = e;
				if (incrementX == 0) {
					if (ybuf > 0) {
						newy = e.rect.getTopY()-rect.h;
					} else {
						newy = e.rect.getBottomY()+1;
					}
				} else if (incrementY == 0) {
					if (xbuf > 0) {
						newx = e.rect.getLeftX()-rect.w;
					} else {
						newx = e.rect.getRightX()+1;
					}
				} else {
					
				}
				
				collided = true;
				continue;
			}
		}
		if (collided) {
			xbuf = 0;
		}
		if (moved) {
			world().move(this, srcx, srcy, rect.w, rect.h);
		}
	}
	
	Color spare = null;
	private static Color temp = new Color(0, 0, 0);
	@Override
	public void render(Graphics g, int x, int y) {
		spare = null;
		
		if (spare == null) {
			spare = color;
			temp.a = color.a;
			temp.r = (float)Math.log10(color.r*(Math.abs(vx)+(Math.abs(vy)))/10d);
			temp.g = (float)Math.log10(color.g*(Math.abs(vx)+(Math.abs(vy)))/10d);
			temp.b = (float)Math.log10(color.b*(Math.abs(vx)+(Math.abs(vy)))/10d);
			color = temp;
		}
		super.render(g, x, y);

		if (spare != null) {
			color = spare;
		}
		
//		if (color == (Color.red)) {
			Renderer.end();
			
//			g.drawString("xbuf: " + (xbuf + vx) + "\nybuf: " + (ybuf + vy) + "\nvx: " + (int)vx + "\nvy: " + (int)vy, x, y);
			
			DecimalFormat df = new DecimalFormat("0.00");
			g.setColor(Color.black);
			String xbufs = df.format((xbuf + vx));
			String ybufs = df.format((ybuf + vy));
			String vxs	 = df.format(vx);
			String vys	 = df.format(vy);
			
			g.drawString(xbufs + "\n" +
						 ybufs + "\n" +
						 vxs + "\n" + 
						 vys, x, y);
			
			Renderer.startRects();
//		}
	}
	
	
	private MovementTask movementTask;
	private double movementdt;
	public void runMovement(ThreadPool tp, double gx, double gy, double f, double dt) {
		movementdt+=dt;
		if (movementTask == null || movementTask.isDone()) {
			movementTask = new MovementTask(0, gx, gy, f, movementdt);
			tp.newTask(movementTask);
			movementdt = 0;
		}
	}

	
	/*------------------------------------------------------------------------*/
	protected class MovementTask extends Task {
		protected final double gx, gy, f, dt;
		
		protected MovementTask(int priority, double gx, double gy, double f, double dt) {
			super(priority);
			this.gx = gx;
			this.gy = gy;
			this.f = f;
			this.dt = dt;
		}

		@Override
		public void start() {
			MovingEntity.this.updateMovement(gx, gy, f, dt);
		}
	}
}