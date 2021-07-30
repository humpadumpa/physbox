
/*
 * Platform.java
 *
 * Feb 1, 2017
 */
package Platform;

import World.Entity;
import Phys.PhysEntity;
import Phys.PhysWorld;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/* 
 * @author Per Eresund
 */
public class Platform extends PlatformEntity {
	public Platform(PlatformWorld world, Color color, int w, int h, boolean passable) {
		super(world, color, w, h, passable);
	}
	public Platform(PlatformWorld world, Image img, int w, int h, boolean passable) {
		super(world, img, w, h, passable);
	}
	
}