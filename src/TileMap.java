
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class TileMap extends GameObj {

	public static final int SIZE = 11; // size of the map array

	private Set<Tile> currTiles;
	private Set<Coin> currCoins;
	private int currMapNum;

	public TileMap() {
		super(0, 0, 0, 0, 0, 0, 0, 0);

		// sets the first map to 0
		currMapNum = 0;
		currTiles = new TreeSet<Tile>(FileManager.maps.get(currMapNum).getFirst());
		currCoins = new TreeSet<Coin>(FileManager.maps.get(currMapNum).getSecond());
	}

	@Override
	public void draw(Graphics g) {
		for (Tile t : currTiles) {
			t.draw(g);
		}
		for (Coin c : currCoins) {
			c.draw(g);
		}
	}

	@Override
	public void tick() {
		for (Coin c : currCoins) {
			c.tick();
		}
	}

	/**
	 * Resets the current tile and coin objects with a random map
	 * 
	 * @param initX
	 * @param initY
	 */
	public void reset(int initX, int initY) {
		// maps with a horizontal entrance
		int[] mapsWithHorizontal = { 0, 1, 2, 3, 4, 5, 6, 9 };
		// maps with a vertical entrance
		int[] mapsWithVertical = { 0, 1, 2, 3, 5, 6, 7, 8 };

		Random rand = new Random();
		int choice = 0;
		if (initX == 0 || initX == SIZE - 1) {
			do {
				choice = mapsWithHorizontal[rand.nextInt(mapsWithHorizontal.length)];
			} while (choice == currMapNum);
		} else if (initY == 0 || initY == SIZE - 1) {
			do {
				choice = mapsWithVertical[rand.nextInt(mapsWithVertical.length)];
			} while (choice == currMapNum);
		}
		currTiles = new TreeSet<Tile>(FileManager.maps.get(choice).getFirst());
		currCoins = new TreeSet<Coin>(FileManager.maps.get(choice).getSecond());
		currMapNum = choice;

	}

	/**
	 * Takes in GameCourt coordinates and determines whether there is a tile
	 * object there
	 * 
	 * @param x
	 * @param y
	 * @return Whether there is a tile object in that location
	 */
	public boolean isWall(int x, int y) {
		for (Tile t : currTiles) {
			if (t.INIT_X / Tile.SIZE == x && t.INIT_Y / Tile.SIZE == y) {
				return true;
			}
		}
		return false;

	}

	public void removeTile(Tile t) {
		Iterator<Tile> iter = currTiles.iterator();
		while (iter.hasNext()) {
			Tile currTile = iter.next();
			if (currTile.equals(t)) {
				iter.remove();
			}
		}

	}

	public void removeCoin(Coin c) {
		Iterator<Coin> iter = currCoins.iterator();
		while (iter.hasNext()) {
			Coin currCoin = iter.next();
			if (currCoin.equals(c)) {
				iter.remove();
			}
		}

	}

	public Set<Tile> getCurrTiles() {
		return currTiles;
	}

	public Set<Coin> getCurrCoins() {
		return currCoins;
	}

	public int getCurrMapNum() {
		return currMapNum;
	}

}
