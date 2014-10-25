package textEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - text edit save class
 * 
 * Purpose: The purpose of this class is to save all the user inputs in the text
 * editing tab in a hidden file, so when the open their project again, they can
 * view their inputs. This class is used in MainTextEditor.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class TextSave {

	private int fileExistsResponse = -1;

	/**
	 * textSave() Method checks performs all the text checks, checks whether the
	 * user has already saved changes in the text editing tab and lastly saves
	 * the user input
	 */
	protected void textSave() {

		// Get the video path and length
		MainTextEditor.getInstance().tpf.setVideoInfo();

		if (MainTextEditor.getInstance().screenType.equals("")) {
			// Abort saving if screen type was not chosen
			JOptionPane
					.showMessageDialog(null,
							"ERROR: please select the type of screen you want to add text on");
			return;
		}

		// Perform all the checks
		boolean passsedChecks = MainTextEditor.getInstance().tc.allChecks();
		// If checks are passed
		if (passsedChecks) {

			// check the screen type to select the corresponding text file
			if (MainTextEditor.getInstance().screenType.equals("Title Screen")) {
				MainTextEditor.getInstance().textFile = new File(
						MainTextEditor.getInstance().workingDir
								+ "/.TitleText.txt");
				// get the duration for title screen
				MainTextEditor.getInstance().titleDuration = MainTextEditor
						.getInstance().addDuration.getText().trim();
				// get the inputFrameTime for title screen
				MainTextEditor.getInstance().titleFrameTime = MainTextEditor
						.getInstance().addTimeFrame.getText().trim();

				// set the font settings for title screen
				MainTextEditor.getInstance().tpf.setTitleFontSettings();

				// Reference:
				// http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/

				// Save all user inputs to the hidden titleFields text
				File f = new File(MainTextEditor.getInstance().workingDir
						+ "/.titleFields");
				try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);
					// Add title audio fields
					bw.write("Title Screen" + "\n");
					bw.write(MainTextEditor.getInstance().titleDuration + "\n");
					bw.write(MainTextEditor.getInstance().overlayCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().defaultCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().frameCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().titleFrameTime + "\n");
					bw.write(MainTextEditor.getInstance().titleFontSize + "\n");
					bw.write(MainTextEditor.getInstance().titleFontColour
							+ "\n");
					bw.write(MainTextEditor.getInstance().titleFontName + "\n");
					bw.write(MainTextEditor.getInstance().titleFontStyle + "\n");
					bw.write(MainTextEditor.getInstance().titlePreviewFont
							+ "\n");
					bw.write(MainTextEditor.getInstance().addTextArea.getText()
							+ "\n");
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				MainTextEditor.getInstance().textFile = new File(
						MainTextEditor.getInstance().workingDir
								+ "/.CreditText.txt");
				// get the duration for credit screen
				MainTextEditor.getInstance().creditDuration = MainTextEditor
						.getInstance().addDuration.getText().trim();
				// get the inputFrameTime for credit screen
				MainTextEditor.getInstance().creditFrameTime = MainTextEditor
						.getInstance().addTimeFrame.getText().trim();

				// set the font settings for credit screen
				MainTextEditor.getInstance().tpf.setCreditFontSettings();

				// Save all user inputs to the hidden creditFields text
				File f = new File(MainTextEditor.getInstance().workingDir
						+ "/.creditFields");

				// Reference:
				// http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
				try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);
					// Add credit audio fields
					bw.write("Credit Screen" + "\n");
					bw.write(MainTextEditor.getInstance().creditDuration + "\n");
					bw.write(MainTextEditor.getInstance().overlayCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().defaultCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().frameCheck
							.isSelected() + "\n");
					bw.write(MainTextEditor.getInstance().creditFrameTime
							+ "\n");
					bw.write(MainTextEditor.getInstance().creditFontSize + "\n");
					bw.write(MainTextEditor.getInstance().creditFontColour
							+ "\n");
					bw.write(MainTextEditor.getInstance().creditFontName + "\n");
					bw.write(MainTextEditor.getInstance().creditFontStyle
							+ "\n");
					bw.write(MainTextEditor.getInstance().creditPreviewFont
							+ "\n");
					bw.write(MainTextEditor.getInstance().addTextArea.getText()
							+ "\n");
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
			// check whether the text file for credit or title exists
			if (MainTextEditor.getInstance().textFile.exists()) {

				// if the file does exist, inform the user that they have
				// previously created a screen
				while (fileExistsResponse == JOptionPane.CLOSED_OPTION) {
					Object[] options = { "Keep original settings", "Overwrite" };
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
							fw = new FileWriter(
									MainTextEditor.getInstance().textFile,
									false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter x = new PrintWriter(bw);
							x.println(MainTextEditor.getInstance().addTextArea
									.getText());
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
					fw = new FileWriter(MainTextEditor.getInstance().textFile,
							true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter x = new PrintWriter(bw);
					x.println(MainTextEditor.getInstance().addTextArea
							.getText());
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
}
