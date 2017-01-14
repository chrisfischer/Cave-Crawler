
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class MobSpawner extends GameObj {

	public static final int NUM_OF_WALKER = 2;
	public static final int NUM_OF_FLYER = 1;
	public static final int NUM_OF_FOLLOWER = 2;

	private List<Mob> mobs;
	private TileMap map;
	private Square targetSquare;

	public MobSpawner(Square targetSquare, TileMap map, String mode) {
		super(0, 0, 0, 0, 0, 0, 0, 0);
		this.targetSquare = targetSquare;
		this.map = map;
		mobs = new LinkedList<Mob>();

		// mode that spawns in the standard start configuration
		// (to be changed for testing purposes)
		if (mode.equals("STANDARD")) {
			// starting number of enemies
			if (map.getCurrMapNum() == 6) {
				mobs.addAll(Follower.spawn(2, map, targetSquare));
			} else {
				mobs.addAll(Walker.spawn(2, map, targetSquare));
				mobs.addAll(Flyer.spawn(1, map, targetSquare));
			}
		}

	}

	/**
	 * Resets the list of current mobs depending on how many rooms have already
	 * been passed (ie. difficulty)
	 * 
	 * @param difficulty
	 */
	public void reset(int difficulty) {
		mobs = new LinkedList<Mob>();

		if (map.getCurrMapNum() == 6) {
			mobs.addAll(Follower.spawn(NUM_OF_FOLLOWER + difficulty / 2, map, targetSquare));
		} else {
			mobs.addAll(Walker.spawn(NUM_OF_WALKER + difficulty, map, targetSquare));
			mobs.addAll(Flyer.spawn(NUM_OF_FLYER + difficulty / 2, map, targetSquare));
		}
	}

	@Override
	public void draw(Graphics g) {
		for (Mob m : mobs) {
			m.draw(g);
		}
	}

	@Override
	public void tick() {
		// removes all mobs that have zero heath and ticks the rest
		Iterator<Mob> iter = mobs.iterator();
		while (iter.hasNext()) {
			Mob currMob = iter.next();
			if (currMob.getCurrHealth() <= 0) {
				targetSquare.incrScore(currMob.getPointsWorth());
				iter.remove();
			} else {
				currMob.tick();
			}
		}
	}

	public List<Mob> getMobs() {
		return mobs;
	}

	public void addMob(Mob mob) {
		mobs.add(mob);
	}

	public boolean isMobListEmpty() {
		return mobs.isEmpty();
	}

}
