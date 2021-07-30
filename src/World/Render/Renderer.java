
/*
 * Renderer.java
 *
 * Feb 8, 2017
 */
package World.Render;

import Shape.Rect;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/* 
 * @author Per Eresund
 */
public class Renderer {

	
	public static void end() {
		GL11.glEnd();
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	public static void startRects() {
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
	}
	
	
	
	
	
	public static void fillRect(Rect rect, Color color) {
		fillRect(rect.getLeftX(), rect.getTopY(), rect.getWidth(), rect.getHeight(), color);
	}
	public static void fillRect(Rect rect) {
		fillRect(rect.getLeftX(), rect.getTopY(), rect.getWidth(), rect.getHeight());
	}
	public static void fillRect(int x, int y, int w, int h, Color color, float alpha) {
		GL11.glColor4f(color.r, color.g, color.b, alpha);
		fillRect(x, y, w, h);
	}
	public static void fillRect(int x, int y, int w, int h, Color color) {
		GL11.glColor4f(color.r, color.g, color.b, color.a);
		fillRect(x, y, w, h);
	}
	public static void fillRect(int x, int y, int w, int h) {
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + w, y);
		GL11.glVertex2f(x + w, y + h);
		GL11.glVertex2f(x, y + h);
	}
}