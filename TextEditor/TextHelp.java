package TextEditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextHelp {
	protected void textHelp(){
		//Set the welcome title frame 
		JFrame helpframe = new JFrame("Help - Text Editor");
		JPanel helpPanel = new JPanel(new BorderLayout());
		
		helpPanel.add(TextEditor.getInstance().helpImage);
		helpframe.setContentPane(helpPanel);
		helpframe.setLocation(450, 100);
		helpframe.setResizable(false);
		helpframe.setSize(800, 650);
		helpframe.setVisible(true);
	}
}
