package mainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import subtitles.SubtitleProjectFunctions;
import subtitles.MainSubtitles;
import textEditor.MainTextEditor;
import textEditor.TextProjectFunctions;
import videoManipulator.MainVideoManipulator;
import videoManipulator.VideoProjectFunctions;
import audioManipulator.MainAudioManipulator;
import audioManipulator.AudioProjectFunctions;

/**
 * SoftEng206 Project - Main class
 * 
 * Purpose: The purpose of this class is to run the application. This is where
 * the main frame of the application gets constructed.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class Main {
	/**
	 * Main method that runs the Vamix player
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			// Run the application
			@Override
			public void run() {
				new Main();
			}
		});
	}

	// Initialize all the fields and JComponents
	protected static JFrame frame;

	protected static JTabbedPane tabbedPane;

	protected static MainAudioManipulator audioMan;
	protected static MainVideoManipulator videoMan;
	protected static MainTextEditor textEdit;
	protected static MainSubtitles subtitles;
	protected static VideoProjectFunctions vpf = new VideoProjectFunctions();
	protected static AudioProjectFunctions apf = new AudioProjectFunctions();
	protected static TextProjectFunctions tpf = new TextProjectFunctions();
	protected static SubtitleProjectFunctions spf = new SubtitleProjectFunctions();

	private JLabel titleImage;

	/**
	 * The Main() Construction - sets up the MAIN GUI frame and the welcome
	 * screen
	 */
	private Main() {

		titleImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/title.png")));

		frame = new JFrame("VAMIX - Video Audio Mixer ");

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(525, 700));

		// Add the singleton classes to the tabs
		videoMan = MainVideoManipulator.getInstance();
		tabbedPane.add("Video Manipulator", videoMan);
		audioMan = MainAudioManipulator.getInstance();
		tabbedPane.add("Audio Manipulator", audioMan);
		textEdit = MainTextEditor.getInstance();
		tabbedPane.add("Text Editor", textEdit);
		subtitles = MainSubtitles.getInstance();
		tabbedPane.add("Subtitles", subtitles);

		// Disable the tabs when first loading the main window
		tabbedPane.setEnabled(false);
		apf.enableAudioMan(false);
		vpf.enableVideoMan(false);
		spf.enableSubtitle(false);

		// Add the panels to a main panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(Menu.getInstance(), BorderLayout.NORTH);
		panel.add(MediaPlayer.getInstance(), BorderLayout.EAST);
		panel.add(tabbedPane, BorderLayout.WEST);

		// Set the frame
		frame.setContentPane(panel);
		frame.setLocation(250, 100);
		frame.setResizable(false);
		frame.setSize(1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Set the volume values back to default when the user closes the Vamix
		// player
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				MediaPlayer.getInstance().video.mute(false);
				MediaPlayer.getInstance().video.setVolume(100);
			}
		});

		// Set the welcome title frame
		JFrame titleframe = new JFrame("Welcome to VAMIX");
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(titleImage);
		titleframe.setContentPane(titlePanel);
		titleframe.setLocation(450, 100);
		titleframe.setResizable(false);
		titleframe.setSize(800, 650);
		titleframe.setVisible(true);

	}

}
