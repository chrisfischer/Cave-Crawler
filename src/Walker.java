

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class Walker extends Mob {

	public static final int SIZE = 25;
	public static final int SPEED = 1;
	public static final int MAX_HEALTH = 10;
	public static final int POWER = 2;
	public static final int POINTS_WORTH = 3;

	private TileMap tileMap;

	public Walker(int velX, int velY, int initX, int initY, TileMap tileMap, Square targetSquare) {
		super(velX, velY, initX, initY, SIZE, SIZE, MAX_HEALTH, POWER, POINTS_WORTH, targetSquare);

		this.tileMap = tileMap;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(FileManager.walker, pos_x, pos_y, width, height, null);
	}

	@Override
	public void mover() {
		Tile t = intersectsTileMap(tileMap);
		if (t != null) {
			bounce(hitObj(t));
		}
		bounce(hitWall());

		move();
	}
	
	/**
	 * Returns a list of new mob objects
	 * 
	 * @param count
	 * @param tileMap
	 * @param targetSquare
	 * @return List<Mob> populated with walker type mobs
	 */
	public static List<Mob> spawn(int count, TileMap tileMap, Square targetSquare) {
		List<Mob> toSpawn = new LinkedList<Mob>();
		Random rand = new Random();
		int[] choices = { -SPEED, 0, SPEED, 0 };
		int[] choices2 = { -SPEED, SPEED };

		// creates a list of poisons with random x and y velocities
		for (int i = 0; i < count; i++) {
			int x = 0;
			int y = 0;
			do {
				x = rand.nextInt(TileMap.SIZE - 2) + 1;
				y = rand.nextInt(TileMap.SIZE - 2) + 1;
			} while (tileMap.isWall(x, y));

			int xVel = choices[rand.nextInt(2)];
			int yVel = 0;
			if (xVel == 0) {
				yVel = choices2[rand.nextInt(2)];
			}

			toSpawn.add(new Walker(xVel, yVel, x * Tile.SIZE + (Tile.SIZE - Walker.SIZE) / 2,
					y * Tile.SIZE + (Tile.SIZE - Walker.SIZE) / 2, tileMap, targetSquare));
		}
		return toSpawn;
	}

}
