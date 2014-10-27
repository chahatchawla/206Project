package videoManipulator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - filter preview class
 * 
 * Purpose: The purpose of this class is to play a small segment of the imported
 * video with the selected filter (playing the video is done in a swing worker
 * so the application does not freeze. This class is used in
 * MainVideoManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/

public class FilterPreview {

	private FilterPrevBackgroundTask longTask;

	/**
	 * filterPreview Method is performed when the prevButton is clicked. It
	 * plays a small segment of the imported video with the selected filter in a
	 * background task.
	 */
	protected void filterPreview() {
		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();

		boolean passedOrNot = true;
		// check if one of the filter options are selected
		if (MainVideoManipulator.getInstance().filter.equals("")) {

			// if none of the six are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the filter options");
		}
		// if all checks are passed, run a bash command that generates the
		// preview screen for the filter option
		if (passedOrNot) {

			longTask = new FilterPrevBackgroundTask();
			longTask.execute();

		}
	}

	/**
	 * FilterPrevBackgroundTask performs the preview video filter feature in a
	 * SwingWorker
	 * 
	 * @author ccha504
	 * 
	 */
	class FilterPrevBackgroundTask extends SwingWorker<Void, String> {
		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {
			StringBuilder cmd = new StringBuilder();

			// bash command for if the filter is negate
			if (MainVideoManipulator.getInstance().filter.equals("Negate")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf \"negate=5\" -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}
			// bash command for if the filter is blur
			else if (MainVideoManipulator.getInstance().filter.equals("Blur")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf \"boxblur=2:1:0:0:0:0\" -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}
			// bash command for if the filter is horizontal flip
			else if (MainVideoManipulator.getInstance().filter
					.equals("Horizontal Flip")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf \"hflip\" -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}
			// bash command for if the filter is vertical flip
			else if (MainVideoManipulator.getInstance().filter
					.equals("Vertical Flip")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf \"vflip\" -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}
			// bash command for if the filter is fade in
			else if (MainVideoManipulator.getInstance().filter
					.equals("Fade In")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf fade=in:00:30 -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}
			// bash command for if the filter is transpose
			else if (MainVideoManipulator.getInstance().filter
					.equals("Transpose")) {

				cmd.append("avplay -i "
						+ mainPackage.Menu.getInstance().inputVideo
						+ " -vf transpose=dir=clock_flip -strict experimental -t 5 -window_title previewScreen -x 500 -y 350");

			}

			cmd.append(";");
			MainVideoManipulator.getInstance().previewCmd = cmd.toString();

			// run the preview Command for the filter option
			Process process;
			ProcessBuilder builder;
			try {

				builder = new ProcessBuilder("/bin/bash", "-c",
						MainVideoManipulator.getInstance().previewCmd);
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
