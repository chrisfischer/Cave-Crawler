
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {

	FileManager files = new FileManager();

	public void run() {
		// NOTE : recall that the 'final' keyword notes immutability
		// even for local variables.

		//================================================================================
	    // Top Level Frame
	    //================================================================================

		final JFrame topLevel = new JFrame("Cave Crawler");
		topLevel.setResizable(false);
		topLevel.setLocation(300, 300);

		final JPanel gamePanel = new JPanel(new BorderLayout());
		final JPanel startPanel = new JPanel(new BorderLayout());
		final JPanel infoPanel = new JPanel(new BorderLayout());
		final JPanel endPanel = new JPanel(new BorderLayout());

		JPanel container = new JPanel(new CardLayout());
		container.add(startPanel, "STARTPANEL");
		container.add(gamePanel, "GAMEPANEL");
		container.add(infoPanel, "INFOPANEL");
		container.add(endPanel, "ENDPANEL");

		//================================================================================
	    // Status Panel
	    //================================================================================

		final JPanel status_panel = new JPanel();
		gamePanel.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Running...");
		status.setForeground(Color.WHITE);
		status.setFont(FileManager.pixeled);
		status_panel.setBackground(Color.BLACK);
		status_panel.add(status);

		// Score Label
		final JLabel scoreTitle = makeWhiteLabel(" SCORE ", 20f);
		final JLabel score = makeWhiteLabel("0", 20f);

		final JLabel roomCountTitle = makeWhiteLabel(" ROOMS ", 20f);
		final JLabel roomCount = makeWhiteLabel("0", 20f);

		final JPanel scoreFrame = new JPanel(new GridLayout(5, 1));
		scoreFrame.setBackground(Color.BLACK);
		scoreFrame.add(scoreTitle);
		scoreFrame.add(score);
		scoreFrame.add(new JLabel()); // creates a spacer between the two
										// counters
		scoreFrame.add(roomCountTitle);
		scoreFrame.add(roomCount);

		//================================================================================
	    // End Game Panel
	    //================================================================================

		EndPanel endGameHolder = new EndPanel();
		final JPanel endGameUpperPanel = endGameHolder.getPanel();
		endPanel.add(endGameUpperPanel, BorderLayout.NORTH);
		endPanel.setBackground(Color.BLACK);

		//================================================================================
	    // GameCourt Panel
	    //================================================================================

		final GameCourt court = new GameCourt(status, score, roomCount, endGameHolder, container);
		gamePanel.add(court, BorderLayout.CENTER);

		//================================================================================
	    // Start Panel
	    //================================================================================
		
		startPanel.setBackground(Color.BLACK);

		StartPanel startPanelHolder = new StartPanel(container, court);
		final JPanel startPanelUpper = startPanelHolder.getPanel();
		startPanel.add(startPanelUpper);
		final JPanel startPanelLower = new JPanel(new BorderLayout());
		startPanelLower.setBackground(Color.BLACK);
		final JCheckBox lighting = new JCheckBox("Lighting", true);
		lighting.setFont(FileManager.pixeled);
		lighting.setForeground(Color.WHITE);
		lighting.setBackground(Color.BLACK);
		lighting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				court.setLighting(!court.isLighting());	
			}
		});
		final JPanel lightingPanel = new JPanel(new FlowLayout());
		lightingPanel.setBackground(Color.BLACK);
		lightingPanel.add(lighting);
		startPanelLower.add(lightingPanel, BorderLayout.NORTH);
		final JLabel credits = Game.makeWhiteLabel("By Chris Fischer, 2016", 15f);
		startPanelLower.add(credits, BorderLayout.SOUTH);
		startPanel.add(startPanelLower, BorderLayout.SOUTH);

		// adds functionality to the buttons button in the end game panel (need
		// to access the game court and start panel)
		final JPanel endGameLowerPanel = new JPanel(new BorderLayout());
		endGameLowerPanel.setBackground(Color.BLACK);
		endPanel.add(endGameLowerPanel, BorderLayout.SOUTH);
		final JButton restart = new JButton("Play Again");
		restart.setFont(FileManager.pixeled);
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (container.getLayout());
				cl.show(container, "GAMEPANEL");
				court.grabFocus();
				court.reset();
			}
		});
		endGameLowerPanel.add(restart, BorderLayout.NORTH);

		final JButton backToStart = new JButton("BACK TO START SCREEN");
		backToStart.setFont(FileManager.pixeled);
		backToStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (container.getLayout());
				cl.show(container, "STARTPANEL");
				startPanelHolder.refreshHighScores();
				startPanel.grabFocus();
			}
		});
		endGameLowerPanel.add(backToStart, BorderLayout.SOUTH);

		//================================================================================
	    // Play Info Panel
	    //================================================================================

		final JPanel player_panel = new JPanel(new BorderLayout());
		player_panel.setBackground(Color.BLACK);
		gamePanel.add(player_panel, BorderLayout.EAST);

		final WeaponBagFrame tools = court.getWeaponBagFrame();
		tools.setBackground(Color.BLACK);
		final HeartsFrame hearts = court.getHearts();
		hearts.setBackground(Color.BLACK);

		player_panel.add(scoreFrame, BorderLayout.NORTH);
		player_panel.add(tools, BorderLayout.CENTER);
		player_panel.add(hearts, BorderLayout.SOUTH);

		final JButton quit = new JButton("QUIT");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.zeroHealth();
				court.setPaused(false);
			}
		});
		quit.setFont(FileManager.pixeled);
		status_panel.add(quit);
		final JButton pause = new JButton("PAUSE");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setPaused(!court.isPaused());
				switchStatus(status);
				court.grabFocus();
			}
		});
		pause.setFont(FileManager.pixeled);
		status_panel.add(pause);
		

		//================================================================================
	    // Instructions Panel
	    //================================================================================

		infoPanel.setBackground(Color.WHITE);

		final JLabel infoTitle = makeBlackLabel("INSTRUCTIONS", 45f);
		infoPanel.add(infoTitle, BorderLayout.NORTH);

		final JButton backButton = new JButton("BACK");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (container.getLayout());
				cl.show(container, "STARTPANEL");
				startPanel.grabFocus();
			}
		});
		backButton.setFont(FileManager.pixeled);

		final JTextArea instructions = new JTextArea();
		instructions.setText(
				"A cube of light finds itself thrust into a ferocious, cold world. It needs your help to survive: don't "
						+ "let it's light go out.\n\n"
						+ "To move throughout the map, use arrow keys. Colliding with monsters will reduce your health, "
						+ "which can be seen in the lower right corner. To fire bullets in your current direction, "
						+ "press SPACE. To select from your range of weapons and tools, use the number keys 1 - 3. Mossy "
						+ "walls can be picked away using the right tool. You can move on the next level at any time by "
						+ "going to edge of the map, but beware: the more rooms you travel through, the more monsters"
						+ "will come. You can pick up coins along the way to try to reach a high score!"
						+ "\n\nPoint Values:" + "\nWalking Monster: " + Walker.POINTS_WORTH + "\nFlying Monster: "
						+ Flyer.POINTS_WORTH + "\nFollowing Monster: " + Follower.POINTS_WORTH + "\nYellow Coins: "
						+ Coin.SMALL_COIN_VALUE + "\nBlue Coins: " + Coin.LARGE_COIN_VALUE);
		instructions.setLineWrap(true);
		instructions.setWrapStyleWord(true);
		instructions.setEditable(false);
		instructions.setFont(FileManager.pixeled);
		instructions.setMargin(new Insets(20, 20, 20, 20));

		infoPanel.add(instructions, BorderLayout.CENTER);
		infoPanel.add(backButton, BorderLayout.SOUTH);

		topLevel.add(container);

		// Put the frame on the screen
		topLevel.pack();
		topLevel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		topLevel.setVisible(true);

	}

	public void switchStatus(JLabel status) {
		if (status.getText().equalsIgnoreCase("RUNNING...")) {
			status.setText("PAUSED");
		} else if (status.getText().equalsIgnoreCase("PAUSED")) {
			status.setText("RUNNING...");
		}
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

	public static JLabel makeLabel(String text, float size, Color fgColor, Color bgColor) {
		JLabel label = new JLabel(text);
		Font labelFont = FileManager.pixeled.deriveFont(size);
		label.setFont(labelFont);
		label.setForeground(fgColor);
		label.setBackground(bgColor);
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}

	public static JLabel makeBlackLabel(String text, float size) {
		return makeLabel(text, size, Color.BLACK, Color.WHITE);
	}

	public static JLabel makeWhiteLabel(String text, float size) {
		return makeLabel(text, size, Color.WHITE, Color.BLACK);
	}
}
