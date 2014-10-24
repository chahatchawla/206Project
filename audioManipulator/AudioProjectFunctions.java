package audioManipulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mainPackage.Menu;



public class AudioProjectFunctions {
	/**
	 * enableAudioMan Method enables or disable all the fields in the audio
	 * manipulating tab depending on the state
	 * 
	 * @param state
	 */

	public void enableAudioMan(boolean state) {
		MainAudioManipulator.getInstance().removeCheck.setEnabled(state);
		MainAudioManipulator.getInstance().extractCheck.setEnabled(state);
		MainAudioManipulator.getInstance().replaceCheck.setEnabled(state);
		MainAudioManipulator.getInstance().overlayCheck.setEnabled(state);
		MainAudioManipulator.getInstance().helpButton.setEnabled(state);
		MainAudioManipulator.getInstance().saveButton.setEnabled(state);
	}

	/**
	 * enableExtractOnly() Method enables only extract option (is called when
	 * the input file is an audio file)
	 */

	public void enableExtractOnly() {
		MainAudioManipulator.getInstance().removeCheck.setEnabled(false);
		MainAudioManipulator.getInstance().extractCheck.setEnabled(true);
		MainAudioManipulator.getInstance().replaceCheck.setEnabled(false);
		MainAudioManipulator.getInstance().overlayCheck.setEnabled(false);
		MainAudioManipulator.getInstance().helpButton.setEnabled(true);
		MainAudioManipulator.getInstance().saveButton.setEnabled(true);
	}

	/**
	 * setAllFields Method sets all the fields of the audio class to the
	 * previously saved values
	 * 
	 * @param audioFieldsPath
	 *            : the path of the stored audio fields
	 */
	public void setAllFields(String audioFieldsPath) {

		MainAudioManipulator.getInstance().audioFields = audioFieldsPath;
		File f = new File(MainAudioManipulator.getInstance().audioFields);
		try {

			// read the audioFields file and sets the fields for audio
			// manipulation depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			MainAudioManipulator.getInstance().removeCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainAudioManipulator.getInstance().extractCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainAudioManipulator.getInstance().outputFileName.setText(reader.readLine());
			MainAudioManipulator.getInstance().extractDurationCheck.setSelected(Boolean.parseBoolean(reader
					.readLine()));
			MainAudioManipulator.getInstance().startTimeExtract.setText(reader.readLine());
			MainAudioManipulator.getInstance().lengthExtract.setText(reader.readLine());
			MainAudioManipulator.getInstance().replaceCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainAudioManipulator.getInstance().inputFile = reader.readLine();
			if (MainAudioManipulator.getInstance().inputFile != null) { // If audio was imported, enable playing it
				File audio = new File(MainAudioManipulator.getInstance().inputFile);
				if (audio.exists()) {
					MainAudioManipulator.getInstance().replacePlayButton.setEnabled(true);
				}
			}
			MainAudioManipulator.getInstance().overlayCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			MainAudioManipulator.getInstance().overlayDurationCheck.setSelected(Boolean.parseBoolean(reader
					.readLine()));
			MainAudioManipulator.getInstance().startTimeOverlay.setText(reader.readLine());
			MainAudioManipulator.getInstance().lengthOverlay.setText(reader.readLine());
			MainAudioManipulator.getInstance().audioFiles.clear();
			MainAudioManipulator.getInstance().fullNames.clear();
			String shortNames = reader.readLine();

			if (shortNames !=null){
				String[] splitList = shortNames.split(" ");
				for (String s : splitList) {
					MainAudioManipulator.getInstance().audioFiles.addElement(s);
				}

				String longNames = reader.readLine();
				String[] splitListLong = longNames.split(" ");
				for (String s : splitListLong) {

					MainAudioManipulator.getInstance().fullNames.addElement(s);
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
		MainAudioManipulator.getInstance().projectPath = Menu.getProjectPath();
		File f = new File(MainAudioManipulator.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			MainAudioManipulator.getInstance().hiddenDir = reader.readLine();
			MainAudioManipulator.getInstance().workingDir = reader.readLine();
			MainAudioManipulator.getInstance().videoPath = reader.readLine(); // video path
			MainAudioManipulator.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * refreshAudioMan Method refreshes all the fields in the audio tab
	 */
	public void refreshAudioMan() {

		MainAudioManipulator.getInstance().startTimeExtract.setText("hh:mm:ss");
		MainAudioManipulator.getInstance().lengthExtract.setText("hh:mm:ss");
		MainAudioManipulator.getInstance().startTimeOverlay.setText("hh:mm:ss");
		MainAudioManipulator.getInstance().lengthOverlay.setText("hh:mm:ss");
		MainAudioManipulator.getInstance().outputFileName.setText("");
		MainAudioManipulator.getInstance().removeCheck.setSelected(false);
		MainAudioManipulator.getInstance().extractCheck.setSelected(false);
		MainAudioManipulator.getInstance().replaceCheck.setSelected(false);
		MainAudioManipulator.getInstance().overlayCheck.setSelected(false);
		MainAudioManipulator.getInstance().extractDurationCheck.setSelected(false);
		MainAudioManipulator.getInstance().overlayDurationCheck.setSelected(false);
		MainAudioManipulator.getInstance().audioFiles.clear();
		MainAudioManipulator.getInstance().fullNames.clear();
		MainAudioManipulator.getInstance().inputFile = "";
		MainAudioManipulator.getInstance().projectPath = "";
		MainAudioManipulator.getInstance().hiddenDir = "";
		MainAudioManipulator.getInstance().videoPath = "";
		MainAudioManipulator.getInstance().videoLength = "";
		MainAudioManipulator.getInstance().audioFields = "";
		MainAudioManipulator.getInstance().workingDir = "";

	}

}
