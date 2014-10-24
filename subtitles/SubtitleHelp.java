package subtitles;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SubtitleHelp {
	
	protected void subtitleHelp(){
		//Set the audio help frame 
		JFrame helpframe = new JFrame("Help - Subtitles");
		JPanel helpPanel = new JPanel(new BorderLayout());

		helpPanel.add(Subtitles.getInstance().helpImage);
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
