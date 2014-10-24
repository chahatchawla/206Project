package VideoManipulator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;





public class LoopPreview {

	private LoopPrevBackgroundTask longTask;

	protected void loopPreview(){
		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();



		boolean passedOrNot = true;

		// check whether the start time is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckStart = MainVideoManipulator.getInstance().vc.timeChecks(MainVideoManipulator.getInstance().timeStart.getText()
				.trim());

		// check whether the length is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckLength = MainVideoManipulator.getInstance().vc.timeChecks(MainVideoManipulator.getInstance().timeLength.getText()
				.trim());

		// if the start time is in the wrong format, inform the user and
		// allow them
		// to enter the time again
		if (passedTimeCheckStart == false) {
			MainVideoManipulator.getInstance().timeStart.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video start time can only be in the format hh:mm:ss");
		}

		// if the length is in the wrong format, inform the user and allow
		// them
		// to enter the time again
		if (passedTimeCheckLength == false) {
			MainVideoManipulator.getInstance().timeLength.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video length can only be in the format hh:mm:ss");
		}

		// check for whether the given length + start time is smaller than
		// the length of the video or not
		else if (passedOrNot) {
			int convertedStartTime = MainVideoManipulator.getInstance().vc.convertToSeconds(MainVideoManipulator.getInstance().timeStart.getText()
					.trim());
			int convertedLength = MainVideoManipulator.getInstance().vc.convertToSeconds(MainVideoManipulator.getInstance().timeLength.getText()
					.trim());
			int totalTime = convertedStartTime + convertedLength;
			int lengthOfVideo = (int) (Double.parseDouble(MainVideoManipulator.getInstance().videoLength));

			// if totalTime is more than the length of the video, inform the
			// user and allow them to enter the start time and length again
			if (totalTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time + length for loop video can not be more than the length of the video - " + lengthOfVideo+ " seconds");
				MainVideoManipulator.getInstance().timeStart.setText("");
				MainVideoManipulator.getInstance().timeLength.setText("");
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
			cmd.append("avplay -ss " + MainVideoManipulator.getInstance().timeStart.getText() + " -i "
					+ MainVideoManipulator.getInstance().videoPath + " -t " + MainVideoManipulator.getInstance().timeLength.getText()
					+ " -window_title previewScreen -x 500 -y 350");
			cmd.append(";");

			MainVideoManipulator.getInstance().previewFrameCmd = cmd.toString();



		// run the preview Command for the loop frame
		Process process;
		ProcessBuilder builder;
		try {
			
			builder = new ProcessBuilder("/bin/bash", "-c", MainVideoManipulator.getInstance().previewFrameCmd);
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
