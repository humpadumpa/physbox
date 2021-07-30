
/*
 * Corner.java
 *
 * Jun 24, 2016
 */
package Shape;

/* 
 * @author Per Eresund
 */
public enum Corner {
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT;
	
	public Corner opposite() {
		switch(this) {
			case TOP_LEFT:		return BOTTOM_RIGHT;
			case TOP_RIGHT:		return BOTTOM_LEFT;
			case BOTTOM_LEFT:	return TOP_RIGHT;
			case BOTTOM_RIGHT:	return TOP_LEFT;
			
			default:			return this;
		}
	}
	
	public static Corner randomCorner() {
		Corner[] values = Corner.values();
		Corner type = values[(int)(Math.random() * values.length)];
		return type;
	}
	
}