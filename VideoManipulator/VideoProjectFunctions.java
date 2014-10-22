package VideoManipulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mainPackage.Menu;



public class VideoProjectFunctions {
	
	/**
	 * enableVideoMan Method enables or disable all the fields in the video
	 * manipulating tab depending on the state
	 * 
	 * @param state
	 */

	public void enableVideoMan(boolean state) {
		VideoManipulator.getInstance().videoManipulatorLabel.setEnabled(state);
		VideoManipulator.getInstance().filterCheck.setEnabled(state);
		VideoManipulator.getInstance().snapshotCheck.setEnabled(state);
		VideoManipulator.getInstance().loopVideoCheck.setEnabled(state);
		VideoManipulator.getInstance().saveButton.setEnabled(state);
		VideoManipulator.getInstance().helpButton.setEnabled(state);
	}

	/**
	 * setAllFields Method sets all the fields of the video manipulator class to
	 * the previously saved values
	 * 
	 * @param videoFieldsPath
	 *            : the path of the stored audio fields
	 */
	public void setAllFields(String videoFieldsPath) {

		VideoManipulator.getInstance().videoFields = videoFieldsPath;
		File f = new File(VideoManipulator.getInstance().videoFields);
		try {

			// read the videoFields file and sets the fields for video
			// manipulation depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			VideoManipulator.getInstance().snapshotCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			VideoManipulator.getInstance().timeSnapshot.setText(reader.readLine());
			VideoManipulator.getInstance().outputSnapshotName.setText(reader.readLine());

			VideoManipulator.getInstance().loopVideoCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			VideoManipulator.getInstance().timeStart.setText(reader.readLine());
			VideoManipulator.getInstance().timeLength.setText(reader.readLine());
			VideoManipulator.getInstance().outputLoopVideoName.setText(reader.readLine());
			VideoManipulator.getInstance().loop.setText(reader.readLine());

			VideoManipulator.getInstance().filterCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			VideoManipulator.getInstance().filter = reader.readLine();
			VideoManipulator.getInstance().filterList.setSelectedItem(VideoManipulator.getInstance().filter);

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
		VideoManipulator.getInstance().projectPath = Menu.getProjectPath();
		File f = new File(VideoManipulator.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			VideoManipulator.getInstance().hiddenDir = reader.readLine();
			VideoManipulator.getInstance().workingDir = reader.readLine();
			VideoManipulator.getInstance().videoPath = reader.readLine(); // video path
			VideoManipulator.getInstance().videoLength = reader.readLine();
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

		VideoManipulator.getInstance().timeSnapshot.setText("hh:mm:ss");
		VideoManipulator.getInstance().timeStart.setText("hh:mm:ss");
		VideoManipulator.getInstance().timeLength.setText("hh:mm:ss");
		VideoManipulator.getInstance().outputSnapshotName.setText("");
		VideoManipulator.getInstance().outputLoopVideoName.setText("");
		VideoManipulator.getInstance().loop.setText("");

		VideoManipulator.getInstance().filterCheck.setSelected(false);
		VideoManipulator.getInstance().snapshotCheck.setSelected(false);
		VideoManipulator.getInstance().loopVideoCheck.setSelected(false);

		VideoManipulator.getInstance().projectPath = "";
		VideoManipulator.getInstance().hiddenDir = "";
		VideoManipulator.getInstance().videoPath = "";
		VideoManipulator.getInstance().videoLength = "";
		VideoManipulator.getInstance().videoFields = "";
		VideoManipulator.getInstance().workingDir = "";

	}


}
