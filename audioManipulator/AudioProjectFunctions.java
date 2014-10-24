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
		AudioManipulator.getInstance().removeCheck.setEnabled(state);
		AudioManipulator.getInstance().extractCheck.setEnabled(state);
		AudioManipulator.getInstance().replaceCheck.setEnabled(state);
		AudioManipulator.getInstance().overlayCheck.setEnabled(state);
		AudioManipulator.getInstance().helpButton.setEnabled(state);
		AudioManipulator.getInstance().saveButton.setEnabled(state);
	}

	/**
	 * enableExtractOnly() Method enables only extract option (is called when
	 * the input file is an audio file)
	 */

	public void enableExtractOnly() {
		AudioManipulator.getInstance().removeCheck.setEnabled(false);
		AudioManipulator.getInstance().extractCheck.setEnabled(true);
		AudioManipulator.getInstance().replaceCheck.setEnabled(false);
		AudioManipulator.getInstance().overlayCheck.setEnabled(false);
		AudioManipulator.getInstance().helpButton.setEnabled(true);
		AudioManipulator.getInstance().saveButton.setEnabled(true);
	}

	/**
	 * setAllFields Method sets all the fields of the audio class to the
	 * previously saved values
	 * 
	 * @param audioFieldsPath
	 *            : the path of the stored audio fields
	 */
	public void setAllFields(String audioFieldsPath) {

		AudioManipulator.getInstance().audioFields = audioFieldsPath;
		File f = new File(AudioManipulator.getInstance().audioFields);
		try {

			// read the audioFields file and sets the fields for audio
			// manipulation depending on the last save

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			AudioManipulator.getInstance().removeCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			AudioManipulator.getInstance().extractCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			AudioManipulator.getInstance().outputFileName.setText(reader.readLine());
			AudioManipulator.getInstance().extractDurationCheck.setSelected(Boolean.parseBoolean(reader
					.readLine()));
			AudioManipulator.getInstance().startTimeExtract.setText(reader.readLine());
			AudioManipulator.getInstance().lengthExtract.setText(reader.readLine());
			AudioManipulator.getInstance().replaceCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			AudioManipulator.getInstance().inputFile = reader.readLine();
			if (AudioManipulator.getInstance().inputFile != null) { // If audio was imported, enable playing it
				File audio = new File(AudioManipulator.getInstance().inputFile);
				if (audio.exists()) {
					AudioManipulator.getInstance().replacePlayButton.setEnabled(true);
				}
			}
			AudioManipulator.getInstance().overlayCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
			AudioManipulator.getInstance().overlayDurationCheck.setSelected(Boolean.parseBoolean(reader
					.readLine()));
			AudioManipulator.getInstance().startTimeOverlay.setText(reader.readLine());
			AudioManipulator.getInstance().lengthOverlay.setText(reader.readLine());
			AudioManipulator.getInstance().audioFiles.clear();
			AudioManipulator.getInstance().fullNames.clear();
			String shortNames = reader.readLine();

			if (shortNames !=null){
				String[] splitList = shortNames.split(" ");
				for (String s : splitList) {
					AudioManipulator.getInstance().audioFiles.addElement(s);
				}

				String longNames = reader.readLine();
				String[] splitListLong = longNames.split(" ");
				for (String s : splitListLong) {

					AudioManipulator.getInstance().fullNames.addElement(s);
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
		AudioManipulator.getInstance().projectPath = Menu.getProjectPath();
		File f = new File(AudioManipulator.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			AudioManipulator.getInstance().hiddenDir = reader.readLine();
			AudioManipulator.getInstance().workingDir = reader.readLine();
			AudioManipulator.getInstance().videoPath = reader.readLine(); // video path
			AudioManipulator.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * refreshAudioMan Method refreshes all the fields in the audio tab
	 */
	public void refreshAudioMan() {

		AudioManipulator.getInstance().startTimeExtract.setText("hh:mm:ss");
		AudioManipulator.getInstance().lengthExtract.setText("hh:mm:ss");
		AudioManipulator.getInstance().startTimeOverlay.setText("hh:mm:ss");
		AudioManipulator.getInstance().lengthOverlay.setText("hh:mm:ss");
		AudioManipulator.getInstance().outputFileName.setText("");
		AudioManipulator.getInstance().removeCheck.setSelected(false);
		AudioManipulator.getInstance().extractCheck.setSelected(false);
		AudioManipulator.getInstance().replaceCheck.setSelected(false);
		AudioManipulator.getInstance().overlayCheck.setSelected(false);
		AudioManipulator.getInstance().extractDurationCheck.setSelected(false);
		AudioManipulator.getInstance().overlayDurationCheck.setSelected(false);
		AudioManipulator.getInstance().audioFiles.clear();
		AudioManipulator.getInstance().fullNames.clear();
		AudioManipulator.getInstance().inputFile = "";
		AudioManipulator.getInstance().projectPath = "";
		AudioManipulator.getInstance().hiddenDir = "";
		AudioManipulator.getInstance().videoPath = "";
		AudioManipulator.getInstance().videoLength = "";
		AudioManipulator.getInstance().audioFields = "";
		AudioManipulator.getInstance().workingDir = "";

	}

}
