package AudioManipulator;

import javax.swing.SwingWorker;

import VideoManipulator.VideoManipulator;

/**
 * Background Task class extends SwingWorker and handles all the long tasks.
 */
public class AudioBackgroundTask extends SwingWorker<Integer, String> {
	Process process;
	ProcessBuilder builder;
	String firstInput;
	String lastOutput;

	// Constructor for backgroundTask, takes in the name of the input and
	// the output
	protected AudioBackgroundTask(String input, String output) {
		firstInput = input;
		lastOutput = output;
	}

	// Override doInBackgrount() to execute longTask in the background
	@Override
	protected Integer doInBackground() throws Exception {

		// Reference for all the avconv commands
		// https://libav.org/avconv.html and a combination of many searches
		// found on google, final command selected after a lot of trials and
		// testing

		try {
			// if remove is enabled and replace is not enabled, constructs
			// the
			// command for removing the audio from the input file
			if (AudioManipulator.getInstance().removeEnable) {

				String tempName = "";
				if (!AudioManipulator.getInstance().replaceEnable) {

					// remove audio
					StringBuilder bigRemoveCmd = new StringBuilder();
					bigRemoveCmd.append("avconv -i " + firstInput + " -an -y "
							+ lastOutput);

					bigRemoveCmd.append(";");

					AudioManipulator.getInstance().removeCmd = bigRemoveCmd
							.toString();

				}

			}

			// if extract is enabled, constructs the command for extract
			if (AudioManipulator.getInstance().extractEnable) {

				StringBuilder bigExtractCmd = new StringBuilder();

				// if duration is not enabled - extract the .mp3 file
				// without a start time and a length
				if (!AudioManipulator.getInstance().extractDurationEnable) {
					bigExtractCmd.append("avconv -i " + firstInput
							+ " -vcodec copy -y -vn ");
					bigExtractCmd
					.append(AudioManipulator.getInstance().workingDir
							+ "/");
					bigExtractCmd
					.append(AudioManipulator.getInstance().outputFileName
							.getText());
					bigExtractCmd.append(".mp3");
					bigExtractCmd.append(";");

				}

				// if duration is enabled - extract the .mp3 file with a
				// start time and a length
				else {
					bigExtractCmd.append("avconv -i " + firstInput
							+ " -vcodec copy -ss ");
					bigExtractCmd
					.append(AudioManipulator.getInstance().startTimeExtract
							.getText());
					bigExtractCmd.append(" -t ");
					bigExtractCmd
					.append(AudioManipulator.getInstance().lengthExtract
							.getText());
					bigExtractCmd.append(" -y -vn ");
					bigExtractCmd
					.append(AudioManipulator.getInstance().workingDir
							+ "/");
					bigExtractCmd
					.append(AudioManipulator.getInstance().outputFileName
							.getText());
					bigExtractCmd.append(".mp3");
					bigExtractCmd.append(";");

				}

				if (!AudioManipulator.getInstance().removeEnable
						&& !AudioManipulator.getInstance().overlayEnable
						&& !AudioManipulator.getInstance().replaceEnable) {

					bigExtractCmd.append("avconv -i ");
					bigExtractCmd.append(firstInput);
					bigExtractCmd.append(" -y ");
					bigExtractCmd.append(lastOutput);
					bigExtractCmd.append(";");

				}

				AudioManipulator.getInstance().extractCmd = bigExtractCmd
						.toString();
			}

			// if extract is enabled, constructs the command for replace
			if (AudioManipulator.getInstance().replaceEnable) {
				String tempName = "";
				if (AudioManipulator.getInstance().overlayEnable) {
					tempName = lastOutput;
					lastOutput = AudioManipulator.getInstance().hiddenDir
							+ "/r.mp4";

				}

				StringBuilder bigReplaceCmd = new StringBuilder();
				// replace the audio stream

				bigReplaceCmd.append("avconv -i " + firstInput + " -i ");
				bigReplaceCmd.append(AudioManipulator.getInstance().inputFile);
				bigReplaceCmd
				.append(" -ss 0 -map 0:0 -map 1:0 -c:v copy -c:a copy -t ");
				bigReplaceCmd
				.append(AudioManipulator.getInstance().videoLength);
				bigReplaceCmd.append(" -y " + lastOutput);
				bigReplaceCmd.append(";");

				AudioManipulator.getInstance().replaceCmd = bigReplaceCmd
						.toString();

				if (AudioManipulator.getInstance().overlayEnable) {
					lastOutput = tempName;
				}
			}

			// if overlay is enabled, constructs the command for overlay

			if (AudioManipulator.getInstance().overlayEnable) {

				String tempName = "";

				if (AudioManipulator.getInstance().replaceEnable) {
					tempName = firstInput;
					firstInput = AudioManipulator.getInstance().hiddenDir
							+ "/r.mp4";
				}

				StringBuilder bigOverlayCmd = new StringBuilder();

				// get the number of input audio files
				int num = AudioManipulator.getInstance().fullNames.getSize() + 1;
				String number = "" + num;

				StringBuilder s = new StringBuilder();
				// append their file names together
				for (int i = 0; i < AudioManipulator.getInstance().fullNames
						.getSize(); i++) {
					s.append("-i ");
					s.append(AudioManipulator.getInstance().fullNames.get(i)
							.toString());
					s.append(" ");
				}
				String inputFileNames = s.toString();

				StringBuilder m = new StringBuilder();
				// append their map functions together
				for (int i = 1; i <= AudioManipulator.getInstance().fullNames
						.getSize(); i++) {
					m.append("-map ");
					m.append(i);
					m.append(":a ");
				}
				String maps = m.toString();

				// if duration is not enabled - merge the audio streams using
				// the filter amix=inputs
				// Reference to Nasser's slides
				if (!AudioManipulator.getInstance().overlayDurationEnable) {
					bigOverlayCmd.append("avconv -i " + firstInput + " ");
					bigOverlayCmd.append(inputFileNames);
					bigOverlayCmd
					.append(" -map 0:v "
							+ maps
							+ " -codec:v libx264 -preset medium -crf 23 -codec:a aac -strict experimental -b:a 192k -filter_complex amix=inputs="
							+ number + " -t ");
					bigOverlayCmd
					.append(AudioManipulator.getInstance().videoLength);
					bigOverlayCmd.append(" -y " + lastOutput);
					bigOverlayCmd.append(";");



				}

				// if duration is enabled - merge the two audio streams using
				// the filted amix=inputs
				// Reference to Nasser's slides with a specified start time and
				// duration
				else {

					// convert the start time from hh:mm:ss to seconds
					int startTimeInSeconds = AudioManipulator.getInstance().ac
							.convertToSeconds(AudioManipulator.getInstance().startTimeOverlay
									.getText());
					// convert the length from hh:mm:ss to seconds
					int lengthInSeconds = AudioManipulator.getInstance().ac
							.convertToSeconds(AudioManipulator.getInstance().lengthOverlay
									.getText());

					// add the start time and length to get the stopTime
					int stopTimeInSeconds = startTimeInSeconds
							+ lengthInSeconds;

					// convert to strings
					String startTime = "" + startTimeInSeconds;
					String length = "" + lengthInSeconds;
					String stopTime = "" + stopTimeInSeconds;

					// temp file Selection.mp4 with the video selection that
					// they want to add audio too
					// then merge the audio streams of the input audio file and
					// selection
					// then concatenate the corresponding segments of the video
					// together
					bigOverlayCmd.append("avconv -i " + firstInput + " -ss ");
					bigOverlayCmd.append(startTime);
					bigOverlayCmd.append(" -t ");
					bigOverlayCmd.append(length);
					bigOverlayCmd.append(" -strict experimental -y "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/Selection.mp4; avconv -i "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/Selection.mp4 ");
					bigOverlayCmd.append(inputFileNames);
					bigOverlayCmd
					.append(" -map 0:v "
							+ maps
							+ "-codec:v libx264 -preset medium -crf 23 -acodec aac -strict experimental -b:a 192k -filter_complex amix=inputs="
							+ number
							+ " -t "
							+ length
							+ " -y "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/out.mp4; avconv -ss 0 -i "
							+ firstInput
							+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t ");
					bigOverlayCmd.append(startTime);
					bigOverlayCmd
					.append(" -y "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file1.ts ; avconv -ss 0 -i "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/out.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
							+ length + " -y "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file2.ts; avconv -ss ");
					bigOverlayCmd.append(stopTime); // start+duration
					bigOverlayCmd
					.append(" -i "
							+ firstInput
							+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file3.ts ; avconv -i concat:\""
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file1.ts|"
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file2.ts|"
							+ AudioManipulator.getInstance().hiddenDir
							+ "/file3.ts\" -c copy -bsf:a aac_adtstoasc -y "
							+ lastOutput);
					bigOverlayCmd.append(";");



				}
				if (AudioManipulator.getInstance().replaceEnable) {
					firstInput = tempName;
				}

				AudioManipulator.getInstance().overlayCmd = bigOverlayCmd
						.toString();

			}

			// construct the final command for audio
			StringBuilder finalCommand = new StringBuilder();

			if (AudioManipulator.getInstance().removeEnable
					&& !AudioManipulator.getInstance().replaceEnable) {
				finalCommand.append(AudioManipulator.getInstance().removeCmd);
				finalCommand.append(";");
			}
			if (AudioManipulator.getInstance().extractEnable) {
				finalCommand.append(AudioManipulator.getInstance().extractCmd);
				finalCommand.append(";");
			}
			if (AudioManipulator.getInstance().replaceEnable) {
				finalCommand.append(AudioManipulator.getInstance().replaceCmd);
				finalCommand.append(";");
			}
			if (AudioManipulator.getInstance().overlayEnable) {
				finalCommand.append(AudioManipulator.getInstance().overlayCmd);
				finalCommand.append(";");
			}

			// start the builder for the bash command so it is executed
			String cmd = finalCommand.toString();
			System.out.println(cmd);
			builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			process = builder.start();
			process.waitFor();
			return process.exitValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 1;
	}
}
