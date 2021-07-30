
/*
 * Worlds.java
 *
 * Feb 3, 2017
 */
package Phys;

import World.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class Levels {
	public static final int LEVEL1 = 0;
	
	public static PhysWorld getLevel(int level) {
		switch(level) {
			case LEVEL1:
				return lev1();
		}
		return null;
	}
	
	private static PhysWorld lev1() {
		int worldW = 1000, worldH = 98;
		double gravity = 0, friction = 0.01;
		PhysWorld world = new PhysWorld(worldW, worldH, gravity, friction);
		
		Color color;
		MovingEntity me;
		int x, y, width, height, weight;
		
		color = Color.red;
		width = 96;
		height = 96;
		weight = 50000;
		me = new MovingEntity(world, color, width, height, false, weight, 0);
		x = worldW/2;
		y = worldH/2;
		me.setPos(x, y, true);
		me.place();
		world.setMain(me);
		
		width = 32;
		height = 32;
		weight = 5;
		for (x = 0, y = worldH-height; x <= worldW-width; x += width) {
			color = new Color(63, 0, 127);
//			p = new Platform(level, color, width, height, false);
//			p.setPos(x, y, false);
//			if (p.place()) {
////				System.out.println("placement success, left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
//			} else {
//				System.out.println("placement failed,  left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
//			}
		}
		for (int i = 0; i < 100; i++){
			color = new Color(63, 0, 127);
//			x = (int)(Math.random()*worldW-width);
//			y = (int)(Math.random()*worldH-height);
			x = (int)(i*(width+2));
			y = (int)(world.height/2-height/2);
//			me = new MovingEntity(world, color, (int)(8 + (Math.random() * 64)), (int)(8 + (Math.random() * 64)), false, 5, i+1);
			me = new MovingEntity(world, color, width, height, false, weight, i+1);
			me.setPos(x, y, false);
			if (me.place()) {
//				System.out.println("placement success, left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
			} else {
//				System.out.println("placement failed,  left: " + x + ", right: " + (x+width-1) + ", top: " + y + ", bottom: " + (y+height-1));
			}
		}
		
		return world;
	}
}