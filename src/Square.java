
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class Square extends GameObj {
	public static final int SIZE = 20;
	public static final int INIT_X = 0;
	public static final int INIT_Y = (GameCourt.COURT_HEIGHT - SIZE) / 2;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int MAX_HEALTH = 40;
	public static final int MAX_VELOCITY = 5;

	private Direction currDirection;
	private int currHealth;
	private int score;
	private int roomCount;

	// tells the GameCourt whether the map and mobs need to be reset
	private boolean newMap;

	private TileMap tileMap;

	public Square(TileMap tileMap) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		currHealth = MAX_HEALTH;

		this.tileMap = tileMap;

		currDirection = Direction.RIGHT;
		newMap = false;

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(pos_x, pos_y, width, height);
	}

	@Override
	public void tick() {

		// sets the object's velocity according to user key input
		if (Input.isKeyPressed(KeyEvent.VK_LEFT)) {
			v_x = -MAX_VELOCITY;
			currDirection = Direction.LEFT;
		} else if (Input.isKeyPressed(KeyEvent.VK_RIGHT)) {
			v_x = MAX_VELOCITY;
			currDirection = Direction.RIGHT;
		} else if (Input.isKeyPressed(KeyEvent.VK_DOWN)) {
			v_y = MAX_VELOCITY;
			currDirection = Direction.DOWN;
		} else if (Input.isKeyPressed(KeyEvent.VK_UP)) {
			v_y = -MAX_VELOCITY;
			currDirection = Direction.UP;
		}

		// sets the object's velocity back to zero when key is released
		if (Input.isKeyReleased(KeyEvent.VK_LEFT) || Input.isKeyReleased(KeyEvent.VK_RIGHT)) {
			v_x = 0;
		}
		if (Input.isKeyReleased(KeyEvent.VK_UP) || Input.isKeyReleased(KeyEvent.VK_DOWN)) {
			v_y = 0;
		}

		// prevents going through walls
		Tile t = intersectsTileMap(tileMap);
		if (t != null) {
			bounceReduce(hitObj(t));
		}

		// updates score and tileMap when coin is intersected
		Coin c = intersectsCoin(tileMap);
		if (c != null) {
			tileMap.removeCoin(c);
			incrScore(c.getCoinValue());
		}

		// moves the object according to the velocity above
		move();

		// check for hitting walls to reset map
		Direction hitD = hitWall();
		if (hitD != null) {
			incrRoomCount();
			newMap = true;
			if (hitD == Direction.RIGHT) {
				tileMap.reset(0, pos_y / Tile.SIZE);
				pos_x = 0;
			} else if (hitD == Direction.LEFT) {
				tileMap.reset((GameCourt.COURT_WIDTH - SIZE) / Tile.SIZE, pos_y / Tile.SIZE);
				pos_x = GameCourt.COURT_WIDTH;
			} else if (hitD == Direction.DOWN) {
				tileMap.reset(pos_x / Tile.SIZE, 0);
				pos_y = 0;
			} else if (hitD == Direction.UP) {
				tileMap.reset(pos_x / Tile.SIZE, (GameCourt.COURT_HEIGHT - SIZE) / Tile.SIZE);
				pos_y = GameCourt.COURT_HEIGHT;
			}
		}

	}

	public void damage(int power) {
		currHealth = currHealth - power;
	}

	public Direction getCurrDirection() {
		return currDirection;
	}

	public void setCurrDirection(Direction currDirection) {
		this.currDirection = currDirection;
	}

	public int getCurrHealth() {
		return currHealth;
	}

	public void zeroHealth() {
		currHealth = 0;
	}

	public void incrScore(int incr) {
		score = score + incr;
	}

	public void resetScore() {
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void incrRoomCount() {
		roomCount++;
	}

	public void resetRoomCount() {
		roomCount = 0;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public boolean isNewMap() {
		return newMap;
	}

	public void setNewMap(boolean newMap) {
		this.newMap = newMap;
	}

}
