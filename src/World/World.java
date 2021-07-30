
/*
 * World.java
 *
 * Jan 29, 2017
 */
package World;

import Agent.Controllable;
import Game.Lock;
import Shape.Rect;
import ThreadPool.Task;
import ThreadPool.ThreadPool;
import Phys.MovingEntity;
import World.Render.Camera;
import World.Render.Renderer;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/* 
 * @author Per Eresund
 */
public abstract class World<E extends Entity> {
	public final int width, height;		//World size in pixels
	protected int gridW, gridH;			//Number of horizontal/vertical tiles for dividing space
	protected int tileSize;				//Length of a tile's side in pixels
	protected int tileW, tileH;			//Length of a tile's sides in pixels
	private Grid<E> grid;
	protected Controllable main;
	
	protected final Lock gridLock = new Lock();
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		calcSize();
		initGrid();
	}
	
	protected void calcSize() {
//		tileSize	= 1280;
		tileW		= width/10;
		tileH		= height/10;
		gridW		= width/tileW+1;
		gridH		= height/tileH+1;
	}
	
	protected Grid<E> grid() {
//		synchronized(gridLock) {
			return grid;
//		}
	}
	
	@SuppressWarnings("Convert2Diamond")
	protected void initGrid() {
		grid = new Grid(width, height, gridW, gridH, tileW, tileH);
	}
	
	public boolean canBePlaced(E e) {
		synchronized(gridLock) {
			return grid.canBePlaced(e);
		}
	}
	public boolean isWithinBounds(int x, int y, int w, int h) {
		synchronized(gridLock) {
			return grid.isWithinBounds(x, y, w, h);
		}
	}
	public boolean isWithinBounds(E e) {
		synchronized(gridLock) {
			return grid.isWithinBounds(e);
		}
	}
	public E getCollision(E e, int x, int y, int w, int h) {
		synchronized(gridLock) {
			return grid.getCollision(e, x, y, w, h);
		}
	}
	public void setMain(Controllable main) {
		this.main = main;
	}
	public Controllable getMain() {
		return main;
	}
	
	public void move(E e, int srcX, int srcY, int srcW, int srcH) {
		synchronized(gridLock) {
			grid.move(e, srcX, srcY, srcW, srcH);
		}
	}
	
	public void place(E e) {
		synchronized(gridLock) {
			grid.add(e);
		}
	}
	
	public void remove(E e) {
		synchronized(gridLock) {
			grid.remove(e);
		}
	}
	
	private final ArrayList<E> visited = new ArrayList();
	private void removeDuplicates(E[] entities) {
		for (int i = 0; i < entities.length; i++) {
			if (visited.contains(entities[i])) {
				entities[i] = null;
			} else {
				visited.add(entities[i]);
			}
		}
		visited.clear();
	}
	
	public void render(Graphics g, Camera cam) {
		g.setColor(Color.blue);
		
//		synchronized(gridLock) {
			Renderer.startRects();
			grid.renderTiles(g, cam);
//		}
		
		E[] entities;
//		synchronized(gridLock) {
			entities = grid.getNearby(cam.getScaledX(), cam.getScaledY(), cam.getScaledW(), cam.getScaledH());
//			removeDuplicates(entities);
//			entitiesOnScreen = entities;
//			System.out.println("nentities: " + entitiesOnScreen.length);
//		}
		
//		synchronized(gridLock) {
			for (E e : entities) {
				if (e == null) continue;
//				synchronized(e) {
					cam.renderEntity(g, e);
//				}
			}
//		}
		
		Renderer.end();
	}
	
//	public E[] entitiesOnScreen = (E[])new Entity[0];
	
	private UpdateTask updateTask;
	private double updateTaskdt;
	public void update(ThreadPool tp, double dt) {
		updateTaskdt += dt;
		if (updateTask == null || updateTask.isDone()) {
			updateTask = new UpdateTask(0, updateTaskdt);
			tp.newTask(updateTask);
			updateTaskdt = 0;
		}
	}
	
	/*------------------------------------------------------------------------*/
	protected class UpdateTask extends Task {
		protected final double dt;
		
		protected UpdateTask(int priority, double dt) {
			super(priority);
			this.dt = dt;
		}

		@Override
		public void start() {
			
		}
	}
}