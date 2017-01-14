
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class Follower extends Mob {
	public static final int SIZE = 20;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int SPEED = 3;
	public static final int MAX_HEALTH = 5;
	public static final int POWER = 1;
	public static final int POINTS_WORTH = 8;

	private Square targetSquare;

	public Follower(int initX, int initY, Square targetSquare) {
		super(INIT_VEL_X, INIT_VEL_Y, initX, initY, SIZE, SIZE, MAX_HEALTH, POWER, POINTS_WORTH, targetSquare);

		this.targetSquare = targetSquare;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(pos_x, pos_y, width, height);
	}

	@Override
	public void mover() {
		setVel(targetSquare.pos_x, targetSquare.pos_y);

		move();
	}

	/**
	 * determines the proper x and y velocity depending on the targetSquare
	 * location so that the mob "follows" the square
	 * 
	 * @param targetX
	 * @param targetY
	 */
	private void setVel(int targetX, int targetY) {
		int dx = targetX - pos_x;
		int dy = targetY - pos_y;
		double angle = Math.atan((double) dy / dx);
		if (dx < 0) {
			v_x = -(int) (SPEED * Math.cos(angle));
			v_y = -(int) (SPEED * Math.sin(angle));
		} else {
			v_x = (int) (SPEED * Math.cos(angle));
			v_y = (int) (SPEED * Math.sin(angle));
		}
	}

	/**
	 * Returns a list of new mob objects
	 * 
	 * @param count
	 * @param tileMap
	 * @param targetSquare
	 * @return List<Mob> populated with follower type mobs
	 */
	public static List<Mob> spawn(int count, TileMap tileMap, Square targetSquare) {
		List<Mob> toSpawn = new LinkedList<Mob>();
		Random rand = new Random();

		for (int i = 0; i < count; i++) {

			int x = rand.nextInt(TileMap.SIZE - 2) + 1;
			int y = rand.nextInt(TileMap.SIZE - 2) + 1;

			toSpawn.add(new Follower(x * Tile.SIZE + (Tile.SIZE - Follower.SIZE) / 2,
					y * Tile.SIZE + (Tile.SIZE - Follower.SIZE) / 2, targetSquare));
		}
		return toSpawn;
	}

}
