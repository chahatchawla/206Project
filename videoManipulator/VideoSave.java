package videoManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - video save class
 * 
 * Purpose: The purpose of this class is to save all the user inputs in the
 * video manipulation tab in a hidden file, so when the open their project
 * again, they can view their inputs. This class is used in
 * MainVideoManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class VideoSave {
	/**
	 * videoSave() Method checks performs all the video checks, checks whether
	 * the user has already saved changes in the video manipulation tab and
	 * lastly saves the user input
	 */
	protected void videoSave() {

		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();

		// Perform all the checks
		boolean passedAllChecks = MainVideoManipulator.getInstance().vc
				.allChecksVideo();

		// If checks are passed
		if (passedAllChecks) {

			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file videoFields exists in the working directory
			File f = new File(MainVideoManipulator.getInstance().workingDir
					+ "/.videoFields");
			if (f.exists()) {

				// Allow user to choose either overwriting the existing
				// changes
				// or keep them if the file exists
				Object[] existOptions = { "Overwrite", "Cancel" };
				int optionChosen = JOptionPane.showOptionDialog(null,
						"Do you want to overwrite the previous changes?",
						"File Exists!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, existOptions,
						existOptions[0]);
				if (optionChosen == 1) { // If cancel, go back to main menu
					MainVideoManipulator.getInstance().vpf
							.setAllFields(MainVideoManipulator.getInstance().workingDir
									+ "/.videoFields");
					return;
				}
			}
			// Save all user inputs to the hidden videoFields text
			try {

				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);

				// Add snapshot fields
				bw.write(MainVideoManipulator.getInstance().snapshotEnable
						+ "\n");
				bw.write(MainVideoManipulator.getInstance().timeSnapshot
						.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().outputSnapshotName
						.getText() + "\n");

				// Add loop video fields
				bw.write(MainVideoManipulator.getInstance().loopVideoEnable
						+ "\n");
				bw.write(MainVideoManipulator.getInstance().timeStart.getText()
						+ "\n");
				bw.write(MainVideoManipulator.getInstance().timeLength
						.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().outputLoopVideoName
						.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().loop.getText()
						+ "\n");

				// Add filter fields
				bw.write(MainVideoManipulator.getInstance().filterEnable + "\n");
				bw.write(MainVideoManipulator.getInstance().filter + "\n");

				bw.close();
				JOptionPane.showMessageDialog(null, "Video Manipulator changes have been saved!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
