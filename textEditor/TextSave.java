package textEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class TextSave {
	
	private int fileExistsResponse = -1;
	protected void textSave(){
		
		// Get the video path and length
		TextEditor.getInstance().tpf.setVideoInfo();

		if (TextEditor.getInstance().screenType.equals("")) { // Abort saving if screen type was not
			// chosen
			JOptionPane
			.showMessageDialog(null,
					"ERROR: please select the type of screen you want to add text on.");
			return;
		}

		boolean passsedChecks = TextEditor.getInstance().tc.allChecks();

		if (passsedChecks) {

			// check the screen type to select the corresponding text file
			if (TextEditor.getInstance().screenType.equals("Title Screen")) {
				TextEditor.getInstance().textFile = new File(TextEditor.getInstance().workingDir + "/.TitleText.txt");
				// get the duration for title screen
				TextEditor.getInstance().titleDuration = TextEditor.getInstance().addDuration.getText().trim();
				// get the inputFrameTime for tistle screen
				TextEditor.getInstance().titleFrameTime = TextEditor.getInstance().addTimeFrame.getText().trim();


				// set the font settings for title screen
				TextEditor.getInstance().tpf.setTitleFontSettings();

				// Save all user inputs to the hidden titleFields text
				File f = new File(TextEditor.getInstance().workingDir + "/.titleFields");
				try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);
					// Add title audio fields
					bw.write("Title Screen" + "\n");
					bw.write(TextEditor.getInstance().titleDuration + "\n");
					bw.write(TextEditor.getInstance().overlayCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().defaultCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().frameCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().titleFrameTime + "\n");
					bw.write(TextEditor.getInstance().titleFontSize + "\n");
					bw.write(TextEditor.getInstance().titleFontColour + "\n");
					bw.write(TextEditor.getInstance().titleFontName + "\n");
					bw.write(TextEditor.getInstance().titleFontStyle + "\n");
					bw.write(TextEditor.getInstance().titlePreviewFont + "\n");
					bw.write(TextEditor.getInstance().addTextArea.getText() + "\n");
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}


			} else {
				TextEditor.getInstance().textFile = new File(TextEditor.getInstance().workingDir + "/.CreditText.txt");
				// get the duration for credit screen
				TextEditor.getInstance().creditDuration = TextEditor.getInstance().addDuration.getText().trim();
				// get the inputFrameTime for credit screen
				TextEditor.getInstance().creditFrameTime = TextEditor.getInstance().addTimeFrame.getText().trim();



				// set the font settings for credit screen
				TextEditor.getInstance().tpf.setCreditFontSettings();

				// Save all user inputs to the hidden creditFields text
				File f = new File(TextEditor.getInstance().workingDir + "/.creditFields");
				try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);
					// Add credit audio fields
					bw.write("Credit Screen" + "\n");
					bw.write(TextEditor.getInstance().creditDuration + "\n");
					bw.write(TextEditor.getInstance().overlayCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().defaultCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().frameCheck.isSelected() + "\n");
					bw.write(TextEditor.getInstance().creditFrameTime + "\n");
					bw.write(TextEditor.getInstance().creditFontSize + "\n");
					bw.write(TextEditor.getInstance().creditFontColour + "\n");
					bw.write(TextEditor.getInstance().creditFontName + "\n");
					bw.write(TextEditor.getInstance().creditFontStyle + "\n");
					bw.write(TextEditor.getInstance().creditPreviewFont + "\n");
					bw.write(TextEditor.getInstance().addTextArea.getText() + "\n");
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}


			}
			// check whether the text file for credit or title exists
			if (TextEditor.getInstance().textFile.exists()) {

				// if the file does exist, inform the user that they have
				// previously created a screen
				while (fileExistsResponse == JOptionPane.CLOSED_OPTION) {
					Object[] options = { "Keep original settings",
					"Overwrite" };
					fileExistsResponse = JOptionPane
							.showOptionDialog(
									null,
									"Chosen screen already created. Are you sure you want to make changes?",
									"Screen Exists",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[1]);

					// overwrite option
					if (fileExistsResponse == 0) {
						FileWriter fw;
						try {
							fw = new FileWriter(TextEditor.getInstance().textFile, false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter x = new PrintWriter(bw);
							x.println(TextEditor.getInstance().addTextArea.getText());
							bw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}

			// Reference for writing to file:
			// http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

			// If the file does not exist create a new file, and append the
			// addTextArea text into the file
			else {
				FileWriter fw;
				try {
					fw = new FileWriter(TextEditor.getInstance().textFile, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter x = new PrintWriter(bw);
					x.println(TextEditor.getInstance().addTextArea.getText());
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
		
	}
}
