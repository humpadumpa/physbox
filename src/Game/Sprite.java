
/*
 * Images.java
 *
 * Feb 1, 2017
 */
package Game;

/* 
 * @author Per Eresund
 */
public enum Sprite {
	DEFAULT("./textures/0.png"),
	PLAYER("./textures/player/plane.png"),
	MOUSE("./textures/0.png"),
	;
	
	public String path;
	
	Sprite(String path) {
		this.path = path;
	}
}