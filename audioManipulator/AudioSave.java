package audioManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - audio save class
 * 
 * Purpose: The purpose of this class is to save all the user inputs in the
 * audio manipulation tab in a hidden file, so when the open their project
 * again, they can view their inputs. This class is used in
 * MainAudioManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class AudioSave {

	/**
	 * audioSave() Method checks performs all the audio checks, checks whether
	 * the user has already saved changes in the audio manipulation tab and
	 * lastly saves the user input
	 */
	protected void audioSave() {

		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();
		
		// Perform all the checks
		boolean passedAll = MainAudioManipulator.getInstance().ac
				.allChecksAudio();
		
		// If checks are passed 
		if (passedAll) {
			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file audioFields exists in the working directory
			File f = new File(MainAudioManipulator.getInstance().workingDir
					+ "/.audioFields");
			if (f.exists()) {

				// Allow user to choose either overwriting the existing changes
				// or keep them if the file exists
				Object[] existOptions = { "Overwrite", "Cancel" };
				int optionChosen = JOptionPane.showOptionDialog(null,
						"Do you want to overwrite the previous changes?",
						"File Exists!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, existOptions,
						existOptions[0]);
				if (optionChosen == 1) { // If cancel, go back to main menu
					MainAudioManipulator.getInstance().apf
							.setAllFields(MainAudioManipulator.getInstance().workingDir
									+ "/.audioFields");
					return;
				}
			}
			// Save all user inputs to the hidden audioFields text
			try {

				// Reference:
				// http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/

				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				// Add remove audio fields
				bw.write(MainAudioManipulator.getInstance().removeEnable + "\n");
				// Add extract audio fields
				bw.write(MainAudioManipulator.getInstance().extractEnable
						+ "\n");
				bw.write(MainAudioManipulator.getInstance().outputFileName
						.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().extractDurationEnable
						+ "\n");
				bw.write(MainAudioManipulator.getInstance().startTimeExtract
						.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().lengthExtract
						.getText() + "\n");
				// Add replace audio fields
				bw.write(MainAudioManipulator.getInstance().replaceEnable
						+ "\n");
				bw.write(MainAudioManipulator.getInstance().inputFile + "\n");
				// Add overlay audio fields
				bw.write(MainAudioManipulator.getInstance().overlayEnable
						+ "\n");
				bw.write(MainAudioManipulator.getInstance().overlayDurationEnable
						+ "\n");
				bw.write(MainAudioManipulator.getInstance().startTimeOverlay
						.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().lengthOverlay
						.getText() + "\n");

				// Make a string for the shortNames of overlay files
				StringBuilder shortNames = new StringBuilder();
				for (int i = 0; i < MainAudioManipulator.getInstance().audioFiles
						.getSize(); i++) {
					shortNames
							.append(MainAudioManipulator.getInstance().audioFiles
									.get(i).toString());
					shortNames.append(" ");
				}
				bw.write(shortNames.toString() + "\n");

				// Make a string for the longNames of overlay files
				StringBuilder longNames = new StringBuilder();
				for (int i = 0; i < MainAudioManipulator.getInstance().fullNames
						.getSize(); i++) {
					longNames
							.append(MainAudioManipulator.getInstance().fullNames
									.get(i).toString());
					longNames.append(" ");
				}
				bw.write(longNames.toString() + "\n");

				bw.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
