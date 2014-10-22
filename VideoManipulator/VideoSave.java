package VideoManipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;


public class VideoSave {
	
	protected void videoSave(){

		boolean passedAllChecks = VideoManipulator.getInstance().vc.allChecksVideo();

		if (passedAllChecks) {

			// Get the video path and length
			VideoManipulator.getInstance().vpf.setVideoInfo();

			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file videoFields exists in the working directory
			File f = new File(VideoManipulator.getInstance().workingDir + "/.videoFields");
			if (f.exists()) {

				// Allow user to choose either overwriting the existing
				// changes
				// or keep them if the file exists
				Object[] existOptions = { "Cancel", "Overwrite" };
				int optionChosen = JOptionPane.showOptionDialog(null,
						"Do you want to overwrite the previous changes?",
						"File Exists!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, existOptions,
						existOptions[0]);
				if (optionChosen == 0) { // If cancel, go back to main menu
					VideoManipulator.getInstance().vpf.setAllFields(VideoManipulator.getInstance().workingDir + "/.videoFields");
					return;
				}
			}
			// Save all user inputs to the hidden videoFields text
			try {
				
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);

				// Add snapshot fields
				bw.write(VideoManipulator.getInstance().snapshotEnable + "\n");
				bw.write(VideoManipulator.getInstance().timeSnapshot.getText() + "\n");
				bw.write(VideoManipulator.getInstance().outputSnapshotName.getText() + "\n");

				// Add loop video fields
				bw.write(VideoManipulator.getInstance().loopVideoEnable + "\n");
				bw.write(VideoManipulator.getInstance().timeStart.getText() + "\n");
				bw.write(VideoManipulator.getInstance().timeLength.getText() + "\n");
				bw.write(VideoManipulator.getInstance().outputLoopVideoName.getText() + "\n");
				bw.write(VideoManipulator.getInstance().loop.getText() + "\n");

				// Add filter fields
				bw.write(VideoManipulator.getInstance().filterEnable + "\n");
				bw.write(VideoManipulator.getInstance().filter + "\n");

				bw.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
