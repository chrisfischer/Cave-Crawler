

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {

	private static boolean[] keys = new boolean[256];
	private static boolean[] keysLast = new boolean[256];

	public Input(GameCourt gc) {
		gc.addKeyListener(this);
	}

	public void tick() {
		keysLast = keys.clone();
	}

	public static boolean isAnyKeyPressed() {
		for (boolean b : keys) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKey(int keyCode) {
		return keys[keyCode];
	}

	// pressed is true, not pressed is false
	public static boolean isKeyPressed(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}

	public static boolean isKeyReleased(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
