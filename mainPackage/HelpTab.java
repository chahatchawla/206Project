package mainPackage;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class HelpTab {
	protected void helpTab(){
		//Create and set the help window
		JFrame helpFrame = new JFrame("Help & Tips"); 
		JTabbedPane helpTips = new JTabbedPane(); 
		helpTips.setPreferredSize(new Dimension(525, 440)); 
		helpTips.add(Menu.getInstance().mainImage, "Welcome to Vamix"); 
		helpTips.add(Menu.getInstance().videoImage, "Video Manipulation"); 
		helpTips.add(Menu.getInstance().audioImage, "Audio Manipulation"); 
		helpTips.add(Menu.getInstance().textEditImage, "Text Editor"); 

		helpFrame.setContentPane(helpTips); 
		helpFrame.setLocation(450, 100);
		helpFrame.setResizable(false);
		helpFrame.setSize(800, 650);
		helpFrame.setVisible(true);; 
		helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
	}

}
