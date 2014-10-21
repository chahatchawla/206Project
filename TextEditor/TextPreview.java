package TextEditor;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;



public class TextPreview {
	private StringBuilder cmd = new StringBuilder();
	private String inputFrameTime;
	private File filePreview;
	private TextPrevBackgroundTask longTask;
	
	protected void textPreview() {
		
		// Get the video path and length
		TextEditor.getInstance().tpf.setVideoInfo();
		
		
		// set the the FontSettings
		TextEditor.getInstance().tpf.setTitleFontSettings();
		
		boolean passedOrNot = true;

		// create a file for preview text
		filePreview = new File(TextEditor.getInstance().hiddenDir + "/PreviewText.txt");

		// Reference for writing to file:
		// http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

		FileWriter fw;
		try {
			fw = new FileWriter(filePreview, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter x = new PrintWriter(bw);
			x.println(TextEditor.getInstance().addTextArea.getText());
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// check if one of the background image options are selected
		if (!TextEditor.getInstance().defaultCheck.isSelected() && !TextEditor.getInstance().overlayCheck.isSelected()
				&& !TextEditor.getInstance().frameCheck.isSelected()) {

			// if none of the three are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the background options");
			filePreview.delete();
		}
		// if the user wants to preview the title screen
		if (TextEditor.getInstance().screenType.equals("Title Screen")) {
			// if they select a frame from the video
			if (TextEditor.getInstance().backgroundImageOption == 2) {
				// check for whether the frame time specified by the user is
				// longer than the length of the video
				if (passedOrNot) {
					int convertedFrameTime = TextEditor.getInstance().tc.convertToSeconds(TextEditor.getInstance().addTimeFrame
							.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(TextEditor.getInstance().videoLength));
					// if it is longer, show an error message to the user
					// and allow them to change the frame time
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane
						.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - " + lengthOfVideo+ " seconds");
						TextEditor.getInstance().addTimeFrame.setText("");
						filePreview.delete();
					}
				}
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the time that the user
				// specified
				inputFrameTime = TextEditor.getInstance().addTimeFrame.getText().trim();
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
			if (TextEditor.getInstance().backgroundImageOption == 2) {
				// check for whether the frame time specified by the user is
				// in the right format
				int passedTimeCheck = TextEditor.getInstance().tc.timeChecks(TextEditor.getInstance().addTimeFrame.getText()
						.trim());
				// if it is not in the right format, show an error message
				// to the user
				// and allow them to change the frame time
				if (passedTimeCheck == 1) {
					TextEditor.getInstance().addTimeFrame.setText("");
					passedOrNot = false;
					JOptionPane
					.showMessageDialog(null,
							"ERROR: please specify the frame time in hh:mm:ss");
					filePreview.delete();
				}
				// check for whether the frame time specified by the user is
				// longer than the length of the video
				if (passedOrNot) {
					int convertedFrameTime = TextEditor.getInstance().tc.convertToSeconds(TextEditor.getInstance().addTimeFrame
							.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(TextEditor.getInstance().videoLength));
					// if it is longer, show an error message to the user
					// and allow them to change the frame time
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane
						.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - " + lengthOfVideo+ " seconds");
						TextEditor.getInstance().addTimeFrame.setText("");
						filePreview.delete();
					}
				}
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the time that the user
				// specified
				inputFrameTime = TextEditor.getInstance().addTimeFrame.getText().trim();
			} else if (TextEditor.getInstance().backgroundImageOption == 0) {
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at the last frame of the video
				// for the overlay option
				int time = (int) (Double.parseDouble(TextEditor.getInstance().videoLength));
				String videoLength = "" + time;
				inputFrameTime = videoLength;
			} else {
				// if the checks are passed, the inputFrameTime is set so
				// the screenshot is taken at 00:00:01 for both the
				// default option
				inputFrameTime = "00:00:01";
			}
		}
		// if all checks are passed generate the preview screen
		if (passedOrNot) {


			longTask = new TextPrevBackgroundTask();
			longTask.execute();
		}
	}


	class TextPrevBackgroundTask  extends SwingWorker<Void, String> {

		@Override
		protected Void doInBackground() throws Exception {
			// command to take a screenshot from the video at the given
			// inputFrametime
			cmd.append("avconv -i " + TextEditor.getInstance().videoPath + " -ss " + inputFrameTime
					+ " -f image2 -vframes 1 " + TextEditor.getInstance().hiddenDir + "/out.png");
			cmd.append(";");
			// command to add the text to the screenshot
			cmd.append("avplay -i "
					+ TextEditor.getInstance().hiddenDir
					+ "/out.png -strict experimental -vf \"drawtext=fontfile='"
					+ TextEditor.getInstance().fontDir + TextEditor.getInstance().fontName + "':textfile='" + filePreview
					+ "':x=(main_w-text_w)/2:y=(main_h-text_h)/2:fontsize="
					+ TextEditor.getInstance().fontSize + ":fontcolor=" + TextEditor.getInstance().fontColour
					+ "\" -window_title previewScreen -x 500 -y 350 ");
			cmd.append(";");
			
			
			TextEditor.getInstance().previewCommand = cmd.toString();
			// run the preview Command
			Process process;
			ProcessBuilder builder;
			builder = new ProcessBuilder("/bin/bash", "-c", TextEditor.getInstance().previewCommand);
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
