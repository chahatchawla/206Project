package VideoManipulator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;



public class FilterPreview {

	private FilterPrevBackgroundTask longTask;

	protected void filterPreview(){
		// Get the video path and length
		VideoManipulator.getInstance().vpf.setVideoInfo();


		boolean passedOrNot = true;
		// check if one of the filter options are selected
		if (VideoManipulator.getInstance().filter.equals("")) {

			// if none of the six are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the filter options");
		}
		// if all checks are passed generate the preview screen for the
		// 'extract then loop' option
		if (passedOrNot) {



			longTask = new FilterPrevBackgroundTask();
			longTask.execute();

		}
	}

	class FilterPrevBackgroundTask  extends SwingWorker<Void, String> {

		@Override
		protected Void doInBackground() throws Exception {
			StringBuilder cmd = new StringBuilder();

			// set the snapshot time to the 1st second of the video
			String inputFrameTime = "00:00:01";

			// command to take a screenshot from the video

			cmd.append("avconv -i " + VideoManipulator.getInstance().videoPath + " -ss " + inputFrameTime
					+ " -f image2 -vframes 1 " + VideoManipulator.getInstance().hiddenDir + "/preview.png");
			cmd.append(";");

			// if the filter is negate
			if (VideoManipulator.getInstance().filter.equals("Negate")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf \"negate=5\" -strict experimental -window_title previewScreen -x 500 -y 350");

			}
			// if the filter is blur
			else if (VideoManipulator.getInstance().filter.equals("Blur")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf \"boxblur=2:1:0:0:0:0\" -window_title previewScreen -x 500 -y 350");

			}
			// if the filter is horizontal flip
			else if (VideoManipulator.getInstance().filter.equals("Horizontal Flip")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf \"hflip\" -strict experimental -window_title previewScreen -x 500 -y 350");

			}
			// if the filter is vertical flip
			else if (VideoManipulator.getInstance().filter.equals("Vertical Flip")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf \"vflip\" -strict experimental -window_title previewScreen -x 500 -y 350");

			}
			// if the filter is fade in
			else if (VideoManipulator.getInstance().filter.equals("Fade In")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf fade=in:00:30 -strict experimental -window_title previewScreen -x 500 -y 350");

			}
			// if the filter is transpose
			else if (VideoManipulator.getInstance().filter.equals("Transpose")) {

				cmd.append("avplay -i "
						+ VideoManipulator.getInstance().hiddenDir
						+ "/preview.png"
						+ " -vf transpose=dir=clock_flip -strict experimental -window_title previewScreen -x 500 -y 350");

			}

			cmd.append(";");
			VideoManipulator.getInstance().previewCmd = cmd.toString();

			// run the preview Command for the loop frame
			Process process;
			ProcessBuilder builder;
			try {

				builder = new ProcessBuilder("/bin/bash", "-c", VideoManipulator.getInstance().previewCmd);
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
