@SuppressWarnings("serial")
public abstract class Mob extends GameObj {

	// Interval that a mob must wait before it can deal damage to the player
	// again (in milliseconds)
	public static final int HIT_INTERVAL = 150;

	private int currHealth;
	private int power;
	private int pointsWorth;
	private Square targetSquare;

	public int timeOfLastHit;

	public Mob(int velX, int velY, int initX, int initY, int width, int height, int maxHealth, int power,
			int pointsWorth, Square targetSquare) {
		super(velX, velY, initX, initY, width, height, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		currHealth = maxHealth;
		this.power = power;
		this.pointsWorth = pointsWorth;
		this.targetSquare = targetSquare;
		
		// so mobs can deal damage immediatly after the game starts
		timeOfLastHit = -HIT_INTERVAL;
	}

	@Override
	public void tick() {
		mover();
		if (intersects(targetSquare) && timeOfLastHit + HIT_INTERVAL <= GameCourt.time) {
			targetSquare.damage(power);
			timeOfLastHit = GameCourt.time;
		}
	}

	/**
	 * Subclasses must override this method to specify how the mob should be
	 * moved whenever the game is updated
	 */
	public abstract void mover();

	public int getCurrHealth() {
		return currHealth;
	}

	public void reduceHealth(int power) {
		currHealth = currHealth - power;
	}

	public int getHitPower() {
		return power;
	}

	public int getPointsWorth() {
		return pointsWorth;
	}

}
