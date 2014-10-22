package audioManipulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class AudioChecks {
	/**
	 * convertToSeconds Method changes a string in the format hh:mm:ss to
	 * seconds
	 * 
	 * @param longFormat
	 */
	protected int convertToSeconds(String longFormat) {

		// Reference for convertingToSeconds:
		// http://stackoverflow.com/questions/11994790/parse-time-of-format-hhmmss

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = null;

		// parse the input to a Date format
		try {
			date = sdf.parse(longFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// creates a new calendar instance
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date); // assigns calendar to given date

		// do the calculation to convert hh:mm:ss to seconds
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		if (hour != 0) {
			hour = hour * 60 * 60;
		}
		if (minute != 0) {
			minute = minute * 60;
		}
		int timeInSeconds = seconds + hour + minute;

		return timeInSeconds;
	}

	/**
	 * timeChecks Method checks whether the time inputs given by the user were
	 * in the hh:mm:ss format or not
	 * 
	 * @param startTime
	 *            , length
	 */

	protected int timeChecks(String startTime, String length) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the startTime and length are in the format hh:mm:ss
		Pattern timePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
		Matcher startMatcher = timePattern.matcher(startTime);
		Matcher lengthMatcher = timePattern.matcher(length);

		int passed = 0;

		// if the startTime is not in the right format, return 1
		if (!(startMatcher.find() && startTime.length() == 8)) {
			passed = passed + 1;
		}
		// if the length is not in the right format, return 2, if startTime is
		// wrong too, it will return 3
		if (!(lengthMatcher.find() && length.length() == 8)) {
			passed = passed + 2;
		}

		return passed;
	}

	/**
	 * audioSignalCheck Method checks whether the input media given by the user
	 * has an audio signal or not
	 */

	protected boolean audioSignalCheck() {

		// Reference for avProbe:
		// https://libav.org/avprobe.html

		int count = 0;

		// count the lines using the avprobe bash command
		String cmd = "avprobe -loglevel error -show_streams " + AudioManipulator.getInstance().videoPath
				+ " | grep -i streams.stream | wc -l";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();
			InputStream outStr = process.getInputStream();
			// read the output of the process
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					outStr));
			String line;
			line = stdout.readLine();
			count = Integer.parseInt(line); // Get the number of streams
		} catch (IOException e) {
			e.printStackTrace();
		}

		// if the video file has no audio stream, disable all audio manipulation fields
		if (count == 2) {
			JOptionPane
			.showMessageDialog(
					null,
					"ERROR: no audio signal in the imported video, can not perform audio manipulation, SORRY :(");
			AudioManipulator.getInstance().removeCheck.setEnabled(false);
			AudioManipulator.getInstance().removeCheck.setSelected(false);
			AudioManipulator.getInstance().extractCheck.setEnabled(false);
			AudioManipulator.getInstance().extractCheck.setSelected(false);
			AudioManipulator.getInstance().replaceCheck.setEnabled(false);
			AudioManipulator.getInstance().replaceCheck.setSelected(false);
			AudioManipulator.getInstance().overlayCheck.setEnabled(false);
			AudioManipulator.getInstance().overlayCheck.setSelected(false);

			AudioManipulator.getInstance().outputFileNameLabel.setEnabled(false);
			AudioManipulator.getInstance().outputFileName.setEnabled(false);
			AudioManipulator.getInstance().mp3Label.setEnabled(false);
			AudioManipulator.getInstance().starLabel2.setEnabled(false);
			AudioManipulator.getInstance().extractDurationCheck.setEnabled(false);
			AudioManipulator.getInstance().startTimeLabelExtract.setEnabled(false);
			AudioManipulator.getInstance().startTimeExtract.setEnabled(false);
			AudioManipulator.getInstance().lengthLabelExtract.setEnabled(false);
			AudioManipulator.getInstance().lengthExtract.setEnabled(false);
			AudioManipulator.getInstance().inputAudioReplaceButton.setEnabled(false);
			AudioManipulator.getInstance().replacePlayButton.setEnabled(false);
			AudioManipulator.getInstance().inputFile = "";

			AudioManipulator.getInstance().starLabel2.setEnabled(false);
			AudioManipulator.getInstance().overlayDurationCheck.setEnabled(false);
			AudioManipulator.getInstance().overlayDurationCheck.setSelected(false);
			AudioManipulator.getInstance().inputOverlayButton.setEnabled(false);
			AudioManipulator.getInstance().deleteOverlayButton.setEnabled(false);
			AudioManipulator.getInstance().playOverlayButton.setEnabled(false);
			AudioManipulator.getInstance().listOverlayLabel.setEnabled(false);
			AudioManipulator.getInstance().scrollPane.setEnabled(false);
			AudioManipulator.getInstance().audioFilesList.setEnabled(false);
			AudioManipulator.getInstance().overlayEnable = false;

			AudioManipulator.getInstance().startTimeLabelOverlay.setEnabled(false);
			AudioManipulator.getInstance().startTimeOverlay.setEnabled(false);
			AudioManipulator.getInstance().startTimeOverlay.setText("");
			AudioManipulator.getInstance().lengthLabelOverlay.setEnabled(false);
			AudioManipulator.getInstance().lengthOverlay.setEnabled(false);
			AudioManipulator.getInstance().lengthOverlay.setText("");


			AudioManipulator.getInstance().apf.enableAudioMan(false);


			return false;
		}

		// if the video file has more than 2 lines, it has an audio signal and
		// if an audio file has one line, it has an audio signal
		else {
			return true;
		}
	}

	/**
	 * allChecksExtract Method does all the checks for extract
	 */

	protected boolean allChecksExtract() {
		// Get the video path and length
		AudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedOrNot = true;

		// if duration is enabled
		if (AudioManipulator.getInstance().extractDurationEnable) {

			// check whether the input startTime and lengthTime is in the right
			// format or not using timeChecks()
			int passedTimeCheckExtract = timeChecks(AudioManipulator.getInstance().startTimeExtract.getText()
					.trim(), AudioManipulator.getInstance().lengthExtract.getText().trim());

			// if both are in the wrong format, inform the user and allow them
			// to enter the start time and length again
			if (passedTimeCheckExtract == 3) {
				AudioManipulator.getInstance().startTimeExtract.setText("");
				AudioManipulator.getInstance().lengthExtract.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time and length for extract command can only be in the format hh:mm:ss");
			}

			// if start time is in the wrong format, inform the user and allow
			// them to enter the start time again
			else if (passedTimeCheckExtract == 1) {
				AudioManipulator.getInstance().startTimeExtract.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: start time for extract command can only be in the format hh:mm:ss");

			}

			// if length is in the wrong format, inform the user and allow them
			// to enter the length again
			else if (passedTimeCheckExtract == 2) {
				AudioManipulator.getInstance().lengthExtract.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: length for extract command can only be in the format hh:mm:ss");
			}

			// check for whether the given duration + start time is smaller than
			// the length of the video or not
			else if (passedOrNot) {
				int convertedStartTime = convertToSeconds(AudioManipulator.getInstance().startTimeExtract
						.getText().trim());
				int convertedLength = convertToSeconds(AudioManipulator.getInstance().lengthExtract.getText()
						.trim());
				int totalTime = convertedStartTime + convertedLength;
				int lengthOfVideo = (int) (Double.parseDouble(AudioManipulator.getInstance().videoLength));

				// if totalTime is more than the length of the video, inform the
				// user and allow them to enter the start time and length again
				if (totalTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane
					.showMessageDialog(
							null,
							"ERROR: start time + length for extract can not be more than the length of the video - " + lengthOfVideo+ " seconds");
					AudioManipulator.getInstance().startTimeExtract.setText("");
					AudioManipulator.getInstance().lengthExtract.setText("");
				}
			}
		}

		// check for whether the outputFileName field is empty
		if (AudioManipulator.getInstance().outputFileName.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output file name for extract");
		}

		// check whether the outputFileName specified user already exists in the
		// project directory
		String outputFile = AudioManipulator.getInstance().workingDir + "/" + AudioManipulator.getInstance().outputFileName.getText()
				+ ".mp3";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Cancel", "Overwrite" };
			int optionChosen = JOptionPane.showOptionDialog(null, "ERROR: "
					+ AudioManipulator.getInstance().outputFileName.getText() + ".mp3 already exists. "
					+ "Do you want to overwrite the existing file?",
					"File Exists!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			if (optionChosen == 1) {
				f.delete(); // Delete the existing file
			} else {
				AudioManipulator.getInstance().outputFileName.setText("");
				passedOrNot = false;
			}
		}
		return passedOrNot;
	}

	/**
	 * allChecksOverlay Method does all the checks for overlay
	 */

	protected boolean allChecksOverlay() {
		// Get the video path and length
		AudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedOrNot = true;

		// if duration is enabled
		if (AudioManipulator.getInstance().overlayDurationEnable) {

			// check whether the input startTime and lengthTime is in the right
			// format or not using timeChecks()
			int passedTimeCheckOverlay = timeChecks(AudioManipulator.getInstance().startTimeOverlay.getText()
					.trim(), AudioManipulator.getInstance().lengthOverlay.getText().trim());

			// if both are in the wrong format, inform the user and allow them
			// to enter the start time and length again
			if (passedTimeCheckOverlay == 3) {
				AudioManipulator.getInstance().startTimeOverlay.setText("");
				AudioManipulator.getInstance().lengthOverlay.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time and length for Overlay command can only be in the format hh:mm:ss");
			}

			// if start time is in the wrong format, inform the user and allow
			// them to enter the start time again
			else if (passedTimeCheckOverlay == 1) {
				AudioManipulator.getInstance().startTimeOverlay.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: start time for Overlay command can only be in the format hh:mm:ss");

			}

			// if length is in the wrong format, inform the user and allow them
			// to enter the length again
			else if (passedTimeCheckOverlay == 2) {
				AudioManipulator.getInstance().lengthOverlay.setText("");
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: length for Overlay command can only be in the format hh:mm:ss");
			}

			// check for whether the given duration + start time is smaller than
			// the length of the video or not
			else if (passedOrNot) {
				int convertedStartTime = convertToSeconds(AudioManipulator.getInstance().startTimeOverlay
						.getText().trim());
				int convertedLength = convertToSeconds(AudioManipulator.getInstance().lengthOverlay.getText()
						.trim());
				int totalTime = convertedStartTime + convertedLength;
				int lengthOfVideo = (int) (Double.parseDouble(AudioManipulator.getInstance().videoLength));

				// if totalTime is more than the length of the video, inform the
				// user and allow them to enter the start time and length again
				if (totalTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane
					.showMessageDialog(
							null,
							"ERROR: start time + length for Overlay can not be more than the length of the video - " + lengthOfVideo+ " seconds");
					AudioManipulator.getInstance().startTimeOverlay.setText("");
					AudioManipulator.getInstance().lengthOverlay.setText("");
				}
			}
		}

		// check for whether the audio list is empty
		if (AudioManipulator.getInstance().audioFiles.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please add audio files for overlay");

			passedOrNot = false;
		}

		return passedOrNot;
	}

	/**
	 * allChecksReplace Method does all the checks for replace
	 */

	protected boolean allChecksReplace() {
		boolean passedOrNot = true;

		// Reference to File.probeContentType
		// http://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileTypeDetector.html

		// check for whether the input audio file chosen by the user is an audio
		// file or not
		File file = new File(AudioManipulator.getInstance().inputFile);
		Path path = file.toPath();
		String type = "";
		try {
			type = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// if the file is NOT an audio file, notify the user and
		// allow them to select again
		if (!(type.equals("audio/mpeg"))) {
			JOptionPane
			.showMessageDialog(
					null,
					"ERROR: "
							+ AudioManipulator.getInstance().inputFile
							+ " does not refer to a valid audio file. Please select a new input file!");
			passedOrNot = false;
		}
		return passedOrNot;
	}

	/**
	 * allChecksAudio Method does all the checks for Audio Manipulator
	 */
	protected boolean allChecksAudio() {
		boolean passedAllChecks = true;

		// do all the checks for extract
		if (AudioManipulator.getInstance().extractEnable) {
			passedAllChecks = allChecksExtract();
		}

		// do all the checks for replace
		if (AudioManipulator.getInstance().replaceEnable) {
			passedAllChecks = allChecksReplace();
		}

		// do all the checks for overlay
		if (AudioManipulator.getInstance().overlayEnable) {

			passedAllChecks = allChecksOverlay();

		}


		return passedAllChecks;
	}
}
