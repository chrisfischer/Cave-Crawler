
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HeartsFrame extends JPanel {

	public static final int HEART_SIZE = 40;
	public static final int WIDTH = HEART_SIZE;
	public static final int HEIGHT = HEART_SIZE * 4;

	private int maxHealth;
	private int currHealth;

	public HeartsFrame(int maxHealth) {

		// starts the player off with full health
		this.maxHealth = maxHealth;
		currHealth = maxHealth;
	}

	public void reset() {
		// resets the health back to full
		currHealth = maxHealth;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 4; i++) {
			g.drawImage(FileManager.heart, (getWidth() - WIDTH) / 2, HEART_SIZE * i, HEART_SIZE, HEART_SIZE, null);
		}

		// black rectangle makes a certain amount of the hearts "disappear"
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), HEIGHT - ((HEIGHT * currHealth) / maxHealth));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), HEIGHT);
	}

	public void setHealth(int health) {
		currHealth = health;
	}

	public int getHealth() {
		return currHealth;
	}

}
