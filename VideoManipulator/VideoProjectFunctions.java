package VideoManipulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mainPackage.Menu;

/**
 * SoftEng206 Project - video project function class
 * 
 * Purpose: The purpose of this class is to perform all the project functions.
 * It has functions to enable/disable, set and refresh all the fields of video
 * manipulation tab. It also sets the main video info for video manipulation.
 * This class is used in MainVideoManipulator.java and also in multiple classes
 * in the mainPackage for performing these functions while creating a new
 * project or opening a new project etc.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class VideoProjectFunctions {

	/**
	 * enableVideoMan Method enables or disable all the fields in the video
	 * manipulating tab depending on the state
	 * 
	 * @param state
	 */

	public void enableVideoMan(boolean state) {
		MainVideoManipulator.getInstance().videoManipulatorLabel
				.setEnabled(state);
		MainVideoManipulator.getInstance().filterCheck.setEnabled(state);
		MainVideoManipulator.getInstance().snapshotCheck.setEnabled(state);
		MainVideoManipulator.getInstance().loopVideoCheck.setEnabled(state);
		MainVideoManipulator.getInstance().saveButton.setEnabled(state);
		MainVideoManipulator.getInstance().helpButton.setEnabled(state);
	}

	/**
	 * setAllFields Method sets all the fields of the video manipulator class to
	 * the previously saved values
	 * 
	 * @param videoFieldsPath
	 *            : the path of the stored video fields
	 */
	public void setAllFields(String videoFieldsPath) {
		// Reference to BufferedReader :
		// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html

		MainVideoManipulator.getInstance().videoFields = videoFieldsPath;
		File f = new File(MainVideoManipulator.getInstance().videoFields);
		try {

			// read the videoFields file and sets the fields for video
			// manipulation depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			MainVideoManipulator.getInstance().snapshotCheck
					.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainVideoManipulator.getInstance().timeSnapshot.setText(reader
					.readLine());
			MainVideoManipulator.getInstance().outputSnapshotName
					.setText(reader.readLine());

			MainVideoManipulator.getInstance().loopVideoCheck
					.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainVideoManipulator.getInstance().timeStart.setText(reader
					.readLine());
			MainVideoManipulator.getInstance().timeLength.setText(reader
					.readLine());
			MainVideoManipulator.getInstance().outputLoopVideoName
					.setText(reader.readLine());
			MainVideoManipulator.getInstance().loop.setText(reader.readLine());

			MainVideoManipulator.getInstance().filterCheck.setSelected(Boolean
					.parseBoolean(reader.readLine()));
			MainVideoManipulator.getInstance().filter = reader.readLine();
			MainVideoManipulator.getInstance().filterList
					.setSelectedItem(MainVideoManipulator.getInstance().filter);

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
		MainVideoManipulator.getInstance().projectPath = Menu.getInstance()
				.getProjectPath();

		// Reference to BufferedReader :
		// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
		File f = new File(MainVideoManipulator.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			MainVideoManipulator.getInstance().hiddenDir = reader.readLine();
			MainVideoManipulator.getInstance().workingDir = reader.readLine();
			MainVideoManipulator.getInstance().videoPath = reader.readLine();
			MainVideoManipulator.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * refreshVideoMan Method refreshes all the fields in the video manipulator
	 * tab
	 */
	public void refreshVideoMan() {

		MainVideoManipulator.getInstance().timeSnapshot.setText("hh:mm:ss");
		MainVideoManipulator.getInstance().timeStart.setText("hh:mm:ss");
		MainVideoManipulator.getInstance().timeLength.setText("hh:mm:ss");
		MainVideoManipulator.getInstance().outputSnapshotName.setText("");
		MainVideoManipulator.getInstance().outputLoopVideoName.setText("");
		MainVideoManipulator.getInstance().loop.setText("");

		MainVideoManipulator.getInstance().filterCheck.setSelected(false);
		MainVideoManipulator.getInstance().snapshotCheck.setSelected(false);
		MainVideoManipulator.getInstance().loopVideoCheck.setSelected(false);

		MainVideoManipulator.getInstance().projectPath = "";
		MainVideoManipulator.getInstance().hiddenDir = "";
		MainVideoManipulator.getInstance().videoPath = "";
		MainVideoManipulator.getInstance().videoLength = "";
		MainVideoManipulator.getInstance().videoFields = "";
		MainVideoManipulator.getInstance().workingDir = "";

	}

}
