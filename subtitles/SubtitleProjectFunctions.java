package subtitles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import mainPackage.Menu;

public class SubtitleProjectFunctions {

	/**
	 * enableSubtitle Method enables or disable all the fields in the subtitles
	 * tab depending on the state
	 * 
	 * @param state
	 */

	public void enableSubtitle(boolean state) {
		Subtitles.getInstance().subtitlesLabel.setEnabled(state);
		Subtitles.getInstance().importCheck.setEnabled(state);
		Subtitles.getInstance().srtCheck.setEnabled(state);

		
		Subtitles.getInstance().saveButton.setEnabled(state);
		Subtitles.getInstance().helpButton.setEnabled(state);
	}

	/**
	 * setAllFields Method sets all the fields of the video manipulator class to
	 * the previously saved values
	 * 
	 * @param videoFieldsPath
	 *            : the path of the stored audio fields
	 */
	public void setAllFields(String subtitlesFieldsPath) {

		Subtitles.getInstance().subtitlesFields = subtitlesFieldsPath;
		File f = new File(Subtitles.getInstance().subtitlesFields);
		try {

			// read the videoFields file and sets the fields for video
			// manipulation depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			Subtitles.getInstance().srtCheck.setSelected(Boolean
					.parseBoolean(reader.readLine()));
			Subtitles.getInstance().startTime.setText(reader
					.readLine());
			Subtitles.getInstance().endTime.setText(reader
					.readLine());
			Subtitles.getInstance().text.setText(reader
					.readLine());

			String subtitles = reader.readLine();
			
			int listRow = Subtitles.getInstance().model.getRowCount();
			for (int i = 0; i < listRow; i++){
				Subtitles.getInstance().model.removeRow(0);
						}
			if (subtitles !=null){
				String[] splitList = subtitles.split(" ");

				for (int i = 0; i< splitList.length; i = i +3) {

					Subtitles.getInstance().model.addRow(new Object[]{splitList[i], splitList[i+1],splitList[i+2]} );

				}
			}

			Subtitles.getInstance().outputFileName.setText(reader
					.readLine());

			Subtitles.getInstance().importCheck.setSelected(Boolean
					.parseBoolean(reader.readLine()));
			Subtitles.getInstance().inputFile = (reader
					.readLine());

			if (Subtitles.getInstance().inputFile != null) { 
				File subtitle = new File(Subtitles.getInstance().inputFile);
				if (subtitle.exists()) {
					Subtitles.getInstance().playButton.setEnabled(true);
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * setVideoInfo Method stores the video info from the hidden file to the
	 * private fields
	 * 
	 * @param hiddenDir
	 */
	public void setVideoInfo() {
		// Get the main project file
		Subtitles.getInstance().projectPath = Menu.getProjectPath();
		File f = new File(Subtitles.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			Subtitles.getInstance().hiddenDir = reader.readLine();
			Subtitles.getInstance().workingDir = reader.readLine();
			Subtitles.getInstance().videoPath = reader.readLine(); // video
			// path
			Subtitles.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * refreshVideoMan Method refreshes all the fields in the video manipulator
	 * tab
	 */
	public void refreshSubtitles() {

		Subtitles.getInstance().startTime.setText("hh:mm:ss,mmm");
		Subtitles.getInstance().endTime.setText("hh:mm:ss,mmm");
		Subtitles.getInstance().text.setText("");
		Subtitles.getInstance().outputFileName.setText("");
		Subtitles.getInstance().inputFile="";
		
		int listRow = Subtitles.getInstance().model.getRowCount();
		for (int i = 0; i < listRow; i++){
			Subtitles.getInstance().model.removeRow(0);
		}

		Subtitles.getInstance().importCheck.setSelected(false);
		Subtitles.getInstance().srtCheck.setSelected(false);

		Subtitles.getInstance().projectPath = "";
		Subtitles.getInstance().hiddenDir = "";
		Subtitles.getInstance().videoPath = "";
		Subtitles.getInstance().videoLength = "";
		Subtitles.getInstance().subtitlesFields = "";
		Subtitles.getInstance().workingDir = "";

	}

}
