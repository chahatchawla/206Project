package audioManipulator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - overlay list class
 * 
 * Purpose: The purpose of this class is to perform all the functions related to
 * the overlay list feature. It allows the user to input an audio file into the
 * file, delete the file or play the file (playing the file is done in a swing
 * worker so the application does not freeze. This class is used in
 * MainAudioManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class OverlayList {

	private OverlayBackgroundTask longTask;

	/**
	 * inputOverlay Method is performed when the inputOverlayButton is clicked.
	 * It adds the audio file to overlay list
	 */
	@SuppressWarnings("unchecked")
	protected void inputOverlay() {

		// Reference for JFileChooser():
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html

		String audioFile;
		String fullName;
		// Show only correct extension type file by default
		MainAudioManipulator.getInstance().chooser
				.setFileFilter(MainAudioManipulator.getInstance().filter);
		int returnVal = MainAudioManipulator.getInstance().chooser
				.showOpenDialog(null);
		// Store the chosen file
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			// get the name and the absolute path of the chosen audio file
			audioFile = MainAudioManipulator.getInstance().chooser
					.getSelectedFile().getName();
			fullName = MainAudioManipulator.getInstance().chooser
					.getSelectedFile().getAbsolutePath();

			// Reference to File.probeContentType
			// http://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileTypeDetector.html

			// check for whether the input audio files chosen by the user
			// are audio files
			File file = new File(fullName);
			Path path = file.toPath();
			String type = "";
			try {
				type = Files.probeContentType(path);
			} catch (IOException f) {
				f.printStackTrace();
			}

			// if the file is NOT an audio file, notify the user and
			// allow them to select again
			if (!(type.equals("audio/mpeg"))) {
				JOptionPane
						.showMessageDialog(
								null,
								"ERROR: "
										+ audioFile
										+ " does not refer to a valid audio file. Please select a new input file!");
			}

			else {

				// if the file is an audio file, add it to the list of audio
				// files and the full name files
				MainAudioManipulator.getInstance().audioFiles
						.addElement(audioFile);
				MainAudioManipulator.getInstance().fullNames
						.addElement(fullName);
			}

		}

	}

	/**
	 * deleteOverlay Method is performed when the deleteOverlayButton is
	 * clicked. It deletes the selected audio file from the overlay list
	 */
	protected void deleteOverlay() {

		// check if the audio file list is empty
		if (MainAudioManipulator.getInstance().audioFiles.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"ERROR: audio file list is empty, cannot perform DELETE");
		}
		// if it is not empty
		else {

			// check if something is selected in the list
			if (MainAudioManipulator.getInstance().audioFilesList
					.getSelectedIndex() != -1) {

				// find the index of the selected item
				int index;
				index = MainAudioManipulator.getInstance().audioFilesList
						.getSelectedIndex();
				// remove the element from all the lists
				MainAudioManipulator.getInstance().audioFiles
						.removeElementAt(index);
				MainAudioManipulator.getInstance().fullNames
						.removeElementAt(index);
			}
			// if nothing is selected, prompt the user and allow them to
			// select a file
			else {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please select an audio file from the list to delete");
			}
		}
	}

	/**
	 * playOverlay Method is performed when the playOverlayButton is clicked. It
	 * plays the selected audio file in a background task.
	 */
	protected void playOverlay() {
		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();

		// check if the audio file list is empty
		if (MainAudioManipulator.getInstance().audioFiles.isEmpty()) {
			JOptionPane
					.showMessageDialog(null,
							"ERROR: audio file list is empty , cannot perform PLAY AUDIO");
		}

		// if the audio file list is not empty
		else {
			// check if something is selected in the list
			if (MainAudioManipulator.getInstance().audioFilesList
					.getSelectedIndex() != -1) {

				// if something is selected, play the audio in a swingworker
				longTask = new OverlayBackgroundTask();
				longTask.execute();

			}

			// if nothing is selected, prompt the user and allow them to select
			// a file
			else {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please select an audio file from the list to play");
			}
		}

	}

	/**
	 * OverlayBackgroundTask performs the play audio in the overlay feature in a
	 * SwingWorker
	 * 
	 * @author ccha504
	 * 
	 */
	class OverlayBackgroundTask extends SwingWorker<Void, String> {
		StringBuilder cmd = new StringBuilder();

		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {
			String playFile;
			int index;
			index = MainAudioManipulator.getInstance().audioFilesList
					.getSelectedIndex();
			playFile = MainAudioManipulator.getInstance().fullNames.get(index)
					.toString();

			// Reference: https://libav.org/avplay.html

			// bash command to play the audio
			String cmd = "avplay -i " + playFile
					+ " -window_title playChosenAudio -x 400 -y 100";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			@SuppressWarnings("unused")
			Process process;
			try {
				process = builder.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;

		}
	}
}
