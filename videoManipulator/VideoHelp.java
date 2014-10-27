package videoManipulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * SoftEng206 Project - video help class
 * 
 * Purpose: The purpose of this class is to create a new frame that displays the
 * help image for video manipulation . This class is used in
 * MainVideoManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class VideoHelp {
	/**
	 * videoHelp() Method creates a help frame for video manipulation
	 */
	protected void videoHelp() {
		// Set the video help frame
		JFrame helpframe = new JFrame("Help - Video Manipulator");
		JPanel helpPanel = new JPanel(new BorderLayout());

		// Add the image to the panel
		helpPanel.add(MainVideoManipulator.getInstance().helpImage);

		// Set all the setting of the frame
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}

}
