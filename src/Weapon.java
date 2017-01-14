

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Weapon {

	private int power;
	private int rateOfFire;
	private int timeOfLastFire;

	private Square parentSquare;
	private List<Bullet> bullets;

	public Weapon(int power, int rateOfFire, Square parentSquare) {
		bullets = new LinkedList<Bullet>();
		this.power = power;
		this.rateOfFire = rateOfFire;
		this.parentSquare = parentSquare;
		
		// so player can fire immediately when the game starts
		timeOfLastFire = -rateOfFire;
	}

	public void dealDamage(Direction d, TileMap tileMap, MobSpawner mobSpawner) {
		// creates a new bullet and centers it with respect to the square it
		// comes from
		if (timeOfLastFire + rateOfFire < GameCourt.time) {
			bullets.add(new Bullet(parentSquare.pos_x + (Square.SIZE - Bullet.getWidth(d)) / 2,
					parentSquare.pos_y + (Square.SIZE - Bullet.getHeight(d)) / 2, power,
					parentSquare.getCurrDirection(), tileMap, mobSpawner));
			timeOfLastFire = GameCourt.time;
		}
	}

	public void tick() {
		// removes all bullets from the list that have a zero velocity and ticks
		// the rest
		Iterator<Bullet> iter = bullets.iterator();
		while (iter.hasNext()) {
			Bullet currBull = iter.next();
			if (currBull.v_x == 0 & currBull.v_y == 0) {
				iter.remove();
			} else {
				currBull.tick();
			}
		}

	}

	public void draw(Graphics g) {
		// draws all bullets in the updated list
		for (Bullet b : bullets) {
			b.draw(g);
		}
	}

}
