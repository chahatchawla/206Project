package audioManipulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AudioHelp {
	
	protected void audioHelp(){
		//Set the audio help frame 
		JFrame helpframe = new JFrame("Help - Audio Manipulator");
		JPanel helpPanel = new JPanel(new BorderLayout());

		helpPanel.add(MainAudioManipulator.getInstance().helpImage);
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
