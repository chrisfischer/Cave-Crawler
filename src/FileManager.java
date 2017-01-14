
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class FileManager {

	public static final int NUM_OF_MAPS = 10;

	public static BufferedImage removable_wall_img;
	public static BufferedImage unremovable_wall_img;
	public static BufferedImage background;
	public static BufferedImage walker;
	public static BufferedImage heart;
	public static BufferedImage pickaxe;
	public static BufferedImage machineGun;
	public static BufferedImage sniper;

	public static Map<Integer, Tuple<Set<Tile>, Set<Coin>>> maps;

	public static Font pixeled;

	public FileManager() {

		maps = new TreeMap<Integer, Tuple<Set<Tile>, Set<Coin>>>();
		try {
			removable_wall_img = ImageIO.read(new File("images/removableWall.png"));
			unremovable_wall_img = ImageIO.read(new File("images/unremovableWall.png"));
			background = ImageIO.read(new File("images/background.jpg"));
			walker = ImageIO.read(new File("images/walker.png"));
			heart = ImageIO.read(new File("images/heart.png"));
			pickaxe = ImageIO.read(new File("images/pickaxe.png"));
			machineGun = ImageIO.read(new File("images/machinegun.png"));
			sniper = ImageIO.read(new File("images/sniper.png"));

			pixeled = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/8-Bit.ttf"));
			pixeled = pixeled.deriveFont(20f);

		} catch (IOException e) {
			System.out.println("Internal Error: " + e.getMessage());
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		// populates the map data structures
		try {
			BufferedReader reader = new BufferedReader(new FileReader("misc/maps.txt"));
			for (int i = 0; i < NUM_OF_MAPS; i++) {
				maps.put(i, readMap(reader, i));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Map File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in Map File");
			e.printStackTrace();
		}

	}

	/**
	 * Reads the map file and returns the proper configurations of tiles and
	 * coins for the level that the file reader is currently on
	 * 
	 * @param reader
	 * @param mapNumber
	 * @return
	 */
	public Tuple<Set<Tile>, Set<Coin>> readMap(BufferedReader reader, int mapNumber) {
		Set<Tile> tileAccum = new TreeSet<Tile>();
		Set<Coin> coinAccum = new TreeSet<Coin>();
		try {
			for (int y = mapNumber * TileMap.SIZE; y < (mapNumber + 1) * TileMap.SIZE; y++) {
				String line = reader.readLine();
				String[] arr = line.split(" ");
				for (int x = 0; x < arr.length; x++) {
					if (arr[x].equals("-1"))
						throw new IOException();
					if (arr[x].equals("1"))
						tileAccum.add(new Tile(x * Tile.SIZE, (y - mapNumber * TileMap.SIZE) * Tile.SIZE, false,
								FileManager.unremovable_wall_img));
					if (arr[x].equals("2"))
						tileAccum.add(new Tile(x * Tile.SIZE, (y - mapNumber * TileMap.SIZE) * Tile.SIZE, true,
								FileManager.removable_wall_img));
					if (arr[x].equals("3"))
						coinAccum.add(new Coin(x * Tile.SIZE + (Tile.SIZE - Coin.SIZE) / 2,
								(y - mapNumber * TileMap.SIZE) * Tile.SIZE + (Tile.SIZE - Coin.SIZE) / 2,
								Coin.SMALL_COIN_VALUE, Color.YELLOW));
					if (arr[x].equals("4"))
						coinAccum.add(new Coin(x * Tile.SIZE + (Tile.SIZE - Coin.SIZE) / 2,
								(y - mapNumber * TileMap.SIZE) * Tile.SIZE + (Tile.SIZE - Coin.SIZE) / 2,
								Coin.LARGE_COIN_VALUE, Color.CYAN));
				}
			}
		} catch (IOException e) {
			System.out.println("Error in Map File Format");
			e.printStackTrace();
		}
		return new Tuple<Set<Tile>, Set<Coin>>(tileAccum, coinAccum);
	}

}
