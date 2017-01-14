
import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Light extends GameObj {

	public static final int RADIUS = 210;
	public static final int DIAMETER = 2 * RADIUS;
	public static final int INIT_X = Square.INIT_X - RADIUS + Square.SIZE / 2;
	public static final int INIT_Y = Square.INIT_Y - RADIUS + Square.SIZE / 2;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int AMBIENT_ALPHA = 240;
	public static final int AMBIENT_RED = 10;
	public static final int AMBIENT_GREEN = 10;
	public static final int AMBIENT_BLUE = 10;

	private Square targetSquare;

	public Light(Square targetSquare) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT,
				GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);

		this.targetSquare = targetSquare;
	}

	@Override
	public void draw(Graphics g) {
		// draws transparent black rectangles everywhere the light is not
		Color c = new Color(AMBIENT_RED, AMBIENT_GREEN, AMBIENT_BLUE, AMBIENT_ALPHA);
		g.setColor(c);
		g.fillRect(0, 0, pos_x, GameCourt.COURT_HEIGHT);
		g.fillRect(pos_x, 0, DIAMETER, pos_y);
		g.fillRect(pos_x, pos_y + DIAMETER, DIAMETER, GameCourt.COURT_HEIGHT - DIAMETER - pos_y);
		g.fillRect(pos_x + DIAMETER, 0, GameCourt.COURT_WIDTH - pos_x + DIAMETER, GameCourt.COURT_HEIGHT);

		// draws a faded out circle
		for (int x = 0; x <= DIAMETER; x++) {
			for (int y = 0; y <= DIAMETER; y++) {
				Color c2 = new Color(AMBIENT_RED, AMBIENT_GREEN, AMBIENT_BLUE, getAlpha(x, y));
				g.setColor(c2);
				g.drawLine(x + pos_x, y + pos_y, x + pos_x, y + pos_y);
			}
		}

	}

	/**
	 * Returns a properly scaled integer based on the distance that a point is
	 * from the center of the light
	 * 
	 * @param x1
	 * @param y1
	 * @return integer between 0 and 255 indicating the alpha level for that
	 *         point
	 */
	private int getAlpha(int x1, int y1) {
		int dx = x1 - RADIUS;
		int dy = y1 - RADIUS;

		double distance = Math.sqrt(dx * dx + dy * dy);
		return Math.min(AMBIENT_ALPHA, (int) ((distance / RADIUS) * 255));

	}

	/**
	 * Keeps the light centered on the square's position
	 */
	@Override
	public void tick() {
		pos_x = targetSquare.pos_x - RADIUS + Square.SIZE / 2;
		pos_y = targetSquare.pos_y - RADIUS + Square.SIZE / 2;
	}

}
