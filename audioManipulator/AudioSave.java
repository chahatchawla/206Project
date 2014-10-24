package audioManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class AudioSave {
	protected void audioSave(){


		// Get the video path and length
		AudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedAll = AudioManipulator.getInstance().ac.allChecksAudio();

		if (passedAll){
			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file audioFields exists in the working directory
			File f = new File(AudioManipulator.getInstance().workingDir + "/.audioFields");
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
					AudioManipulator.getInstance().apf.setAllFields(AudioManipulator.getInstance().workingDir + "/.audioFields");
					return;
				}
			}
			// Save all user inputs to the hidden audioFields text
			try {

				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				// Add remove audio fields
				bw.write(AudioManipulator.getInstance().removeEnable + "\n");
				// Add extract audio fields
				bw.write(AudioManipulator.getInstance().extractEnable + "\n");
				bw.write(AudioManipulator.getInstance().outputFileName.getText() + "\n");
				bw.write(AudioManipulator.getInstance().extractDurationEnable + "\n");
				bw.write(AudioManipulator.getInstance().startTimeExtract.getText() + "\n");
				bw.write(AudioManipulator.getInstance().lengthExtract.getText() + "\n");
				// Add replace audio fields
				bw.write(AudioManipulator.getInstance().replaceEnable + "\n");
				bw.write(AudioManipulator.getInstance().inputFile + "\n");
				// Add overlay audio fields
				bw.write(AudioManipulator.getInstance().overlayEnable + "\n");
				bw.write(AudioManipulator.getInstance().overlayDurationEnable + "\n");
				bw.write(AudioManipulator.getInstance().startTimeOverlay.getText() + "\n");
				bw.write(AudioManipulator.getInstance().lengthOverlay.getText() + "\n");

				// Make a string for the shortNames of overlay files
				StringBuilder shortNames = new StringBuilder();
				for (int i = 0; i < AudioManipulator.getInstance().audioFiles.getSize(); i++) {
					shortNames.append(AudioManipulator.getInstance().audioFiles.get(i).toString());
					shortNames.append(" ");
				}
				bw.write(shortNames.toString() + "\n");

				// Make a string for the longNames of overlay files
				StringBuilder longNames = new StringBuilder();
				for (int i = 0; i < AudioManipulator.getInstance().fullNames.getSize(); i++) {
					longNames.append(AudioManipulator.getInstance().fullNames.get(i).toString());
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
