package VideoManipulator;

/**
 * SoftEng206 Project - video background command class
 * 
 * Purpose: The purpose of this class is to create the command that corresponds
 * to the background task for video manipulation during export. It identifies
 * whether snapshot, extract then loop and/or filter is selected by the user and
 * creates the corresponding bash command using the user input. This class is
 * used in MainVideoMainpulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class VideoBackgroundCommand {

	private String firstInput;
	private String lastOutput;

	/**
	 * makeVideoCommand is a method that creates the bash commands in the form
	 * of a string according to all the options selected by the user
	 * 
	 * @param input
	 * @param output
	 * @return the final command
	 */

	protected String makeVideoCommand(String input, String output) {

		firstInput = input;
		lastOutput = output;

		// Reference for all the avconv commands
		// https://libav.org/avconv.html and a combination of many searches
		// found on google, final command selected after a lot of trials and
		// testing

		// if filter is enabled, constructs the command for adding the
		// filter to the input video
		if (MainVideoManipulator.getInstance().filterEnable) {

			StringBuilder bigFilterCmd = new StringBuilder();

			// bash command if the filter is negate
			if (MainVideoManipulator.getInstance().filter.equals("Negate")) {

				bigFilterCmd.append("avconv -i " + firstInput
						+ " -vf \"negate=5\" -strict experimental -y "
						+ lastOutput);

			}
			// bash command if the filter is blur
			else if (MainVideoManipulator.getInstance().filter.equals("Blur")) {

				bigFilterCmd.append("avconv -i " + firstInput
						+ " -vf \"boxblur=2:1:0:0:0:0\" -codec:a copy -y "
						+ lastOutput);

			}
			// bash command if the filter is horizontal flip
			else if (MainVideoManipulator.getInstance().filter
					.equals("Horizontal Flip")) {

				bigFilterCmd.append("avconv -i " + firstInput
						+ " -vf \"hflip\" -strict experimental -y "
						+ lastOutput);

			}
			// bash command if the filter is vertical flip
			else if (MainVideoManipulator.getInstance().filter
					.equals("Vertical Flip")) {

				bigFilterCmd.append("avconv -i " + firstInput
						+ " -vf \"vflip\" -strict experimental -y "
						+ lastOutput);

			}
			// bash command if the filter is fade in
			else if (MainVideoManipulator.getInstance().filter
					.equals("Fade In")) {

				bigFilterCmd
						.append("avconv -i "
								+ firstInput
								+ " -vf fade=in:00:30 -strict experimental -codec:v libx264 -y "
								+ lastOutput);

			}
			// bash command if the filter is transpose
			else if (MainVideoManipulator.getInstance().filter
					.equals("Transpose")) {

				bigFilterCmd
						.append("avconv -i "
								+ firstInput
								+ " -vf transpose=dir=clock_flip -strict experimental -codec:v libx264 -y "
								+ lastOutput);

			}
			bigFilterCmd.append(";");
			MainVideoManipulator.getInstance().filterCmd = bigFilterCmd
					.toString();
		}

		// if snapshot is enabled, constructs the command for snapshot
		if (MainVideoManipulator.getInstance().snapshotEnable) {

			StringBuilder bigsSnapshotCmd = new StringBuilder();

			// take a screen shot at the time given by the user bash command
			bigsSnapshotCmd.append("avconv -i "
					+ firstInput
					+ " -ss "
					+ MainVideoManipulator.getInstance().timeSnapshot.getText()
					+ " -f image2 -vframes 1 -y "
					+ MainVideoManipulator.getInstance().workingDir
					+ "/"
					+ MainVideoManipulator.getInstance().outputSnapshotName
							.getText() + ".png");
			bigsSnapshotCmd.append(";");
			MainVideoManipulator.getInstance().snapshotCmd = bigsSnapshotCmd
					.toString();

			// if filter is not enabled,bash command to create the exported
			// output
			if (!MainVideoManipulator.getInstance().filterEnable) {

				bigsSnapshotCmd.append("avconv -i ");
				bigsSnapshotCmd.append(firstInput);
				bigsSnapshotCmd.append(" -strict experimental -y ");
				bigsSnapshotCmd.append(lastOutput);
				bigsSnapshotCmd.append(";");

			}

		}

		// if loopVideo is enabled, constructs the command for making a
		// loop video of a chosen frame
		if (MainVideoManipulator.getInstance().loopVideoEnable) {

			StringBuilder bigLoopVideoCmd = new StringBuilder();
			// get the number of times the user wants to loop
			int loopNumber = Integer.parseInt(MainVideoManipulator
					.getInstance().loop.getText());

			// create the looped command
			StringBuilder loop = new StringBuilder();
			for (int i = 0; i < loopNumber; i++) {

				if (loopNumber - 1 == i) {
					loop.append(MainVideoManipulator.getInstance().hiddenDir
							+ "/file1.ts\"");

				} else {
					loop.append(MainVideoManipulator.getInstance().hiddenDir
							+ "/file1.ts");
					loop.append("|");
				}
			}
			String loopString = loop.toString();

			// extract the video and make a .ts file for it bash command
			bigLoopVideoCmd
					.append("avconv -ss "
							+ MainVideoManipulator.getInstance().timeStart
									.getText()
							+ " -i "
							+ firstInput
							+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
							+ MainVideoManipulator.getInstance().timeLength
									.getText() + " -y "
							+ MainVideoManipulator.getInstance().hiddenDir
							+ "/file1.ts");
			bigLoopVideoCmd.append(";");

			// create a loop video from the .ts file bash command
			bigLoopVideoCmd.append("avconv -i concat:\"");
			bigLoopVideoCmd.append(loopString);
			bigLoopVideoCmd.append(" -c copy -bsf:a aac_adtstoasc -y "
					+ MainVideoManipulator.getInstance().workingDir
					+ "/"
					+ MainVideoManipulator.getInstance().outputLoopVideoName
							.getText() + ".mp4");
			bigLoopVideoCmd.append(";");

			// if filter is not enabled,bash command to create the exported
			// output
			if (!MainVideoManipulator.getInstance().filterEnable) {

				bigLoopVideoCmd.append("avconv -i ");
				bigLoopVideoCmd.append(firstInput);
				bigLoopVideoCmd.append(" -strict experimental -y ");
				bigLoopVideoCmd.append(lastOutput);
				bigLoopVideoCmd.append(";");

			}

			MainVideoManipulator.getInstance().loopVideoCmd = bigLoopVideoCmd
					.toString();
		}

		// construct the final command for video manipulation
		StringBuilder finalCommand = new StringBuilder();

		// if snapshot is enabled
		if (MainVideoManipulator.getInstance().snapshotEnable) {
			finalCommand.append(MainVideoManipulator.getInstance().snapshotCmd);
		}
		// if loopVideo is enabled
		if (MainVideoManipulator.getInstance().loopVideoEnable) {
			finalCommand
					.append(MainVideoManipulator.getInstance().loopVideoCmd);
		}
		// if filter is enabled
		if (MainVideoManipulator.getInstance().filterEnable) {
			finalCommand.append(MainVideoManipulator.getInstance().filterCmd);
		}
		// return the final command 
		String cmd = finalCommand.toString();
		return cmd;

	}
}
