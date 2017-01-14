
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

@SuppressWarnings("serial")
public class Bullet extends GameObj {

	public static final int WIDTH = 10; // when horizontal
	public static final int HEIGHT = 5; // when horizontal
	public static final int MAX_VEL = 10;
	private int power;

	private Direction d;
	private TileMap tileMap;
	private MobSpawner mobSpawner;

	public Bullet(int pos_x, int pos_y, int power, Direction d, TileMap tileMap, MobSpawner mobSpawner) {
		super(getXVel(d), getYVel(d), pos_x, pos_y, getWidth(d), getHeight(d), GameCourt.COURT_WIDTH,
				GameCourt.COURT_HEIGHT);

		this.power = power;
		this.d = d;
		this.tileMap = tileMap;
		this.mobSpawner = mobSpawner;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(pos_x, pos_y, getWidth(d), getHeight(d));
	}

	@Override
	public void tick() {

		// stops bullet if it hits a wall
		if (hitWall() != null) {
			v_x = 0;
			v_y = 0;
		}
		// stops bullet if it hits the tileMap
		Tile t = intersectsTileMap(tileMap);
		if (t != null) {
			v_x = 0;
			v_y = 0;
		}
		// stops the bullet if it hits mob, also deals damage and increases the
		// score
		List<Mob> mobs = intersectsMobs(mobSpawner);
		if (!mobs.isEmpty()) {
			Mob targetMob = mobs.get(0); // just deals damage to the first one
			v_x = 0;
			v_y = 0;
			targetMob.reduceHealth(power);
		}
		move();
	}

	/**
	 * Gives the bullet the proper x-velocity given its direction and MAX_VEL
	 * 
	 * @param d
	 * @return integer velocity for the x-direction
	 */
	private static int getXVel(Direction d) {
		if (d == Direction.LEFT) {
			return -MAX_VEL;
		} else if (d == Direction.RIGHT) {
			return MAX_VEL;
		} else {
			return 0;
		}
	}

	/**
	 * Gives the bullet the proper y-velocity given its direction and MAX_VEL
	 * 
	 * @param d
	 * @return integer velocity for the y-direction
	 */
	private static int getYVel(Direction d) {
		if (d == Direction.UP) {
			return -MAX_VEL;
		} else if (d == Direction.DOWN) {
			return MAX_VEL;
		} else {
			return 0;
		}
	}

	/**
	 * Gives the bullet the proper width determined by its direction and WIDTH
	 * 
	 * @param d
	 * @return integer width
	 */
	public static int getWidth(Direction d) {
		if (d == Direction.LEFT || d == Direction.RIGHT) {
			return WIDTH;
		} else if (d == Direction.UP || d == Direction.DOWN) {
			return HEIGHT;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Gives the bullet the proper height determined by its direction and HEIGHT
	 * 
	 * @param d
	 * @return integer height
	 */
	public static int getHeight(Direction d) {
		if (d == Direction.LEFT || d == Direction.RIGHT) {
			return HEIGHT;
		} else if (d == Direction.UP || d == Direction.DOWN) {
			return WIDTH;
		} else {
			throw new NullPointerException();
		}
	}

	public int getPower() {
		return power;
	}

}
