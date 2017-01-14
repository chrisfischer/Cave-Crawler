
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StartPanel {

	private final JPanel startPanelHolder;
	private final JPanel highScores;

	public StartPanel(JPanel container, GameCourt court) {

		startPanelHolder = new JPanel();
		startPanelHolder.setLayout(new BoxLayout(startPanelHolder, BoxLayout.Y_AXIS));
		startPanelHolder.setBackground(Color.BLACK);

		final JLabel title = makeWhiteLabel("CAVE CRAWLER", 70f);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		startPanelHolder.add(title);

		final JButton startButton = new JButton("NEW GAME");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (container.getLayout());
				cl.show(container, "GAMEPANEL");
				court.grabFocus();
				court.reset();
			}
		});
		startButton.setFont(FileManager.pixeled);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		final JButton infoButton = new JButton("INSTRUCTIONS");
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (container.getLayout());
				cl.show(container, "INFOPANEL");
			}
		});
		infoButton.setFont(FileManager.pixeled);
		infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JLabel spacer = new JLabel(" ");

		startPanelHolder.add(startButton);
		startPanelHolder.add(infoButton);
		startPanelHolder.add(spacer);

		final JLabel scoresTitle = makeWhiteLabel("HIGH SCORES", 40f);
		scoresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		startPanelHolder.add(scoresTitle);

		highScores = new JPanel();
		highScores.setBackground(Color.BLACK);
		highScores.setBorder(new EmptyBorder(0, 40, 20, 30));
		refreshHighScores();
		startPanelHolder.add(highScores);

	}

	public JPanel getPanel() {
		return startPanelHolder;
	}

	/**
	 * Removes all JLabels from the high score JPanel and re-reads the file
	 */
	public void refreshHighScores() {
		highScores.removeAll();
		try {
			readHighScores();
		} catch (IOException e) {
			System.out.println("Error in Map File.");
		}
	}

	/**
	 * Class that allows the arrays of strings to be compared based on their
	 * entry
	 */
	private class ScoreFormat implements Comparable<ScoreFormat> {
		public String[] data;

		public ScoreFormat(String[] data) {
			this.data = data;
		}

		@Override
		public int compareTo(ScoreFormat sf) {
			return Integer.parseInt(sf.data[3]) - Integer.parseInt(data[3]);
		}
	}

	/**
	 * Reads the entire high score file, picks only a certain number of high
	 * scores to print and updates the high score JPanel's grid to include new
	 * JLabels
	 * 
	 * @throws IOException
	 */
	private void readHighScores() throws IOException {
		final int CUT_OFF = 8;

		List<ScoreFormat> scores = new ArrayList<ScoreFormat>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("misc/highscores.txt"));

			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				} else {
					String[] elements = line.split(" ");
					if (elements.length != 4) {
						throw new IOException();
					} else {
						scores.add(new ScoreFormat(elements));
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Map File Not Found");
			e.printStackTrace();
		} finally {
			reader.close();
		}
		Collections.sort(scores);

		highScores.setLayout(new GridLayout(CUT_OFF + 1, 4));

		String[] header = { "Username", "Time", "Rooms", "Score" };
		for (int i = 0; i < 4; i++) {
			JLabel label = new JLabel(header[i]);
			label.setFont(FileManager.pixeled);
			label.setForeground(Color.WHITE);
			label.setBackground(Color.BLACK);
			highScores.add(label);
		}
		int counter = 0;
		for (ScoreFormat sArr : scores) {
			if (counter == CUT_OFF)
				break;
			for (String s : sArr.data) {
				JLabel label = new JLabel(s);
				label.setFont(FileManager.pixeled);
				label.setForeground(Color.WHITE);
				label.setBackground(Color.BLACK);
				highScores.add(label);
			}
			counter++;
		}
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
