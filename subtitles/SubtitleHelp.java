package subtitles;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * SoftEng206 Project - subtitle help class
 * 
 * Purpose: The purpose of this class is to create a new frame that displays the
 * help image for subtitles . This class is used in MainSubtitles.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class SubtitleHelp {
	/**
	 * subtitleHelp() Method creates a help frame for subtitles
	 */
	protected void subtitleHelp(){
		//Set the subtitles help frame and panel
		JFrame helpframe = new JFrame("Help - Subtitles");
		JPanel helpPanel = new JPanel(new BorderLayout());
		// Add the image to the panel
		helpPanel.add(MainSubtitles.getInstance().helpImage);
		// Set all the setting of the frame
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
