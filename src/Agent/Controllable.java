/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agent;

import Game.Direction;
import Shape.Rect;
import Shape.Shape;
import World.World;

/**
 *
 * @author Humpadumpa
 */
public interface Controllable {
	public void move(Direction d, double dt);
	public World getWorld();
	public Shape getShape();
}
