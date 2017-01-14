
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class WeaponBag extends GameObj {

	public final static int SNIPER_POWER = 10;
	public final static int SNIPER_RATE_OF_FIRE = 600; // in milliseconds
	public final static int MACHINEGUN_POWER = 2;
	public final static int MACHINEGUN_RATE_OF_FIRE = 90; // in milliseconds

	public static final String ORIGINAL_WEAPON = "SNIPER";

	private Map<String, Weapon> weapon;
	private String currWeapon;

	private Square parentSquare;
	private TileMap tileMap;
	private MobSpawner mobSpawner;

	public WeaponBag(Square s, TileMap tileMap, MobSpawner mobSpawner) {
		super(0, 0, 0, 0, 0, 0, 0, 0);

		parentSquare = s;
		this.tileMap = tileMap;
		this.mobSpawner = mobSpawner;

		weapon = new TreeMap<String, Weapon>();

		weapon.put("SNIPER", new Weapon(SNIPER_POWER, SNIPER_RATE_OF_FIRE, s));
		weapon.put("MACHINEGUN", new Weapon(MACHINEGUN_POWER, MACHINEGUN_RATE_OF_FIRE, s));
		weapon.put("PICKAXE", new Weapon(0, 0, null));

		currWeapon = ORIGINAL_WEAPON;

	}

	@Override
	public void draw(Graphics g) {
		// draws the bullets for each weapon
		for (Weapon w : weapon.values()) {
			w.draw(g);
		}
	}

	@Override
	public void tick() {

		// changes the current weapon according to user key input
		if (Input.isKeyPressed(KeyEvent.VK_1)) {
			currWeapon = "SNIPER";
		} else if (Input.isKeyPressed(KeyEvent.VK_2)) {
			currWeapon = "MACHINEGUN";
		} else if (Input.isKeyPressed(KeyEvent.VK_3)) {
			currWeapon = "PICKAXE";
		}

		// does the appropriate action if space bar is pressed
		if (currWeapon.equals("MACHINEGUN")) {
			// enables player to hold down space bar for the machine gun
			if (Input.isKey(KeyEvent.VK_SPACE)) {
				getCurrentWeaponObject().dealDamage(parentSquare.getCurrDirection(), tileMap, mobSpawner);
			}
		}
		if (Input.isKeyPressed(KeyEvent.VK_SPACE)) {
			if (currWeapon.equals("SNIPER")) {
				getCurrentWeaponObject().dealDamage(parentSquare.getCurrDirection(), tileMap, mobSpawner);
			} else if (currWeapon.equals("PICKAXE")) {
				Tile t = parentSquare.intersectsTileMap(tileMap);
				if (t != null && t.isDestroyable) {
					tileMap.removeTile(t);
				}
			}
		}

		// ticks each weapon (moves their bullets)
		for (Weapon w : weapon.values()) {
			w.tick();
		}

	}

	public Weapon getCurrentWeaponObject() {
		return weapon.get(currWeapon);
	}

	public String getCurrWeapon() {
		return currWeapon;
	}

}
