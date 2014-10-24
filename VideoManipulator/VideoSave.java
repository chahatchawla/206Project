package VideoManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;


public class VideoSave {
	
	protected void videoSave(){

		boolean passedAllChecks = MainVideoManipulator.getInstance().vc.allChecksVideo();

		if (passedAllChecks) {

			// Get the video path and length
			MainVideoManipulator.getInstance().vpf.setVideoInfo();

			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file videoFields exists in the working directory
			File f = new File(MainVideoManipulator.getInstance().workingDir + "/.videoFields");
			if (f.exists()) {

				// Allow user to choose either overwriting the existing
				// changes
				// or keep them if the file exists
				Object[] existOptions = {"Overwrite","Cancel"};
				int optionChosen = JOptionPane.showOptionDialog(null,
						"Do you want to overwrite the previous changes?",
						"File Exists!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, existOptions,
						existOptions[0]);
				if (optionChosen == 1) { // If cancel, go back to main menu
					MainVideoManipulator.getInstance().vpf.setAllFields(MainVideoManipulator.getInstance().workingDir + "/.videoFields");
					return;
				}
			}
			// Save all user inputs to the hidden videoFields text
			try {
				
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);

				// Add snapshot fields
				bw.write(MainVideoManipulator.getInstance().snapshotEnable + "\n");
				bw.write(MainVideoManipulator.getInstance().timeSnapshot.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().outputSnapshotName.getText() + "\n");

				// Add loop video fields
				bw.write(MainVideoManipulator.getInstance().loopVideoEnable + "\n");
				bw.write(MainVideoManipulator.getInstance().timeStart.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().timeLength.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().outputLoopVideoName.getText() + "\n");
				bw.write(MainVideoManipulator.getInstance().loop.getText() + "\n");

				// Add filter fields
				bw.write(MainVideoManipulator.getInstance().filterEnable + "\n");
				bw.write(MainVideoManipulator.getInstance().filter + "\n");

				bw.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
