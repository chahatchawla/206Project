package mainPackage;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * SoftEng206 Project - help tab class
 * 
 * Purpose: The purpose of this class is to create a new frame that displays the
 * help images for welcome to vamix, video manipulation, audio manipulation and
 * text editing . This class is used in mainPackage.Menu.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class HelpTab {
	
	/**
	 * helpTab() Method creates a help frame for the help tab
	 */
	protected void helpTab() {
		// Create and set the help window
		JFrame helpFrame = new JFrame("Help & Tips");
		JTabbedPane helpTips = new JTabbedPane();
		helpTips.setPreferredSize(new Dimension(525, 440));
		
		// Add images to the tabs
		helpTips.add(Menu.getInstance().mainImage, "Welcome to Vamix");
		helpTips.add(Menu.getInstance().videoImage, "Video Manipulation");
		helpTips.add(Menu.getInstance().audioImage, "Audio Manipulation");
		helpTips.add(Menu.getInstance().textEditImage, "Text Editor");
		helpTips.add(Menu.getInstance().subtitleImage, "Subtitles");

		// Set the frame settings
		helpFrame.setContentPane(helpTips);
		helpFrame.setLocation(450, 100);
		helpFrame.setResizable(false);
		helpFrame.setSize(800, 650);
		helpFrame.setVisible(true);
		helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
