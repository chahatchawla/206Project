package videoManipulator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - snapshot preview class
 * 
 * Purpose: The purpose of this class is to preview a snapshot from the imported
 * video selected by the user (previewing the snapshot is done in a swing worker
 * so the application does not freeze. This class is used in
 * MainVideoManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/

public class SnapshotPreview {

	private SnapPrevBackgroundTask longTask;

	/**
	 * snapshotPreview Method is performed when the snapshotPrevButton is
	 * clicked. It captures a snapshot from the imported video at the time given
	 * by the user
	 */
	protected void snapshotPreview() {
		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();

		boolean passedOrNot = true;

		// check if the snapshot frame time is empty
		if (MainVideoManipulator.getInstance().timeSnapshot.equals("")) {

			// if the time is not specified by the user
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please enter the snapshot frame time ");

		} else {
			// check for whether the frame time specified by the user is
			// in the right format
			boolean passedTimeCheck = MainVideoManipulator.getInstance().vc
					.timeChecks(MainVideoManipulator.getInstance().timeSnapshot
							.getText().trim());

			// if it is not in the right format, show an error message
			// to the user and allow them to change the frame time
			if (passedTimeCheck == false) {
				MainVideoManipulator.getInstance().timeSnapshot.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please specify the snapshot frame time in hh:mm:ss");
			}

			// check for whether the frame time specified by the user is
			// longer than the length of the video
			if (passedOrNot) {
				int convertedFrameTime = MainVideoManipulator.getInstance().vc
						.convertToSeconds(MainVideoManipulator.getInstance().timeSnapshot
								.getText().trim());
				int lengthOfVideo = (int) (Double
						.parseDouble(MainVideoManipulator.getInstance().videoLength));
				// if it is longer, show an error message to the user
				// and allow them to change the frame time
				if (convertedFrameTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane.showMessageDialog(null,
							"ERROR: snapshot frame time can not be more than the length of the video - "
									+ lengthOfVideo + " seconds");
					MainVideoManipulator.getInstance().timeSnapshot.setText("");
				}
			}

		}
		// if all checks are passed, run a bash command that generates the
		// preview screen for the snapshot option
		if (passedOrNot) {

			longTask = new SnapPrevBackgroundTask();
			longTask.execute();

		}
	}

	/**
	 * SnapPrevBackgroundTask performs the preview snapshot feature in a
	 * SwingWorker
	 * 
	 * @author ccha504
	 * 
	 */
	class SnapPrevBackgroundTask extends SwingWorker<Void, String> {
		StringBuilder cmd = new StringBuilder();

		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {
			// set the snapshot time
			String inputFrameTime = MainVideoManipulator.getInstance().timeSnapshot
					.getText().trim();

			// command to create the preview screenshot
			cmd.append("avconv -i "
					+ MainVideoManipulator.getInstance().videoPath + " -ss "
					+ inputFrameTime + " -f image2 -vframes 1 "
					+ MainVideoManipulator.getInstance().hiddenDir
					+ "/previewSnap.png");
			cmd.append(";");

			// command to preview a screenshot
			cmd.append("avplay -i "
					+ MainVideoManipulator.getInstance().hiddenDir
					+ "/previewSnap.png -window_title previewScreen -x 500 -y 350");

			cmd.append(";");

			MainVideoManipulator.getInstance().previewSnapCmd = cmd.toString();

			// run the preview Command for the snapshot frame
			Process process;
			ProcessBuilder builder;
			try {

				builder = new ProcessBuilder("/bin/bash", "-c",
						MainVideoManipulator.getInstance().previewSnapCmd);
				process = builder.start();
				process.waitFor();
				return null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;

		}
	}

}
