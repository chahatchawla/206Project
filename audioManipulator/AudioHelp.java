package audioManipulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * SoftEng206 Project - audio help class
 * 
 * Purpose: The purpose of this class is to create a new frame that displays the
 * help image for audio manipulation . This class is used in
 * MainAudioManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class AudioHelp {

	/**
	 * audioHelp() Method creates a help fram for audio manipulation
	 */
	protected void audioHelp() {
		// Set the audio help frame and panel
		JFrame helpframe = new JFrame("Help - Audio Manipulator");
		JPanel helpPanel = new JPanel(new BorderLayout());

		// Add the image to the panel
		helpPanel.add(MainAudioManipulator.getInstance().helpImage);

		// Set all the setting of the frame
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
