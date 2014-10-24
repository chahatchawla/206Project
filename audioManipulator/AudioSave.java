package audioManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class AudioSave {
	protected void audioSave(){


		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedAll = MainAudioManipulator.getInstance().ac.allChecksAudio();

		if (passedAll){
			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file audioFields exists in the working directory
			File f = new File(MainAudioManipulator.getInstance().workingDir + "/.audioFields");
			if (f.exists()) {

				// Allow user to choose either overwriting the existing changes
				// or keep them if the file exists
				Object[] existOptions = {  "Overwrite" ,"Cancel"};
				int optionChosen = JOptionPane.showOptionDialog(null,
						"Do you want to overwrite the previous changes?",
						"File Exists!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, existOptions,
						existOptions[0]);
				if (optionChosen == 1) { // If cancel, go back to main menu
					MainAudioManipulator.getInstance().apf.setAllFields(MainAudioManipulator.getInstance().workingDir + "/.audioFields");
					return;
				}
			}
			// Save all user inputs to the hidden audioFields text
			try {

				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				// Add remove audio fields
				bw.write(MainAudioManipulator.getInstance().removeEnable + "\n");
				// Add extract audio fields
				bw.write(MainAudioManipulator.getInstance().extractEnable + "\n");
				bw.write(MainAudioManipulator.getInstance().outputFileName.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().extractDurationEnable + "\n");
				bw.write(MainAudioManipulator.getInstance().startTimeExtract.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().lengthExtract.getText() + "\n");
				// Add replace audio fields
				bw.write(MainAudioManipulator.getInstance().replaceEnable + "\n");
				bw.write(MainAudioManipulator.getInstance().inputFile + "\n");
				// Add overlay audio fields
				bw.write(MainAudioManipulator.getInstance().overlayEnable + "\n");
				bw.write(MainAudioManipulator.getInstance().overlayDurationEnable + "\n");
				bw.write(MainAudioManipulator.getInstance().startTimeOverlay.getText() + "\n");
				bw.write(MainAudioManipulator.getInstance().lengthOverlay.getText() + "\n");

				// Make a string for the shortNames of overlay files
				StringBuilder shortNames = new StringBuilder();
				for (int i = 0; i < MainAudioManipulator.getInstance().audioFiles.getSize(); i++) {
					shortNames.append(MainAudioManipulator.getInstance().audioFiles.get(i).toString());
					shortNames.append(" ");
				}
				bw.write(shortNames.toString() + "\n");

				// Make a string for the longNames of overlay files
				StringBuilder longNames = new StringBuilder();
				for (int i = 0; i < MainAudioManipulator.getInstance().fullNames.getSize(); i++) {
					longNames.append(MainAudioManipulator.getInstance().fullNames.get(i).toString());
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
