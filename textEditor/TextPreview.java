package textEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - text preview class
 * 
 * Purpose: The purpose of this class is to preview the created screen (preview
 * is done in a swing worker so the application does not freeze. This class is
 * used in MainTextEditor.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/
public class TextPreview {

	private String inputFrameTime;
	private File filePreview;
	private TextPrevBackgroundTask longTask;

	/**
	 * textPreview Method is performed when the prevBtn is clicked. It creates
	 * the selected title/credit screen on a snapshot
	 */
	protected void textPreview() {

		// Get the video path and length
		MainTextEditor.getInstance().tpf.setVideoInfo();

		// set the the FontSettings
		MainTextEditor.getInstance().tpf.setTitleFontSettings();

		boolean passedOrNot = true;

		// create a file for preview text
		filePreview = new File(MainTextEditor.getInstance().hiddenDir
				+ "/PreviewText.txt");

		// Reference for writing to file:
		// http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
		FileWriter fw;
		try {
			fw = new FileWriter(filePreview, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter x = new PrintWriter(bw);
			x.println(MainTextEditor.getInstance().addTextArea.getText());
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// check if one of the background image options are selected
		if (!MainTextEditor.getInstance().defaultCheck.isSelected()
				&& !MainTextEditor.getInstance().overlayCheck.isSelected()
				&& !MainTextEditor.getInstance().frameCheck.isSelected()) {

			// if none of the three are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the background options");
			filePreview.delete();
		}
		// if the user wants to preview the title screen
		else if (MainTextEditor.getInstance().screenType.equals("Title Screen")) {
			// if they select a frame from the video
			if (MainTextEditor.getInstance().backgroundImageOption == 2) {
				// check for whether the frame time specified by the user is
				// longer than the length of the video
				if (passedOrNot) {
					int convertedFrameTime = MainTextEditor.getInstance().tc
							.convertToSeconds(MainTextEditor.getInstance().addTimeFrame
									.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(MainTextEditor.getInstance().videoLength));
					// if it is longer, show an error message to the user
					// and allow them to change the frame time
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - "
										+ lengthOfVideo + " seconds");
						MainTextEditor.getInstance().addTimeFrame.setText("");
						filePreview.delete();
					}
				}
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the time that the user
				// specified
				inputFrameTime = MainTextEditor.getInstance().addTimeFrame
						.getText().trim();
			} else {
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at 00:00:01 for both the
				// overlay option and the default option
				inputFrameTime = "00:00:01";
			}
		}
		// if the user wants to preview the credit screen
		else {
			// if they select a frame from the video
			if (MainTextEditor.getInstance().backgroundImageOption == 2) {
				// check for whether the frame time specified by the user is
				// in the right format
				int passedTimeCheck = MainTextEditor.getInstance().tc
						.timeChecks(MainTextEditor.getInstance().addTimeFrame
								.getText().trim());
				// if it is not in the right format, show an error message
				// to the user
				// and allow them to change the frame time
				if (passedTimeCheck == 1) {
					MainTextEditor.getInstance().addTimeFrame.setText("");
					passedOrNot = false;
					JOptionPane.showMessageDialog(null,
							"ERROR: please specify the frame time in hh:mm:ss");
					filePreview.delete();
				}
				// check for whether the frame time specified by the user is
				// longer than the length of the video
				if (passedOrNot) {
					int convertedFrameTime = MainTextEditor.getInstance().tc
							.convertToSeconds(MainTextEditor.getInstance().addTimeFrame
									.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(MainTextEditor.getInstance().videoLength));
					// if it is longer, show an error message to the user
					// and allow them to change the frame time
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - "
										+ lengthOfVideo + " seconds");
						MainTextEditor.getInstance().addTimeFrame.setText("");
						filePreview.delete();
					}
				}
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the time that the user
				// specified
				inputFrameTime = MainTextEditor.getInstance().addTimeFrame
						.getText().trim();
			} else if (MainTextEditor.getInstance().backgroundImageOption == 0) {
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the last frame of the video
				// for the overlay option
				int time = (int) (Double.parseDouble(MainTextEditor
						.getInstance().videoLength));
				String videoLength = "" + time;
				inputFrameTime = videoLength;
			} else {
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at 00:00:01 for both the
				// default option
				inputFrameTime = "00:00:01";
			}
		}
		// if all checks are passed execute the bash command to create preview
		// screen
		if (passedOrNot) {

			longTask = new TextPrevBackgroundTask();
			longTask.execute();
		}
	}

	/**
	 * TextPrevBackgroundTask performs the preview operation in the text editing
	 * feature in a SwingWorker
	 * 
	 * @author ccha504
	 * 
	 */
	class TextPrevBackgroundTask extends SwingWorker<Void, String> {
		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {

			StringBuilder cmd = new StringBuilder();
			// command to take a screenshot from the video at the given
			// inputFrametime
			cmd.append("avconv -i " + MainTextEditor.getInstance().videoPath
					+ " -ss " + inputFrameTime + " -f image2 -vframes 1 "
					+ MainTextEditor.getInstance().hiddenDir + "/out.png");
			cmd.append(";");
			// command to add the text to the screenshot
			cmd.append("avplay -i " + MainTextEditor.getInstance().hiddenDir
					+ "/out.png -strict experimental -vf \"drawtext=fontfile='"
					+ MainTextEditor.getInstance().fontDir
					+ MainTextEditor.getInstance().fontName + "':textfile='"
					+ filePreview
					+ "':x=(main_w-text_w)/2:y=(main_h-text_h)/2:fontsize="
					+ MainTextEditor.getInstance().fontSize + ":fontcolor="
					+ MainTextEditor.getInstance().fontColour
					+ "\" -window_title previewScreen -x 500 -y 350 ");
			cmd.append(";");

			MainTextEditor.getInstance().previewCommand = cmd.toString();
			// run the preview Command
			Process process;
			ProcessBuilder builder;

			builder = new ProcessBuilder("/bin/bash", "-c",
					MainTextEditor.getInstance().previewCommand);
			try {
				process = builder.start();
				process.waitFor();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// delete the preview file so the user can generate another
			// preview
			filePreview.delete();
			return null;
		}

	}
}
