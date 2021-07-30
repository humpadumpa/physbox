
/*
 * AppContainer.java
 *
 * Jan 29, 2017
 */
package Game;

import static Game.Main.SCREEN_HEIGHT;
import static Game.Main.SCREEN_WIDTH;
import ThreadPool.Task;
import ThreadPool.ThreadPool;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/* 
 * @author Per Eresund
 */
public class AppContainer extends Task {
	private Game game;
	private AppGameContainer app;
	private ThreadPool threadPool;

	public AppContainer(int priority, ThreadPool threadPool) {
		super(priority);
		this.threadPool = threadPool;
	}

	@Override
	public void start() {
		System.out.println("container started");
		game = new Game(threadPool);
		try {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
			app = new AppGameContainer(game);
			app.setUpdateOnlyWhenVisible(false);
			app.setAlwaysRender(true);
			app.setShowFPS(true);
			app.setVSync(false);
			app.setTargetFrameRate(0);
			app.setClearEachFrame(false);
			app.setMultiSample(0);
//			app.setIcon("barrel.png");
			app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			app.start();
		} catch (SlickException ex) {}
    }
}