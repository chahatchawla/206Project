package VideoManipulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VideoHelp {
	
	
	protected void videoHelp(){
		//Set the video help frame 
		JFrame helpframe = new JFrame("Help - Video Manipulator");
		JPanel helpPanel = new JPanel(new BorderLayout());
		
		helpPanel.add(MainVideoManipulator.getInstance().helpImage);
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
	

}
