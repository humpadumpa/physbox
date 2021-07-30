/*
 * Game.java
 *
 * Jan 28, 2017
 */
package Game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import ThreadPool.*;

/* 
 * @author Per Eresund
 */
public class Main {
	public static final boolean logging = false;
	
	public static final int SCREEN_WIDTH		= 1920;
	public static final int SCREEN_HEIGHT		= 1080;
	public static final int NUMBER_OF_THREADS	= 4;
	public static final ThreadPool THREAD_POOL	= new ThreadPool(NUMBER_OF_THREADS);
	public static final Thread POOLER			= new Thread(THREAD_POOL);

	private static AppContainer container;
	
	public static void main(String[] args) {
		POOLER.start();
		container = new AppContainer(100, THREAD_POOL);
		THREAD_POOL.newTask(container);
		if (!POOLER.isAlive()) System.exit(0);
	}
    
}
