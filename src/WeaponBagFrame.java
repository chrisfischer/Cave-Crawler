
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WeaponBagFrame extends JPanel {

	public static final int FRAME_SIZE = 40;

	private String originalWeapon;
	private String currWeapon;

	public WeaponBagFrame(String originalTool) {
		this.originalWeapon = originalTool;
		currWeapon = originalTool;
	}

	public void reset() {
		currWeapon = originalWeapon;
	}

	@Override
	public void paintComponent(Graphics g) {

		if (currWeapon.equals("SNIPER")) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect((getWidth() - FRAME_SIZE) / 2, (getHeight() - 160) / 2 + 10, FRAME_SIZE, FRAME_SIZE);
		g.drawImage(FileManager.sniper, (getWidth() - FRAME_SIZE) / 2 + 5, (getHeight() - 160) / 2 + 10 + 13, 30, 14,
				null);

		if (currWeapon.equals("MACHINEGUN")) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect((getWidth() - FRAME_SIZE) / 2, (getHeight() - 160) / 2 + FRAME_SIZE + 20, FRAME_SIZE, FRAME_SIZE);
		g.drawImage(FileManager.machineGun, (getWidth() - FRAME_SIZE) / 2 + 5,
				(getHeight() - 160) / 2 + FRAME_SIZE + 20 + 13, 30, 14, null);

		if (currWeapon.equals("PICKAXE")) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect((getWidth() - FRAME_SIZE) / 2, (getHeight() - 160) / 2 + 2 * FRAME_SIZE + 30, FRAME_SIZE,
				FRAME_SIZE);
		g.drawImage(FileManager.pickaxe, (getWidth() - FRAME_SIZE) / 2 + 5,
				(getHeight() - 160) / 2 + 2 * FRAME_SIZE + 30 + 6, null);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(FRAME_SIZE + 20, FRAME_SIZE * 3 + 40);
	}

	public String getCurrTool() {
		return currWeapon;
	}

	public void setCurrWeapon(String currWeapon) {
		this.currWeapon = currWeapon;
	}

}
