package VideoManipulator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;





public class LoopPreview {

	private LoopPrevBackgroundTask longTask;

	protected void loopPreview(){
		// Get the video path and length
		VideoManipulator.getInstance().vpf.setVideoInfo();



		boolean passedOrNot = true;

		// check whether the start time is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckStart = VideoManipulator.getInstance().vc.timeChecks(VideoManipulator.getInstance().timeStart.getText()
				.trim());

		// check whether the length is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckLength = VideoManipulator.getInstance().vc.timeChecks(VideoManipulator.getInstance().timeLength.getText()
				.trim());

		// if the start time is in the wrong format, inform the user and
		// allow them
		// to enter the time again
		if (passedTimeCheckStart == false) {
			VideoManipulator.getInstance().timeStart.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video start time can only be in the format hh:mm:ss");
		}

		// if the length is in the wrong format, inform the user and allow
		// them
		// to enter the time again
		if (passedTimeCheckLength == false) {
			VideoManipulator.getInstance().timeLength.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video length can only be in the format hh:mm:ss");
		}

		// check for whether the given length + start time is smaller than
		// the length of the video or not
		else if (passedOrNot) {
			int convertedStartTime = VideoManipulator.getInstance().vc.convertToSeconds(VideoManipulator.getInstance().timeStart.getText()
					.trim());
			int convertedLength = VideoManipulator.getInstance().vc.convertToSeconds(VideoManipulator.getInstance().timeLength.getText()
					.trim());
			int totalTime = convertedStartTime + convertedLength;
			int lengthOfVideo = (int) (Double.parseDouble(VideoManipulator.getInstance().videoLength));

			// if totalTime is more than the length of the video, inform the
			// user and allow them to enter the start time and length again
			if (totalTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time + length for loop video can not be more than the length of the video - " + lengthOfVideo+ " seconds");
				VideoManipulator.getInstance().timeStart.setText("");
				VideoManipulator.getInstance().timeLength.setText("");
			}
		}

		// if all checks are passed generate the preview screen for the
		// 'extract then loop' option
		if (passedOrNot) {

			longTask = new LoopPrevBackgroundTask();
			longTask.execute();

		}
	}

	class LoopPrevBackgroundTask  extends SwingWorker<Void, String> {

		@Override
		protected Void doInBackground() throws Exception {
			StringBuilder cmd = new StringBuilder();
			// command to preview extracted video
			cmd.append("avplay -ss " + VideoManipulator.getInstance().timeStart.getText() + " -i "
					+ VideoManipulator.getInstance().videoPath + " -t " + VideoManipulator.getInstance().timeLength.getText()
					+ " -window_title previewScreen -x 500 -y 350");
			cmd.append(";");

			VideoManipulator.getInstance().previewFrameCmd = cmd.toString();



		// run the preview Command for the loop frame
		Process process;
		ProcessBuilder builder;
		try {
			
			builder = new ProcessBuilder("/bin/bash", "-c", VideoManipulator.getInstance().previewFrameCmd);
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
