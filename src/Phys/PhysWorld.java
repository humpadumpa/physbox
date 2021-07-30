
/*
 * PlatformWorld.java
 *
 * Feb 1, 2017
 */
package Phys;

import ThreadPool.Task;
import ThreadPool.ThreadPool;
import World.Entity;
import World.Grid;
import World.Render.Camera;
import World.Render.Renderer;
import World.World;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/* 
 * @author Per Eresund
 */
public class PhysWorld<E extends PhysEntity> extends World {
	protected double gravity, friction;
	private ArrayList<MovingEntity> movingEntities;
	
	{
		this.gravTaskdt = 0;
	}
	
	public PhysWorld(int width, int height, double gravity, double friction) {
		super(width, height);
		this.gravity = gravity;
		this.friction = friction;
		movingEntities = new ArrayList();
	}
	
	@Override
	protected Grid<PhysEntity> grid() { return (Grid<PhysEntity>)super.grid(); };
	
	public Entity[] getAll() {
		return grid().getElements();
	}
	
	public ArrayList<MovingEntity> getMoving() {
		return movingEntities;
	}
	
	public boolean canBePlaced(E e) {
		return super.canBePlaced(e);
	}

	public boolean isWithinBounds(E e) {
		return super.isWithinBounds(e);
	}
	
	public E getCollision(E e, int x, int y, int w, int h) {
		return (E)super.getCollision(e, x, y, w, h);
	}
	
	public void move(E e, int srcX, int srcY, int srcW, int srcH) {
		super.move(e, srcX, srcY, srcW, srcH);
	}
	
	public void place(E e) {
		super.place(e);
		if (e instanceof MovingEntity) {
			movingEntities.add((MovingEntity)e);
		}
	}
	
	public void remove(E e) {
		super.remove(e);
		if (e instanceof MovingEntity) {
			movingEntities.remove(e);
		}
	}
		
		
	@Override
	public void update(ThreadPool tp, double dt) {
	//		super.update(tp, dt);
			runGravity(tp, dt);
	}

	@Override
	public void render(Graphics g, Camera cam) {
		super.render(g, cam);
		
		int x, y, w, h;
		double r1, r2;
		w = 500;
		h = 500;
		r1 = Math.sqrt(w*w/4d + h*h/4d);
		r2 = r1 + Math.sqrt(width*width/4d + height*height/4d);
		
		x = width/2-w/2;
		y = height/2-h/2;
		
		x = (int)(x + r2*Math.cos(n));
		y = (int)(y + r2*Math.sin(n));
		
//		x = cam.getRenderX(x);
//		y = cam.getRenderY(y);
		
		g.setColor(Color.white);
		Renderer.startRects();
		cam.fillRect(g, x, y, w, h);
		Renderer.end();
		
		cam.unscale(g);
		g.setColor(Color.yellow);
		String str = "Degrees: ";
		if (period == 0) {
			str+=n;
		} else {
			str+=dp;
		}
		g.drawString(str, 10f, 70f);
		cam.scale(g);
	}
	
	
//	private final boolean 
	private RunGravity gravTask;
	
	private final double roundTime = 25, period = 0;
	private double gravTaskdt = 0, n = Math.PI/2D, dp = 0;
	public void runGravity(ThreadPool tp, double dt) {
		gravTaskdt+=dt;
		if (gravTask == null || gravTask.isDone()) {
			gravTask = new RunGravity(0, gravTaskdt, tp);
			tp.newTask(gravTask);
//			gravTask.start();
			gravTaskdt = 0;
//			gravTask = null;
		}
	}

	
	/*------------------------------------------------------------------------*/
	protected class RunGravity extends Task {
		protected final double dt;
		protected final ThreadPool tp;
		
		protected RunGravity(int priority, double dt, ThreadPool tp) {
			super(priority);
			this.dt = dt;
			this.tp = tp;
		}

		@Override
		public void start() {
//			synchronized(gridLock) {
				double gx = 0, gy = 0, f = 0;
				
				if (period != 0) {
					dp += dt;
					if (dp >= period) {
	//					double d = Math.random()*Math.PI*2D;
						gx = gravity * Math.cos(n);
						gy = gravity * Math.sin(n);
						dp -= period;
					}
				} else {
					gx = gravity * Math.cos(n);
					gy = gravity * Math.sin(n);
				}
				f = friction;
				
				int x, y, w, h;
				double r1, r2;
				w = 500;
				h = 500;
				r1 = Math.sqrt(w*w/4d + h*h/4d);
				r2 = r1 + Math.sqrt(width*width/4d + height*height/4d);

				x = width/2-w/2;
				y = height/2-h/2;

				x = (int)(x + r2*Math.cos(n));
				y = (int)(y + r2*Math.sin(n));
				
				ArrayList<MovingEntity> entities = getMoving();
//				MovingEntity[] entities = null;
//				synchronized(PlatformWorld.super.gridLock) {
//					entities = new MovingEntity[entitiesOnScreen.length];
//					System.arraycopy(PlatformWorld.super.entitiesOnScreen, 0, entities, 0, entitiesOnScreen.length);
//				}
				double angle;
				for (MovingEntity e : entities) {
//					if (e == null) continue;
//					e.runMovement(tp, gx, gy, f, dt);
//					synchronized(e) {
//		synchronized(gridLock) {
						angle = e.getShape().getAngleTo(x, y);
						gx = gravity * Math.cos(angle);
						gy = gravity * Math.sin(angle);
//						e.runMovement(tp, gx, gy, f, dt);
						e.updateMovement(gx, gy, f, dt);
//		}
//					}
				}
				n += (Math.PI*2D / roundTime * dt);
				n %= (Math.PI*2D);
			}
//		}
	}
}