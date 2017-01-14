
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// Game constants
	public static final int COURT_WIDTH = TileMap.SIZE * Tile.SIZE;
	public static final int COURT_HEIGHT = TileMap.SIZE * Tile.SIZE;
	public static final int INTERVAL = 35;

	private ObjController objController;
	private HeartsFrame heartsFrame;
	private WeaponBagFrame weaponBagFrame;

	// Game objects
	private Square square;
	private MobSpawner mobSpawner;
	private TileMap tileMap;
	private WeaponBag weaponBag;
	private Light light;

	// Input
	public Input input;

	private JLabel status; // Current status text (i.e. Running...)
	private JLabel scoreLabel; // Current score text
	private JLabel roomCountLabel; // Current room count text
	private JPanel container; // contains all the cards of different screens to
								// switch to end screen when game is over
	private EndPanel endPanel; // panel that is to be updated at end of game

	// Whether lighting is to be rendered
	private boolean lighting = true;
	
	private boolean playing = false; // whether the game is running
	private boolean paused = false; // whether the game is paused
	
	public static int time;

	public GameCourt(JLabel status, JLabel scoreLabel, JLabel roomCountLabel, EndPanel endPanel, JPanel container) {

		objController = new ObjController();

		// Adds the key listener to this instance of GameCourt
		input = new Input(this);

		// Creates the displays of health and current weapon
		heartsFrame = new HeartsFrame(Square.MAX_HEALTH);
		weaponBagFrame = new WeaponBagFrame(WeaponBag.ORIGINAL_WEAPON);

		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playing && !paused) {
					tick();
					
				}
			}
		});
		timer.start();

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		this.status = status;
		this.scoreLabel = scoreLabel;
		this.roomCountLabel = roomCountLabel;
		this.container = container;
		this.endPanel = endPanel;

	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		objController.clear();

		// reset all other objects
		tileMap = new TileMap();
		square = new Square(tileMap);
		mobSpawner = new MobSpawner(square, tileMap, "STANDARD");
		weaponBag = new WeaponBag(square, tileMap, mobSpawner);
		if (lighting) {
			light = new Light(square);
		}

		// adds objects to the object controller so they are properly rendered
		// and ticked
		objController.addGameObj(tileMap);
		objController.addGameObj(weaponBag);
		objController.addGameObj(mobSpawner);
		if (lighting) {
			objController.addGameObj(light);
		}
		objController.addGameObj(square);

		// reset health meter
		heartsFrame.reset();

		// resets ToolBag
		weaponBagFrame.reset();

		// reset score
		square.resetScore();
		scoreLabel.setText(new Integer(square.getScore()).toString());

		// reset room count
		square.resetRoomCount();
		roomCountLabel.setText(new Integer(square.getRoomCount()).toString());

		// reset label and playing flag
		playing = true;
		paused = false;
		status.setText("Running...");
		this.setVisible(true);
			
		// resets the timer
		time = 0;

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	public void tick() {
		// ticks all of the game objects
		objController.tick();
		// updates the input
		input.tick();

		// updates the score and room counters
		scoreLabel.setText(new Integer(square.getScore()).toString());
		roomCountLabel.setText(new Integer(square.getRoomCount()).toString());

		// updates the health and current weapon display
		heartsFrame.setHealth(square.getCurrHealth());
		weaponBagFrame.setCurrWeapon(weaponBag.getCurrWeapon());

		// when square hits a boundary, calls for the MobSpawner to reset
		if (square.isNewMap()) {
			mobSpawner.reset(square.getRoomCount());
			square.setNewMap(false);
		}

		// ends the game if player health becomes 0
		if (square.getCurrHealth() <= 0) {
			status.setText("GAME OVER");
		    playing = false;
			CardLayout cl = (CardLayout) (container.getLayout());
			cl.show(container, "ENDPANEL");

			endPanel.setEndScore(square.getScore());
			endPanel.setRoomsSurvived(square.getRoomCount());
			endPanel.setTimeSurvived(time);
			endPanel.setUsernameFieldVisible(true);
			endPanel.setSaveButtonVisible(true);
		}
		
		// repaints all of the frames
		repaint();
		heartsFrame.repaint();
		weaponBagFrame.repaint();
		
		// updates the time field
		time = time + GameCourt.INTERVAL;

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(FileManager.background, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
		objController.draw(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

	public HeartsFrame getHearts() {
		return heartsFrame;
	}

	public WeaponBagFrame getWeaponBagFrame() {
		return weaponBagFrame;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isLighting() {
		return lighting;
	}

	public void setLighting(boolean lighting) {
		this.lighting = lighting;
	}
	
	/**
	 * Sets the player's health to zero. Used to end the game early
	 */
	public void zeroHealth() {
		square.zeroHealth();
	}

}
