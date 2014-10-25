package textEditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * SoftEng206 Project - text help class
 * 
 * Purpose: The purpose of this class is to create a new frame that displays the
 * help image for text editing . This class is used in MainTextEditor.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class TextHelp {

	/**
	 * textHelp() Method creates a help frame for text editing
	 */
	protected void textHelp() {
		// Set the text help frame and panel
		JFrame helpframe = new JFrame("Help - Text Editor");
		JPanel helpPanel = new JPanel(new BorderLayout());
		// Add the image to the panel
		helpPanel.add(MainTextEditor.getInstance().helpImage);
		// Set all the setting of the frame
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
