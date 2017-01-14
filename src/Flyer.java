
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class Flyer extends Mob {
	public static final int SIZE = 20;
	public static final int SPEED = 3;
	public static final int MAX_HEALTH = 5;
	public static final int POWER = 2;
	public static final int POINTS_WORTH = 5;

	public Flyer(int velX, int velY, int initX, int initY, Square targetSquare) {
		super(velX, velY, initX, initY, SIZE, SIZE, MAX_HEALTH, POWER, POINTS_WORTH, targetSquare);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(pos_x, pos_y, width, height);
	}

	@Override
	public void mover() {
		move();

		// make the mob bounce off walls
		bounce(hitWall());

	}

	/**
	 * Returns a list of new mob objects
	 * 
	 * @param count
	 * @param tileMap
	 * @param targetSquare
	 * @return List<Mob> populated with flyer type mobs
	 */
	public static List<Mob> spawn(int count, TileMap tileMap, Square targetSquare) {
		List<Mob> toSpawn = new LinkedList<Mob>();
		Random rand = new Random();
		int[] choices = { -SPEED, SPEED };

		for (int i = 0; i < count; i++) {

			int x = rand.nextInt(TileMap.SIZE - 2) + 1;
			int y = rand.nextInt(TileMap.SIZE - 2) + 1;

			int xVel = choices[rand.nextInt(2)];
			int yVel = choices[rand.nextInt(2)];

			toSpawn.add(new Flyer(xVel, yVel, x * Tile.SIZE + (Tile.SIZE - Flyer.SIZE) / 2,
					y * Tile.SIZE + (Tile.SIZE - Flyer.SIZE) / 2, targetSquare));
		}
		return toSpawn;
	}

}
