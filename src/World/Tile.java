
/*
 * Tile.java
 *
 * Jan 29, 2017
 */
package World;

import java.util.ArrayList;

/* 
 * @author Per Eresund
 */
public class Tile<E extends Entity> {
	private volatile ArrayList<E> elements;
	
	{
		elements = new ArrayList();
	}
	
	protected void add(E elem) {
		elements.add(elem);
	}
	
	protected void remove(E elem) {
		elements.remove(elem);
	}
	
	protected E getCollision(E elem, int x, int y, int w, int h) {
		for (E elem2 : elements) {
			if (elem == elem2) continue;
			if (elem2.collidesWith(x, y, w, h)) return elem2;
		}
		return null;
	}
	
	protected E[] getElements() {
		E[] arr = (E[]) new Entity[elements.size()];
		return elements.toArray(arr);
	}
}