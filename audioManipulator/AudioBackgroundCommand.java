package audioManipulator;

/**
 * SoftEng206 Project - audio background command class
 * 
 * Purpose: The purpose of this class is to create the command that corresponds
 * to the background task for audio manipulation during export. It identifies
 * whether remove, extract, replace and/or overlay is selected by the user and
 * creates the corresponding bash command using the user input. This class is
 * used in MainAudioMainpulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class AudioBackgroundCommand {

	private String firstInput;
	private String lastOutput;

	/**
	 * makeAudioCommand is a method that creates the bash commands in the form
	 * of a string according to all the options selected by the user
	 * 
	 * @param input
	 * @param output
	 * @return the final command
	 */

	protected String makeAudioCommand(String input, String output) {

		firstInput = input;
		lastOutput = output;

		// Reference for all the avconv commands
		// https://libav.org/avconv.html and a combination of many searches
		// found on google, final command selected after a lot of trials and
		// testing

		// if remove is enabled and replace is not enabled, constructs
		// the command for removing the audio from the input file
		if (MainAudioManipulator.getInstance().removeEnable) {

			if (!MainAudioManipulator.getInstance().replaceEnable) {

				// remove audio bash command
				StringBuilder bigRemoveCmd = new StringBuilder();
				bigRemoveCmd.append("avconv -i " + firstInput + " -an -y "
						+ lastOutput);

				bigRemoveCmd.append(";");

				MainAudioManipulator.getInstance().removeCmd = bigRemoveCmd
						.toString();

			}

		}

		// if extract is enabled, constructs the command for extract
		if (MainAudioManipulator.getInstance().extractEnable) {

			StringBuilder bigExtractCmd = new StringBuilder();

			// if duration is not enabled - extract the .mp3 file
			// without a start time and a length - bash command for extract
			if (!MainAudioManipulator.getInstance().extractDurationEnable) {
				bigExtractCmd.append("avconv -i " + firstInput
						+ " -vcodec copy -y -vn ");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().workingDir
								+ "/");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().outputFileName
								.getText());
				bigExtractCmd.append(".mp3");
				bigExtractCmd.append(";");

			}

			// if duration is enabled - extract the .mp3 file with a
			// start time and a length - bash command for extract
			else {
				bigExtractCmd.append("avconv -i " + firstInput
						+ " -vcodec copy -ss ");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().startTimeExtract
								.getText());
				bigExtractCmd.append(" -t ");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().lengthExtract
								.getText());
				bigExtractCmd.append(" -y -vn ");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().workingDir
								+ "/");
				bigExtractCmd
						.append(MainAudioManipulator.getInstance().outputFileName
								.getText());
				bigExtractCmd.append(".mp3");
				bigExtractCmd.append(";");

			}

			// if ONLY extract is enabled, bash command to create the exported
			// output
			if (!MainAudioManipulator.getInstance().removeEnable
					&& !MainAudioManipulator.getInstance().overlayEnable
					&& !MainAudioManipulator.getInstance().replaceEnable) {

				bigExtractCmd.append("avconv -i ");
				bigExtractCmd.append(firstInput);
				bigExtractCmd.append(" -strict experimental -y ");
				bigExtractCmd.append(lastOutput);
				bigExtractCmd.append(";");

			}

			MainAudioManipulator.getInstance().extractCmd = bigExtractCmd
					.toString();
		}

		// if replace is enabled, constructs the command for replace
		if (MainAudioManipulator.getInstance().replaceEnable) {

			// if overlay is enabled as well, change the variable lastOutput to
			// a temp file r.mp4
			String tempName = "";
			if (MainAudioManipulator.getInstance().overlayEnable) {
				tempName = lastOutput;
				lastOutput = MainAudioManipulator.getInstance().hiddenDir
						+ "/r.mp4";

			}

			StringBuilder bigReplaceCmd = new StringBuilder();

			// replace audio stream bash command
			bigReplaceCmd.append("avconv -i " + firstInput + " -i ");
			bigReplaceCmd.append(MainAudioManipulator.getInstance().inputFile);
			bigReplaceCmd
					.append(" -ss 0 -map 0:0 -map 1:0 -c:v copy -c:a copy -t ");
			bigReplaceCmd
					.append(MainAudioManipulator.getInstance().videoLength);
			bigReplaceCmd.append(" -y " + lastOutput);
			bigReplaceCmd.append(";");

			MainAudioManipulator.getInstance().replaceCmd = bigReplaceCmd
					.toString();

			// if overlay is enabled as well, change the variable lastOutput to
			// back to the original lastOutput
			if (MainAudioManipulator.getInstance().overlayEnable) {
				lastOutput = tempName;
			}
		}

		// if overlay is enabled, constructs the command for overlay
		if (MainAudioManipulator.getInstance().overlayEnable) {

			// if replace is enabled as well, change the variable firstInput to
			// the temp file r.mp4
			String tempName = "";
			if (MainAudioManipulator.getInstance().replaceEnable) {
				tempName = firstInput;
				firstInput = MainAudioManipulator.getInstance().hiddenDir
						+ "/r.mp4";
			}

			StringBuilder bigOverlayCmd = new StringBuilder();

			// get the number of input audio files
			int num = MainAudioManipulator.getInstance().fullNames.getSize() + 1;
			String number = "" + num;

			StringBuilder s = new StringBuilder();
			// append their file names together
			for (int i = 0; i < MainAudioManipulator.getInstance().fullNames
					.getSize(); i++) {
				s.append("-i ");
				s.append(MainAudioManipulator.getInstance().fullNames.get(i)
						.toString());
				s.append(" ");
			}
			String inputFileNames = s.toString();

			StringBuilder m = new StringBuilder();
			// append their map functions together
			for (int i = 1; i <= MainAudioManipulator.getInstance().fullNames
					.getSize(); i++) {
				m.append("-map ");
				m.append(i);
				m.append(":a ");
			}
			String maps = m.toString();

			// if duration is not enabled - merge the audio streams using
			// the filter amix=inputs bash command
			// Reference to Nasser's slides
			if (!MainAudioManipulator.getInstance().overlayDurationEnable) {
				bigOverlayCmd.append("avconv -i " + firstInput + " ");
				bigOverlayCmd.append(inputFileNames);
				bigOverlayCmd
						.append(" -map 0:v "
								+ maps
								+ " -codec:v libx264 -preset medium -crf 23 -codec:a aac -strict experimental -b:a 192k -filter_complex amix=inputs="
								+ number + " -t ");
				bigOverlayCmd
						.append(MainAudioManipulator.getInstance().videoLength);
				bigOverlayCmd.append(" -y " + lastOutput);
				bigOverlayCmd.append(";");

			}

			// if duration is enabled - merge the two audio streams using
			// the filted amix=inputs bash command
			// Reference to Nasser's slides with a specified start time and
			// duration
			else {

				// convert the start time from hh:mm:ss to seconds
				int startTimeInSeconds = MainAudioManipulator.getInstance().ac
						.convertToSeconds(MainAudioManipulator.getInstance().startTimeOverlay
								.getText());
				// convert the length from hh:mm:ss to seconds
				int lengthInSeconds = MainAudioManipulator.getInstance().ac
						.convertToSeconds(MainAudioManipulator.getInstance().lengthOverlay
								.getText());

				// add the start time and length to get the stopTime
				int stopTimeInSeconds = startTimeInSeconds + lengthInSeconds;

				// convert to strings
				String startTime = "" + startTimeInSeconds;
				String length = "" + lengthInSeconds;
				String stopTime = "" + stopTimeInSeconds;

				// temp file Selection.mp4 with the video selection that they
				// want to add audio too
				bigOverlayCmd.append("avconv -i " + firstInput + " -ss ");
				bigOverlayCmd.append(startTime);
				bigOverlayCmd.append(" -t ");
				bigOverlayCmd.append(length);
				bigOverlayCmd.append(" -strict experimental -y "
						+ MainAudioManipulator.getInstance().hiddenDir
						+ "/Selection.mp4; avconv -i "
						+ MainAudioManipulator.getInstance().hiddenDir
						+ "/Selection.mp4 ");

				// then merge the audio streams of the input audio file and
				// selection and concatenate the corresponding segments of the
				// video
				// together
				bigOverlayCmd.append(inputFileNames);
				bigOverlayCmd
						.append(" -map 0:v "
								+ maps
								+ "-codec:v libx264 -preset medium -crf 23 -acodec aac -strict experimental -b:a 192k -filter_complex amix=inputs="
								+ number
								+ " -t "
								+ length
								+ " -y "
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/out.mp4; avconv -ss 0 -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t ");
				bigOverlayCmd.append(startTime);
				bigOverlayCmd
						.append(" -y "
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file1.ts ; avconv -ss 0 -i "
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/out.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
								+ length + " -y "
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file2.ts; avconv -ss ");
				bigOverlayCmd.append(stopTime); // start+duration
				bigOverlayCmd
						.append(" -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file3.ts ; avconv -i concat:\""
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file1.ts|"
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file2.ts|"
								+ MainAudioManipulator.getInstance().hiddenDir
								+ "/file3.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
				bigOverlayCmd.append(";");

			}

			// if replace is enabled as well, change the variable firstInput to
			// back to the original firstInput
			if (MainAudioManipulator.getInstance().replaceEnable) {
				firstInput = tempName;
			}

			MainAudioManipulator.getInstance().overlayCmd = bigOverlayCmd
					.toString();

		}

		// construct the final command for audio
		StringBuilder finalCommand = new StringBuilder();

		// if remove is enabled AND replace is NOT enabled
		if (MainAudioManipulator.getInstance().removeEnable
				&& !MainAudioManipulator.getInstance().replaceEnable) {
			finalCommand.append(MainAudioManipulator.getInstance().removeCmd);

		}
		// if extract is enabled
		if (MainAudioManipulator.getInstance().extractEnable) {
			finalCommand.append(MainAudioManipulator.getInstance().extractCmd);

		}
		// if replace is enabled
		if (MainAudioManipulator.getInstance().replaceEnable) {
			finalCommand.append(MainAudioManipulator.getInstance().replaceCmd);

		}
		// if overlay is enabled
		if (MainAudioManipulator.getInstance().overlayEnable) {
			finalCommand.append(MainAudioManipulator.getInstance().overlayCmd);

		}

		// return the final command
		String cmd = finalCommand.toString();
		return cmd;
	}
}
