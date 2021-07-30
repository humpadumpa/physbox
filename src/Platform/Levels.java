
/*
 * Worlds.java
 *
 * Feb 3, 2017
 */
package Platform;

import Platform.Platform;
import World.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class Levels {
	public static final int LEVEL1 = 0;
	
	public static PlatformWorld getLevel(int level) {
		switch(level) {
			case LEVEL1:
				return lev1();
		}
		return null;
	}
	
	private static PlatformWorld lev1() {
		int worldW = 2500, worldH = 2500;
		double gravity = 0, friction = 0.01;
		PlatformWorld world = new PlatformWorld(worldW, worldH, gravity, friction);
		
		Color color;
		Platform p;
		MovingEntity me;
		int x, y, width, height;
		
		color = Color.red;
		width = 256;
		height = 256;
		me = new MovingEntity(world, color, width, height, false, 0);
		x = worldW/2;
		y = worldH/2;
		me.setPos(x, y, true);
		me.place();
		world.setMain(me);
		
		width = 32;
		height = 32;
		for (x = 0, y = worldH-height; x <= worldW-width; x += width) {
			color = new Color(63, 0, 127);
			p = new Platform(world, color, width, height, false);
			p.setPos(x, y, false);
			if (p.place()) {
//				System.out.println("placement success, left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
			} else {
				System.out.println("placement failed,  left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
			}
		}
//		for (int i = 0; i < 3000; i++){
//			color = new Color(63, 0, 127);
//			x = (int)(Math.random()*worldW-width);
//			y = (int)(Math.random()*worldH-height);
////			me = new MovingEntity(world, color, (int)(8 + (Math.random() * 64)), (int)(8 + (Math.random() * 64)), false, 5, i+1);
//			me = new MovingEntity(world, color, width, height, false, i+1);
//			me.setPos(x, y, false);
//			if (me.place()) {
////				System.out.println("placement success, left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
//			} else {
////				System.out.println("placement failed,  left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
//			}
//		}
		
		return world;
	}
}