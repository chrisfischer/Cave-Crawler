

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Coin extends GameObj implements Comparable<Coin> {

	public static final int SIZE = 20;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int SMALL_COIN_VALUE = 7;
	public static final int LARGE_COIN_VALUE = 15;

	private final int coinValue;
	private final Color c;

	// used for the spinning coin animation
	private boolean increasing = false;

	public Coin(int initX, int initY, int coinValue, Color c) {
		super(INIT_VEL_X, INIT_VEL_Y, initX, initY, SIZE, SIZE, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		this.coinValue = coinValue;
		this.c = c;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(c);
		g.fillOval(pos_x, pos_y, width, height);
		g.setColor(Color.BLACK);
		g.drawOval(pos_x, pos_y, width, height);
	}

	@Override
	public void tick() {

		// makes the coin appear to be spinning
		if (width <= SIZE && width > 0 && !increasing) {
			width = width - 2;
			pos_x++;
		} else if (width < SIZE) {
			increasing = true;
			width = width + 2;
			pos_x--;
		} else if (width == SIZE) {
			increasing = false;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pos_x;
		result = prime * result + pos_y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coin other = (Coin) obj;
		if (pos_x != other.pos_x)
			return false;
		if (pos_y != other.pos_y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Coin c) {
		if (pos_x > c.pos_x) {
			return 1;
		} else if (pos_x < c.pos_x) {
			return -1;
		} else if (pos_y > c.pos_y) {
			return 1;
		} else if (pos_y < c.pos_y) {
			return -1;
		} else {
			return 0;
		}
	}

	public int getCoinValue() {
		return coinValue;
	}

}
