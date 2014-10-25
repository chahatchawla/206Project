package subtitles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mainPackage.Menu;

/**
 * SoftEng206 Project - subtitle project function class
 * 
 * Purpose: The purpose of this class is to perform all the project functions.
 * It has functions to enable/disable, set and refresh all the fields of
 * subtitle tab. It also sets the main video info for subtitle. This class is
 * used in MainSubtitles.java and also in multiple classes in the mainPackage
 * for performing these functions while creating a new project or opening a new
 * project etc.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class SubtitleProjectFunctions {

	/**
	 * enableSubtitle Method enables or disable all the fields in the subtitles
	 * tab depending on the state
	 * 
	 * @param state
	 */

	public void enableSubtitle(boolean state) {
		MainSubtitles.getInstance().subtitlesLabel.setEnabled(state);
		MainSubtitles.getInstance().importCheck.setEnabled(state);
		MainSubtitles.getInstance().srtCheck.setEnabled(state);

		MainSubtitles.getInstance().saveButton.setEnabled(state);
		MainSubtitles.getInstance().helpButton.setEnabled(state);
	}

	/**
	 * setAllFields Method sets all the fields of the subtitles class to the
	 * previously saved values
	 * 
	 * @param subtitlesFieldsPath
	 *            : the path of the stored subtitles fields
	 */
	public void setAllFields(String subtitlesFieldsPath) {

		// Reference to BufferedReader :
		// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html

		MainSubtitles.getInstance().subtitlesFields = subtitlesFieldsPath;
		File f = new File(MainSubtitles.getInstance().subtitlesFields);
		try {

			// read the subtitlesFields file and sets the fields for subtitles
			// depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			MainSubtitles.getInstance().srtCheck.setSelected(Boolean
					.parseBoolean(reader.readLine()));
			MainSubtitles.getInstance().startTime.setText(reader.readLine());
			MainSubtitles.getInstance().endTime.setText(reader.readLine());
			MainSubtitles.getInstance().text.setText(reader.readLine());

			String subtitles = reader.readLine();
			// Remove the rows from the table
			int listRow = MainSubtitles.getInstance().model.getRowCount();
			for (int i = 0; i < listRow; i++) {
				MainSubtitles.getInstance().model.removeRow(0);
			}
			// read the line and split the sentence so every subtitle is
			// be added to the table
			if (subtitles != null) {
				String[] splitList = subtitles.split(" ");

				for (int i = 0; i < splitList.length; i = i + 3) {
					MainSubtitles.getInstance().model.addRow(new Object[] {
							splitList[i], splitList[i + 1], splitList[i + 2] });
				}
			}

			MainSubtitles.getInstance().outputFileName.setText(reader
					.readLine());

			MainSubtitles.getInstance().importCheck.setSelected(Boolean
					.parseBoolean(reader.readLine()));
			MainSubtitles.getInstance().inputFile = (reader.readLine());
			// If subtitles was imported, enable the option of playing it
			if (MainSubtitles.getInstance().inputFile != null) {
				File subtitle = new File(MainSubtitles.getInstance().inputFile);
				if (subtitle.exists()) {
					MainSubtitles.getInstance().playButton.setEnabled(true);
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
	 */
	public void setVideoInfo() {
		// Get the main project file
		MainSubtitles.getInstance().projectPath = Menu.getInstance()
				.getProjectPath();

		// Reference to BufferedReader :
		// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
		File f = new File(MainSubtitles.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			MainSubtitles.getInstance().hiddenDir = reader.readLine();
			MainSubtitles.getInstance().workingDir = reader.readLine();
			MainSubtitles.getInstance().videoPath = reader.readLine();
			MainSubtitles.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * refreshSubtitles Method refreshes all the fields in the subtitles tab
	 */
	public void refreshSubtitles() {

		MainSubtitles.getInstance().startTime.setText("hh:mm:ss,mmm");
		MainSubtitles.getInstance().endTime.setText("hh:mm:ss,mmm");
		MainSubtitles.getInstance().text.setText("");
		MainSubtitles.getInstance().outputFileName.setText("");
		MainSubtitles.getInstance().inputFile = "";

		// Deletes all the rows from the table 
		int listRow = MainSubtitles.getInstance().model.getRowCount();
		for (int i = 0; i < listRow; i++) {
			MainSubtitles.getInstance().model.removeRow(0);
		}

		MainSubtitles.getInstance().importCheck.setSelected(false);
		MainSubtitles.getInstance().srtCheck.setSelected(false);

		MainSubtitles.getInstance().projectPath = "";
		MainSubtitles.getInstance().hiddenDir = "";
		MainSubtitles.getInstance().videoPath = "";
		MainSubtitles.getInstance().videoLength = "";
		MainSubtitles.getInstance().subtitlesFields = "";
		MainSubtitles.getInstance().workingDir = "";

	}

}
