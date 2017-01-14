
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EndPanel {
	private final JPanel endGameHolder;
	private final JLabel timeSurvived;
	private final JLabel roomsSurvived;
	private final JLabel scoreEnd;
	private final JTextField username;
	private final JButton save;

	private String time;
	private int score;
	private int rooms;

	public EndPanel() {

		endGameHolder = new JPanel();
		endGameHolder.setLayout(new BoxLayout(endGameHolder, BoxLayout.Y_AXIS));
		endGameHolder.setBackground(Color.BLACK);

		final JLabel gameOver = makeWhiteLabel("GAME OVER", 70f);
		gameOver.setAlignmentX(Component.CENTER_ALIGNMENT);
		endGameHolder.add(gameOver);

		timeSurvived = makeWhiteLabel("", 20f);
		timeSurvived.setAlignmentX(Component.CENTER_ALIGNMENT);
		endGameHolder.add(timeSurvived);

		roomsSurvived = makeWhiteLabel("", 20f);
		roomsSurvived.setAlignmentX(Component.CENTER_ALIGNMENT);
		endGameHolder.add(roomsSurvived);

		scoreEnd = makeWhiteLabel("Score: 0", 20f);
		scoreEnd.setAlignmentX(Component.CENTER_ALIGNMENT);
		endGameHolder.add(scoreEnd);

		username = new JTextField("Username", 20);
		username.setFont(FileManager.pixeled);
		final JPanel usernameContainer = new JPanel();
		usernameContainer.setBackground(Color.BLACK);
		usernameContainer.add(username);
		endGameHolder.add(usernameContainer);

		save = new JButton("SAVE HIGHSCORE");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save(username.getText().replaceAll("\\s", ""), time, rooms, score);
				save.setVisible(false);
				username.setVisible(false);
			}
		});
		save.setFont(FileManager.pixeled);
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		endGameHolder.add(save);

	}

	/**
	 * Updates the score label
	 * 
	 * @param score
	 */
	public void setEndScore(int score) {
		scoreEnd.setText("Score: " + score);
		this.score = score;
	}

	/**
	 * Updates the room survived label
	 * 
	 * @param rooms
	 */
	public void setRoomsSurvived(int rooms) {
		roomsSurvived.setText("Rooms Survived: " + rooms);
		this.rooms = rooms;
	}

	/**
	 * Updates the time survived label with the formatted time
	 * 
	 * @param time
	 */
	public void setTimeSurvived(int time) {
		int minutes = (time / 1000) / 60;
		int seconds = (time / 1000) - (minutes * 60);
		String minutesText;
		String secondsText;
		if (minutes < 10) {
			minutesText = "0" + minutes;
		} else {
			minutesText = new Integer(minutes).toString();
		}
		if (seconds < 10) {
			secondsText = "0" + seconds;
		} else {
			secondsText = new Integer(seconds).toString();
		}
		timeSurvived.setText("Time Survived: " + minutesText + ":" + secondsText);
		this.time = minutesText + ":" + secondsText;
	}
	
	/**
	 * Updates the high score file with the new entry
	 * 
	 * @param username
	 * @param time
	 * @param rooms
	 * @param score
	 */
	private void save(String username, String time, int rooms, int score) {
		try {
			FileWriter fw = new FileWriter("misc/highscores.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.write("\n" + username + " " + time + " " + rooms + " " + score);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("High score file was not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in high score file.");
			e.printStackTrace();
		}
	}

	public JPanel getPanel() {
		return endGameHolder;
	}

	public void setSaveButtonVisible(boolean flag) {
		save.setVisible(flag);
	}

	public void setUsernameFieldVisible(boolean flag) {
		username.setVisible(flag);
	}

	private static JLabel makeWhiteLabel(String text, float size) {
		JLabel label = new JLabel(text);
		Font labelFont = FileManager.pixeled.deriveFont(size);
		label.setFont(labelFont);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.BLACK);
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}
}
