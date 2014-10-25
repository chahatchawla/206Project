package textEditor;

import java.io.File;

import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - text delete class
 * 
 * Purpose: The purpose of this class is to delete an exisiting saved title or
 * credit screen . This class is used in MainTextEditor.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/
public class TextDelete {

	/**
	 * textDelete Method is performed when the deleteBtn is clicked. It deletes
	 * the saved selected screen and refreshes that screen inputs
	 */
	protected void textDelete() {

		// Get the video path and length
		MainTextEditor.getInstance().tpf.setVideoInfo();

		// If no screen is selected
		if (MainTextEditor.getInstance().screenType.equals("")) {
			// Inform the user and allow them to select a screen
			JOptionPane
					.showMessageDialog(null,
							"ERROR: please select the type of screen you want to delete");
			return;
		}
		// If title screen is selected
		else if (MainTextEditor.getInstance().screenType.equals("Title Screen")) {

			File f = new File(MainTextEditor.getInstance().workingDir
					+ "/.titleFields");
			File t = new File(MainTextEditor.getInstance().workingDir
					+ "/.TitleText.txt");
			// Delete the titleFields and also the titleText
			f.delete();
			t.delete();
			// Refresh the title screen and inform the user that delete has been
			// performed
			MainTextEditor.getInstance().tpf.refreshTitleScreen();
			JOptionPane
					.showMessageDialog(null, "Title Screen has been deleted");

		}
		// If credit screen is selected
		else {
			File f = new File(MainTextEditor.getInstance().workingDir
					+ "/.creditFields");
			File t = new File(MainTextEditor.getInstance().workingDir
					+ "/.CreditText.txt");
			// Delete the creditFields and also the CreditText
			f.delete();
			t.delete();
			// Refresh the credit screen and inform the user that delete has
			// been
			// performed
			MainTextEditor.getInstance().tpf.refreshCreditScreen();
			JOptionPane.showMessageDialog(null,
					"Credit Screen has been deleted");

		}

	}
}
