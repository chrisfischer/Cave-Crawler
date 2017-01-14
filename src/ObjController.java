

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ObjController {
	
	private List<GameObj> gameObjects;

	public ObjController() {
		gameObjects = new ArrayList<GameObj>();
	}

	public void tick() {
		for (GameObj obj : gameObjects) {
			obj.tick();
		}

	}

	public void draw(Graphics g) {
		for (GameObj obj : gameObjects) {
			obj.draw(g);
		}
	}
	
	/**
	 * Adds a gameObj to the list
	 * @param obj
	 */
	public void addGameObj(GameObj obj) {
		if (obj == null)
			throw new NullPointerException();
		gameObjects.add(obj);
	}
	
	/**
	 * Removes all objects from its list
	 */
	public void clear() {
		gameObjects.removeAll(gameObjects);
	}


}