
/*
 * MovingEntity.java
 *
 * Feb 1, 2017
 */
package Phys;

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
public class MovingEntity extends PhysEntity implements Controllable {
//	private static double calcVY(double gy, double f, double dt, double vy)	{ return (gy*dt + vy - f*vy*dt); }
//	private static double calcVX(double gx, double f, double dt, double vx)	{ return (gx*dt + vx - f*vx*dt); }
	private static double calcV(double g, double f, double dt, double v)	{ return (g*dt + v - f*v*dt); }
	
	private final Lock lock = new Lock();
	private final Lock moveLock = new Lock();
	private double vx, vy, xbuf, ybuf;
	private double addvx, addvy;
	private int weight;
	private int ID;

	public MovingEntity(PhysWorld world, Color color, int w, int h, boolean passable, int weight, int ID) {
		super(world, color, w, h, passable);
		this.weight = weight;
		vx = 0;
		vy = 0;
		if (color == Color.red) System.out.println("red id: " + ID);
		this.ID = ID;
	}
	public MovingEntity(PhysWorld world, Image img, int w, int h, boolean passable, int weight) {
		super(world, img, w, h, passable);
		this.weight = weight;
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
	public double getFx() { return vx*weight; }
	@Override
	public double getFy() { return vy*weight; }
	
	@Override
	public double getVx() { return vx; }
	@Override
	public double getVy() { return vy; }
	@Override
	public double getWeight() { return weight; }
	
	

	private double tempdtthing = 1;
	@Override
	public void move(Direction d, double dt) {
		tempdtthing = dt;
		int increment = 10000;
		boolean add = true;
		synchronized(moveLock) {
			switch(d) {
				case NORTH:
					addvy += -increment * (add ? dt : 1);
					break;
				case WEST:
					addvx += -increment * (add ? dt : 1);
					break;
				case SOUTH:
					addvy += increment * (add ? dt : 1);
					break;
				case EAST:
					addvx += increment * (add ? dt : 1);
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
			if (chained && System.currentTimeMillis() - chainT > 50) chained = false;
//			root = this;
//			walledlol = false;
			walledx = false;
			walledy = false;
			reboundCheck = true;
			if (redlog && ID == 0) logging = true;
			if (logging || logging2) System.out.println("\n" + " " + ID + " start");
			vx = calcV(gx, f, dt, vx);
			vy = calcV(gy, f, dt, vy);
			setXbuf(xbuf, vx, dt);
			setYbuf(ybuf, vy, dt);
			updateX(dt, 0, this);
			updateY(dt, 0, this);
			if (logging || logging2) System.out.println(" " + ID + " end");
			if (redlog && ID == 0) logging = false;
			reboundCheck = false;
//			root = null;
			xbuf %= 10;
			ybuf %= 10;
		}
	}
	
	private static final double C = 0.8;
	
	private void setXbuf(double add, double vx, double dt) {
		xbuf = add + vx*dt;
	}
	private void setYbuf(double add, double vy, double dt) {
		ybuf = add + vy*dt;
	}

	private boolean reboundCheck = false;
	@Override
	public void collideWith(PhysEntity e, double fx, double fy, double dt, int chainSize) {
		System.exit(0);
//		vx = (C*e.getWeight()*(fx-vx) + fx*e.getWeight() + getFx())/(e.getWeight()+weight);
//		vy = (C*e.getWeight()*(fy-vy) + fy*e.getWeight() + getFy())/(e.getWeight()+weight);
//		xbuf = vx*dt;
//		ybuf = vy*dt;
//		if (logging) {
//			if (reboundCheck) {
//				System.out.println(ID + " rebound");
//	//			return;
//			}
//			if (chainSize > 100) {
//				System.out.println(ID + " chain: " + chainSize);
//				System.out.println(ID + " vx: " + vx);
//	//			System.exit(0);
//	//			return;
//			}
//		}
//		chainSize++;
//		updateX(dt, chainSize);
//		updateY(dt, chainSize);
	}
	
	private boolean walledlol = false;
	
	private final Object collideLock = new Object();
	public void collideWith0(MovingEntity e, double fx, double fy, double dt, MovingEntity root) {
		synchronized(collideLock) {
			vx = (C*e.getWeight()*(fx-vx) + fx*e.getWeight() + getFx())/(e.getWeight()+weight);
			xbuf = vx*dt;
			if (Math.abs(xbuf) < 1) {
				if (vx == 0)  {
				} else if (vx > 0) {
					if (root.vx > 0) xbuf = 1;
				} else {
					if (root.vx < 0) xbuf = -1;
				}
			}
			vy = (C*e.getWeight()*(fy-vy) + fy*e.getWeight() + getFy())/(e.getWeight()+weight);
			ybuf = vy*dt;
//			if (vy == 0)  {
//			} else if (vy > 0) {
//				ybuf = Math.max(1, vy*dt);
//			} else {
//				ybuf = Math.min(-1, vy*dt);
//			}
		}
	}
	
	
	private boolean walledx, walledy;
	private boolean chained = false;
	private long chainT = 0;
	public void collideWith1(double dt, int chainSize, MovingEntity root) {
//		if (true) return;
//		if (root.color == Color.red) {
//			chained = true;
//			chainT = System.currentTimeMillis();
//		}
		chainSize++;
		if (logging) {
			System.out.println(" " + ID + " xwall: " + walledx + ", ywall: " + walledy);
			if (reboundCheck) {
				System.out.println(" " + ID + " rebound");
				return;
			}
			if (chainSize > 100) {
				System.out.println(" " + ID + " chain: " + chainSize);
				System.out.println(" " + ID + " vx: " + vx);
	//			System.exit(0);
	//			return;
			}
		}
//		else if (chainSize > 5) {
//			return;
//		}
		updateX(dt, chainSize, root);
		updateY(dt, chainSize, root);
	}
	
	private static boolean logging = false;
	private static boolean logging2 = false;
	private static boolean redlog = true;
	
	private static int increment = 1;
	private void updateX(double dt, int chainSize, MovingEntity root) {
		String indent = "";
		if (logging || logging2) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < chainSize-1; i++) {
				sb.append(" ");
			}
			indent = sb.toString();
		}
		
		boolean collided = false, moved = false;
		int srcx = rect.x, srcy = rect.y;
		double newdt = dt, lastdt = dt;
		
//		int newx, prevx, increment, incrementX = rect.w;
		int newx, prevx, increment, incrementX = MovingEntity.increment;
		
		increment = (int) (xbuf > 0
				? Math.min(incrementX, xbuf)
				: Math.max(-incrementX, xbuf));
		prevx = rect.x;
		newx = prevx + increment;
		
		PhysEntity e = null;
		PhysEntity laste = null;
		if (logging) {
			System.out.println(" " + ID + " prexbuf: " + xbuf);
			System.out.println(" " + ID + " x: " + rect.x);
			if (Math.abs(xbuf) < 1) System.out.println(" " + ID + " nomove");
		}
		
		MovingEntity toMove = null;
		while (Math.abs(xbuf) >= 1 && newdt > 0) {
//			try {
//				Thread.sleep(0);
//			} catch (InterruptedException ex) {}
			if (!world().isWithinBounds(newx, rect.y, rect.w, rect.h)) {
				walledx = true;
				if (logging) System.out.println(" " + ID + " collide with wall");
				if (chainSize > 0) return;
				setXbuf(-xbuf, 0, 0);
				vx = -vx;
				if (logging) System.out.println(" " + ID + " xbuf: " + xbuf);
				increment = (int) (xbuf > 0
						? Math.min(incrementX, xbuf)
						: Math.max(-incrementX, xbuf));
				newx = prevx + increment;
				continue;
			}
			
			e = world().getCollision(this, newx, rect.y, rect.w, rect.h);
			lastdt = newdt;
			newdt = newdt - (Math.abs(increment)/Math.abs(xbuf))*dt;
			if (logging) System.out.println(" " + ID + " newdt: " + newdt);
			if (e == null) {
				
				xbuf -= (newx - prevx);
				rect.x = prevx = newx;
				world().move(this, srcx, srcy, rect.w, rect.h);
				srcx = rect.x;
				srcy = rect.y;
				
				if (collided) {
					double prevvx = vx;
					double prevvy = vy;
					vx = (C*laste.getWeight()*(laste.getVx()-vx) + laste.getVx()*laste.getWeight() + getFx()) / (laste.getWeight()+weight);
					vy = (C*laste.getWeight()*(laste.getVy()-vy) + laste.getVy()*laste.getWeight() + getFy()) / (laste.getWeight()+weight);
					
					double diff = vx/prevvx;
					if (diff != diff) diff = vy/prevvy;
					setXbuf(0, xbuf, diff);
					setYbuf(0, ybuf, 1);
					if (diff < 0 && chainSize > 0) return; 
					if (logging) System.out.println(" " + ID + " xbuf: " + xbuf);
					if (laste instanceof MovingEntity) {
						MovingEntity me = (MovingEntity)laste;
						if (logging) System.out.println(" " + me.ID + " xbuf0: " + me.xbuf);
						me.collideWith0(this, prevvx, prevvy, newdt, root);
						if (logging) System.out.println(" " + me.ID + " xbuf1: " + me.xbuf);
						if (toMove != null) {
							toMove.collideWith1(lastdt, chainSize, root);
							walledx = walledx || toMove.walledx;
							walledy = walledy || toMove.walledy;
							if (walledx || walledy) {
								toMove.walledx = false;
								toMove.walledy = false;
								if (chainSize > 0) return;
								
								if (walledx) {
									vx = -vx*C;
									xbuf = -xbuf*C;
								}
								if (walledy) {
									vy = -vy*C;
									ybuf = -ybuf*C;
								}
							}
							toMove = null;
						}
						
						if (logging || logging2) if (root != null && chained) System.out.println(" " + indent + root.ID + " rootbuf: " + root.xbuf);
						if ((xbuf > 0 && me.xbuf > 0 && me.xbuf > xbuf) || (xbuf < 0 && me.xbuf < 0 && me.xbuf < xbuf)) {
							toMove = me;
							if (logging || logging2) System.out.println(" " + indent + me.ID + " will wait");
						} else {
							if (logging || logging2) System.out.println(" " + indent + me.ID + " will not wait");
							me.collideWith1(newdt, chainSize, root);
							walledx = walledx || me.walledx;
							walledy = walledy || me.walledy;
							if (walledx || walledy) {
								me.walledx = false;
								me.walledy = false;
								if (chainSize > 0) return;
								
								if (walledx) {
									vx = -vx*C;
									xbuf = -xbuf*C;
								}
								if (walledy) {
									vy = -vy*C;
									ybuf = -ybuf*C;
								}
							}
						}
					} else System.exit(0);
				} else {
					if (logging) System.out.println(" " + ID + " moving freely, x: " + rect.x);
					walledx = false;
				}
				increment = (int) (xbuf > 0
						? Math.min(incrementX, xbuf)
						: Math.max(-incrementX, xbuf));
				newx = rect.x + increment;
				collided = false;
				moved = true;
			} else {
				laste = e;
				if (logging) {
					System.out.println(" " + ID + " collide with " + ((MovingEntity)e).ID);
					if ((xbuf > 0) ? (newx > prevx) : (newx < prevx)) {

					} else {
						System.out.println(" " + ID + " bug");
					}
				}
				if (xbuf > 0) {
					newx = e.rect.getLeftX()-rect.w;
				} else {
					newx = e.rect.getRightX()+1;
				}
				collided = true;
				moved = true;
				continue;
			}
		}
		if (collided) {
			xbuf = 0;
		}
		if (moved) {
			world().move(this, srcx, srcy, rect.w, rect.h);
		}
		if (toMove != null) {
			toMove.collideWith1(newdt, chainSize, root);
			walledx = walledx || toMove.walledx;
			walledy = walledy || toMove.walledy;
			if (walledx || walledy) {
				toMove.walledx = false;
				toMove.walledy = false;
				if (chainSize > 0) return;

				vx = -vx*C;
				xbuf = -xbuf*C;
			}
			toMove = null;
		}
	}
	private void updateY(double dt, int chainSize, MovingEntity root) {
		boolean collided = false, moved = false;
		int srcx = rect.x, srcy = rect.y;
		double newdt = dt, lastdt = dt;
		
//		int newy, prevy, increment, incrementY = rect.h;
		int newy, prevy, increment, incrementY = MovingEntity.increment;
		
		increment = (int) (ybuf > 0
				? Math.min(incrementY, ybuf)
				: Math.max(-incrementY, ybuf));
		prevy = rect.y;
		newy = prevy + increment;
		
		PhysEntity e = null;
		PhysEntity laste = null;
		
		MovingEntity toMove = null;
		while (Math.abs(ybuf) >= 1 && newdt > 0) {
//			try {
//				Thread.sleep(0);
//			} catch (InterruptedException ex) {}
			if (!world().isWithinBounds(rect.x, newy, rect.w, rect.h)) {
				setYbuf(-ybuf, 0, 0);
				vy = -vy;
				increment = (int) (ybuf > 0
						? Math.min(incrementY, ybuf)
						: Math.max(-incrementY, ybuf));
				newy = prevy + increment;
				walledy = true;
				continue;
			}
			
			e = world().getCollision(this, rect.x, newy, rect.w, rect.h);
			lastdt = newdt;
			newdt = newdt - (Math.abs(increment)/Math.abs(ybuf))*dt;
			if (e == null) {
				
				ybuf -= (newy - prevy);
				rect.y = prevy = newy;
				world().move(this, srcx, srcy, rect.w, rect.h);
				srcx = rect.x;
				srcy = rect.y;
				
				if (collided) {
					double prevvx = vx;
					double prevvy = vy;
					vx = (C*laste.getWeight()*(laste.getVx()-vx) + laste.getVx()*laste.getWeight() + getFx()) / (laste.getWeight()+weight);
					vy = (C*laste.getWeight()*(laste.getVy()-vy) + laste.getVy()*laste.getWeight() + getFy()) / (laste.getWeight()+weight);
					
					double diff = vy/prevvy;
					if (diff != diff) diff = vx/prevvx;
					setXbuf(0, xbuf, 1);
					setYbuf(0, ybuf, diff);
					if (laste instanceof MovingEntity) {
						MovingEntity me = (MovingEntity)laste;
						me.collideWith0(this, prevvx, prevvy, newdt, root);
						if (toMove != null) {
							toMove.collideWith1(lastdt, chainSize, root);
							walledx = walledx || toMove.walledx;
							walledy = walledy || toMove.walledy;
							if (walledx || walledy) {
								toMove.walledx = false;
								toMove.walledy = false;
								if (chainSize > 0) return;
								
								if (walledx) {
									vx = -vx*C;
									xbuf = -xbuf*C;
								}
								if (walledy) {
									vy = -vy*C;
									ybuf = -ybuf*C;
								}
							}
							toMove = null;
						}
						
						if ((ybuf > 0 && me.ybuf > 0) || (ybuf < 0 && me.ybuf < 0)) {
							toMove = me;
						} else {
							me.collideWith1(newdt, chainSize, root);
							walledx = walledx || me.walledx;
							walledy = walledy || me.walledy;
							if (walledx || walledy) {
								me.walledx = false;
								me.walledy = false;
								if (chainSize > 0) return;
								
								if (walledx) {
									vx = -vx*C;
									xbuf = -xbuf*C;
								}
								if (walledy) {
									vy = -vy*C;
									ybuf = -ybuf*C;
								}
							}
						}
					} else System.exit(0);
				} else {
					walledy = false;
				}
				increment = (int) (ybuf > 0
						? Math.min(incrementY, ybuf)
						: Math.max(-incrementY, ybuf));
				newy = rect.y + increment;
				collided = false;
				moved = true;
			} else {
				laste = e;
				if (ybuf > 0) {
					newy = e.rect.getTopY()-rect.h;
				} else {
					newy = e.rect.getBottomY()+1;
				}
				collided = true;
				moved = true;
				continue;
			}
		}
		if (collided) {
			ybuf = 0;
		}
		if (moved) {
			world().move(this, srcx, srcy, rect.w, rect.h);
		}
		if (toMove != null) {
			toMove.collideWith1(newdt, chainSize, root);
			walledx = toMove.walledx;
			walledy = toMove.walledy;
			if (walledx || walledy) {
				toMove.walledx = false;
				toMove.walledy = false;
				if (chainSize > 0) return;

				vx = -vx*C;
				xbuf = -xbuf*C;
			}
			toMove = null;
		}
	}
	
	Color spare = null;
	private static Color temp = new Color(0, 0, 0);
	@Override
	public void render(Graphics g, int x, int y) {
		spare = null;
		
//		if (root == this) {
//			spare = color;
//			color = Color.green;
//		}
		
		if (spare == null) {
			if (walledx || walledy) {
				spare = color;
				color = Color.white;
			} else if (chained) {
				spare = color;
				color = Color.yellow;
			}
		}
		
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
		
		if (color == (Color.red)) {
			Renderer.end();
			
//			g.drawString("xbuf: " + (xbuf + vx*tempdtthing) + "\nybuf: " + (ybuf + vy*tempdtthing) + "\nvx: " + (int)vx + "\nvy: " + (int)vy, x, y);
			
			DecimalFormat df = new DecimalFormat("0.00");
			g.setColor(Color.black);
			String xbufs = df.format((xbuf + vx*tempdtthing));
			String ybufs = df.format((ybuf + vy*tempdtthing));
			String vxs	 = df.format(vx);
			String vys	 = df.format(vy);
			
			g.drawString(xbufs + "\n" +
						 ybufs + "\n" +
						 vxs + "\n" + 
						 vys, x, y);
			
			Renderer.startRects();
		}
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