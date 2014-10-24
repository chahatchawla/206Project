package textEditor;

import java.io.File;

import javax.swing.JOptionPane;

public class TextDelete {
	protected void textDelete() {

		// Get the video path and length
		MainTextEditor.getInstance().tpf.setVideoInfo();

		if (MainTextEditor.getInstance().screenType.equals("")) { 
			// chosen
			JOptionPane
			.showMessageDialog(null,
					"ERROR: please select the type of screen you want to delete");
			return;
		}

	
		else if (MainTextEditor.getInstance().screenType.equals("Title Screen")) {
			File f = new File(MainTextEditor.getInstance().workingDir + "/.titleFields");
			File t = new File(MainTextEditor.getInstance().workingDir + "/.TitleText.txt");
			f.delete();
			t.delete();
			MainTextEditor.getInstance().tpf.refreshTitleScreen();
			JOptionPane
			.showMessageDialog(null,
					"Title Screen has been deleted");
			
			
		}
		
		else {
			File f = new File(MainTextEditor.getInstance().workingDir + "/.creditFields");
			File t = new File(MainTextEditor.getInstance().workingDir + "/.CreditText.txt");
			f.delete();
			t.delete();
			MainTextEditor.getInstance().tpf.refreshCreditScreen();
			JOptionPane
			.showMessageDialog(null,
					"Credit Screen has been deleted");
			
		}
		
		


	}
}
