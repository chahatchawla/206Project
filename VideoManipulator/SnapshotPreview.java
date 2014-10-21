package VideoManipulator;


import javax.swing.JOptionPane;
import javax.swing.SwingWorker;



public class SnapshotPreview {

	private SnapPrevBackgroundTask longTask;

	protected void snapshotPreview(){
		// Get the video path and length
		VideoManipulator.getInstance().vpf.setVideoInfo();


		
		boolean passedOrNot = true;

		// check if the snapshot frame time is empty
		if (VideoManipulator.getInstance().timeSnapshot.equals("")) {

			// if the time is not specified by the user
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please enter the snapshot frame time ");

		} else {
			// check for whether the frame time specified by the user is
			// in the right format
			boolean passedTimeCheck = VideoManipulator.getInstance().vc.timeChecks(VideoManipulator.getInstance().timeSnapshot.getText()
					.trim());

			// if it is not in the right format, show an error message
			// to the user and allow them to change the frame time
			if (passedTimeCheck == false) {
				VideoManipulator.getInstance().timeSnapshot.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: please specify the snapshot frame time in hh:mm:ss");
			}

			// check for whether the frame time specified by the user is
			// longer than the length of the video
			if (passedOrNot) {
				int convertedFrameTime = VideoManipulator.getInstance().vc.convertToSeconds(VideoManipulator.getInstance().timeSnapshot
						.getText().trim());
				int lengthOfVideo = (int) (Double.parseDouble(VideoManipulator.getInstance().videoLength));
				// if it is longer, show an error message to the user
				// and allow them to change the frame time
				if (convertedFrameTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane
					.showMessageDialog(null,
							"ERROR: snapshot frame time can not be more than the length of the video - " + lengthOfVideo+ " seconds");
					VideoManipulator.getInstance().timeSnapshot.setText("");
				}
			}

		}
		// if all checks are passed generate the preview screen for the
		// snapshot option
		if (passedOrNot) {

			longTask = new SnapPrevBackgroundTask();
			longTask.execute();

		}
	}

	class SnapPrevBackgroundTask  extends SwingWorker<Void, String> {
		StringBuilder cmd = new StringBuilder();
		@Override
		protected Void doInBackground() throws Exception {
			// set the snapshot time
			String inputFrameTime = VideoManipulator.getInstance().timeSnapshot.getText().trim();

			// command to create the preview screenshot
			cmd.append("avconv -i " + VideoManipulator.getInstance().videoPath + " -ss " + inputFrameTime
					+ " -f image2 -vframes 1 " + VideoManipulator.getInstance().hiddenDir
					+ "/previewSnap.png");
			cmd.append(";");

			// command to preview a screenshot
			cmd.append("avplay -i "
					+ VideoManipulator.getInstance().hiddenDir
					+ "/previewSnap.png -window_title previewScreen -x 500 -y 350");

			cmd.append(";");

			VideoManipulator.getInstance().previewSnapCmd = cmd.toString();
			

			// run the preview Command for the loop frame
			Process process;
			ProcessBuilder builder;
			try {

				builder = new ProcessBuilder("/bin/bash", "-c", VideoManipulator.getInstance().previewSnapCmd);
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
