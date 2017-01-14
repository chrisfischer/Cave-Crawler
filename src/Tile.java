

import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Tile extends GameObj implements Comparable<Tile> {

	public static final int SIZE = 52;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	public final int INIT_X;
	public final int INIT_Y;

	public final BufferedImage img;
	public final boolean isDestroyable;

	public Tile(int initX, int initY, boolean isDestroyable, BufferedImage img) {
		super(INIT_VEL_X, INIT_VEL_Y, initX, initY, SIZE, SIZE, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);

		INIT_X = initX;
		INIT_Y = initY;

		this.img = img;
		this.isDestroyable = isDestroyable;

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, INIT_X, INIT_Y, SIZE, SIZE, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + INIT_X;
		result = prime * result + INIT_Y;
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
		Tile other = (Tile) obj;
		if (INIT_X != other.INIT_X)
			return false;
		if (INIT_Y != other.INIT_Y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Tile t) {
		if (INIT_X > t.INIT_X) {
			return 1;
		} else if (INIT_X < t.INIT_X) {
			return -1;
		} else if (INIT_Y > t.INIT_Y) {
			return 1;
		} else if (INIT_Y < t.INIT_Y) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public void tick() {

	}

}
