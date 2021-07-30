/*
 *   Direction.java
 *
 *   Apr 20, 2016
 */
package Game;

/* 
 * @author Per Eresund
 */
public enum Direction {
	NORTH, 
	SOUTH,
	WEST,
	EAST,
	NORTH_WEST,
	NORTH_EAST,
	SOUTH_WEST,
	SOUTH_EAST,
	
	FORWARD,
	BACKWARD,
	LEFT,
	RIGHT;
	
	public static Direction randomDirection() {
		Direction[] values = Direction.values();
		Direction type = values[(int)(Math.random() * values.length)];
		return type;
	}
}