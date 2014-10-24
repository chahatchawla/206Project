package subtitles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class SubtitleSave {
	protected void subtitleSave(){


		// Get the video path and length
		Subtitles.getInstance().spf.setVideoInfo();
		boolean passedAllAdd = Subtitles.getInstance().sc.allChecksAdd();
		boolean passedAllGenerate = Subtitles.getInstance().sc.allChecksGenerate();

		if (passedAllAdd & passedAllGenerate){
			// Reference for JOptionPane():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// check if the file audioFields exists in the working directory
			File f = new File(Subtitles.getInstance().workingDir + "/.subtitleFields");
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
					Subtitles.getInstance().spf.setAllFields(Subtitles.getInstance().workingDir + "/.subtitleFields");
					return;
				}
			}
			// Save all user inputs to the hidden subtitleFields text
			try {

				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// Add Create Srt fields
				bw.write(Subtitles.getInstance().srtEnable + "\n");
				bw.write(Subtitles.getInstance().startTime.getText() + "\n");
				bw.write(Subtitles.getInstance().endTime.getText() + "\n");
				bw.write(Subtitles.getInstance().text.getText() + "\n");
		
				// Make a string for the subtitleList
				StringBuilder subtitleList = new StringBuilder();
				for (int i = 0; i < Subtitles.getInstance().model.getRowCount(); i++) {
					subtitleList.append(Subtitles.getInstance().model.getValueAt(i, 0));
					subtitleList.append(" ");
					subtitleList.append(Subtitles.getInstance().model.getValueAt(i, 1));
					subtitleList.append(" ");
					subtitleList.append(Subtitles.getInstance().model.getValueAt(i, 2));
					subtitleList.append(" ");
				}
				bw.write(subtitleList.toString() + "\n");
				bw.write(Subtitles.getInstance().outputFileName.getText() + "\n");
				
				
				
				// Add import subtitles fields
				bw.write(Subtitles.getInstance().importEnable + "\n");
				bw.write(Subtitles.getInstance().inputFile + "\n");
		

				bw.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
