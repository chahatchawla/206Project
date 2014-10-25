package audioManipulator;

import java.io.IOException;

import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - replace preview class
 * 
 * Purpose: The purpose of this class is to play the file chosen to replace the
 * audio (playing the file is done in a swing worker so the application does not
 * freeze. This class is used in MainAudioManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/
public class ReplacePreview {

	private ReplaceBackgroundTask longTask;
	
	/**
	 * replacePreview Method is performed when the playReplaceButton is clicked.
	 * It plays the selected audio file in a background task.
	 */
	protected void replacePreview() {

		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();

		// Run the allChecksReplace() to check whether or not the user
		// inputs
		// are valid
		boolean replaceChecksPassed = MainAudioManipulator.getInstance().ac
				.allChecksReplace();

		// If all checks are passed, run a bash command avplay to enable the
		// user to preview the audio file they chose
		if (replaceChecksPassed) {

			longTask = new ReplaceBackgroundTask();
			longTask.execute();

		}

	}

	/**
	 * ReplaceBackgroundTask performs the play audio in the replace feature in a
	 * SwingWorker
	 * 
	 * @author ccha504
	 * 
	 */
	class ReplaceBackgroundTask extends SwingWorker<Void, String> {
		StringBuilder cmd = new StringBuilder();
		
		
		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {
			
			//Reference: https://libav.org/avplay.html
				
			// bash command to play the audio
			String cmd = "avplay -i "
					+ MainAudioManipulator.getInstance().inputFile
					+ " -window_title playChosenAudio -x 400 -y 100";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
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
