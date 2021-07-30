
/*
 * Human.java
 *
 * Feb 3, 2017
 */
package Agent;

import Game.Direction;
import static Game.Main.SCREEN_HEIGHT;
import static Game.Main.SCREEN_WIDTH;
import World.Render.Camera;
import World.World;
import javax.swing.Scrollable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

/* 
 * @author Per Eresund
 */
public class Human extends Agent implements MouseListener {
	private final Camera cam;
	private Controllable currentUnit;
	private World currentWorld;
	
	public Human(Controllable unit) {
		currentUnit = unit;
		currentWorld = currentUnit.getWorld();
		cam = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0);
		setCamPos();
	}
	
	public Human(World world) {
		currentWorld = world;
		currentUnit = currentWorld.getMain();
		cam = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0);
		setCamPos();
		for(int i = 0; i < 20; i++) {
			cam.zoomOut();
		}
	}
	
	private void setCamPos() {
		if (currentUnit != null) cam.setPos(currentUnit.getShape().getCenterX(), currentUnit.getShape().getCenterY(), true);
		else cam.setPos(currentWorld.width/2, currentWorld.height/2, true);
	}
	
	public void render(GameContainer container, Graphics g) {
		int x = 0, y = 0;
		
		cam.scale(g);
			setCamPos();
			currentWorld.render(g, cam);
			if (currentUnit != null) {
				x = currentUnit.getShape().getCenterX();
				y = currentUnit.getShape().getBottomY();
			}
		cam.unscale(g);
		
		g.setColor(Color.red);
		Input input = container.getInput();
		g.drawString("X: " + cam.getWorldX(input.getMouseX()) +
				   "\nY: " + cam.getWorldY(input.getMouseY()),
					 input.getMouseX()-64, input.getMouseY()-16);
		if (currentUnit != null) {
			g.setColor(Color.yellow);
			g.drawString("X: " + x + "\nY: " + y, 10, 32);
		}
		g.setColor(Color.yellow);
		g.drawString("Zoom: " + cam.getScale(), 10, 90);
	}
	
	private void runUnitInput(Input input, double dt) {
		if (currentUnit == null) return;
		
		if (input.isKeyDown(Input.KEY_W)) {
			currentUnit.move(Direction.NORTH, dt);
		}
		if (input.isKeyDown(Input.KEY_A)) {
			currentUnit.move(Direction.WEST, dt);
		}
		if (input.isKeyDown(Input.KEY_S)) {
			currentUnit.move(Direction.SOUTH, dt);
		}
		if (input.isKeyDown(Input.KEY_D)) {
			currentUnit.move(Direction.EAST, dt);
		}
	}
	
	public void runInput(GameContainer container, double dt) {
		Input input = container.getInput();
		runUnitInput(input, dt);
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			container.exit();
		}
	}

	@Override
	public void setInput(Input input) {}
	@Override
	public void inputEnded() {}
	@Override
	public void inputStarted() {}
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {}
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {}
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {}
	@Override
	public void mousePressed(int button, int x, int y) {}
	@Override
	public void mouseReleased(int button, int x, int y) {}
	
	@Override
	public boolean isAcceptingInput() { return true;}
	@Override
	public void mouseWheelMoved(int change) {
		int zoomAmount = 10;
		
		if (change < 0) {
			for (int i = 0; i < zoomAmount; i++) {
				cam.zoomOut();
			}
		} else if (change > 0) {
			for (int i = 0; i < zoomAmount; i++) {
				cam.zoomIn();
			}
		}
	}
}