package textEditor;

import java.io.File;

import javax.swing.JOptionPane;

public class TextDelete {
	protected void textDelete() {

		// Get the video path and length
		TextEditor.getInstance().tpf.setVideoInfo();

		if (TextEditor.getInstance().screenType.equals("")) { 
			// chosen
			JOptionPane
			.showMessageDialog(null,
					"ERROR: please select the type of screen you want to delete");
			return;
		}

	
		else if (TextEditor.getInstance().screenType.equals("Title Screen")) {
			File f = new File(TextEditor.getInstance().workingDir + "/.titleFields");
			File t = new File(TextEditor.getInstance().workingDir + "/.TitleText.txt");
			f.delete();
			t.delete();
			TextEditor.getInstance().tpf.refreshTitleScreen();
			JOptionPane
			.showMessageDialog(null,
					"Title Screen has been deleted");
			
			
		}
		
		else {
			File f = new File(TextEditor.getInstance().workingDir + "/.creditFields");
			File t = new File(TextEditor.getInstance().workingDir + "/.CreditText.txt");
			f.delete();
			t.delete();
			TextEditor.getInstance().tpf.refreshCreditScreen();
			JOptionPane
			.showMessageDialog(null,
					"Credit Screen has been deleted");
			
		}
		
		


	}
}
