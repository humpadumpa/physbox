/*
 * Game.java
 *
 * Jan 28, 2017
 */
package Game;

import Agent.Human;
import static Game.Main.SCREEN_HEIGHT;
import static Game.Main.SCREEN_WIDTH;
import ThreadPool.ThreadPool;
import World.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/* 
 * @author Per Eresund
 */
public class Game implements org.newdawn.slick.Game{
	private final ThreadPool threadPool;
	private World world;
	private Human human;
	
	public Game(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	@Override
	public boolean closeRequested() {
		Main.THREAD_POOL.stop();
		return true;
	}

	@Override
	public String getTitle() {
		return "PhysWorld";
	}

	@Override
	public void init(GameContainer container) throws SlickException {
//		container.setMouseCursor(new Image(Sprite.MOUSE.path), 1, 1);
		world = new Platform.PlatformWorld(4096, 1024, 10, 0.1);
//		world = Phys.Levels.getLevel(Levels.LEVEL1);
		world = Platform.Levels.getLevel(Platform.Levels.LEVEL1);
		
//		Image img = new Image(Sprite.PLAYER.path);
//		MovingEntity e = new MovingEntity(world, img, img.getWidth(), img.getHeight(), false, 10);
		human = new Human(world);
		container.getInput().addMouseListener(human);
		lastTime = System.nanoTime();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
//		System.out.println(fps);
		g.setColor(new Color(0f, 0f, 0f, 1));//60f/Math.max(1, fps)));
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		human.render(container, g);
	}

	private long lastTime;
	private float fps = 0;
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		long thisTime = System.nanoTime();
		double dt = (thisTime - lastTime) / 1000000000D;
		human.runInput(container, dt);
		world.update(threadPool, dt);
		fps = (float)(1d/dt);
		lastTime = thisTime;
	}
    
}