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

/**
 * SoftEng206 Project - audio checks class
 * 
 * Purpose: The purpose of this class is to perform all the error checks for the
 * features remove, extract, overlay and replace audio. It also provides some
 * helper methods that are necessary for these checks. This class is used in
 * AudioSave.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class AudioChecks {

	/**
	 * convertToSeconds Method is a helper method changes a string in the format
	 * hh:mm:ss to seconds
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
	 * timeChecks Method is a helper method that checks whether the time inputs
	 * given by the user were in the hh:mm:ss format or not
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
	 * has an audio signal or not and enables or disables audio features
	 * accordingly
	 */

	protected boolean audioSignalCheck() {

		// Reference for avProbe:
		// https://libav.org/avprobe.html
		int count = 0;

		// count the lines using the avprobe bash command
		String cmd = "avprobe -loglevel error -show_streams "
				+ MainAudioManipulator.getInstance().videoPath
				+ " | grep -i streams.stream.1 | wc -l";

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

		// if the video file has no audio stream, disable all audio manipulation
		// fields
		if (count == 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"ERROR: no audio signal in the imported video, can not perform audio manipulation, SORRY :(");
			MainAudioManipulator.getInstance().removeCheck.setEnabled(false);
			MainAudioManipulator.getInstance().removeCheck.setSelected(false);
			MainAudioManipulator.getInstance().extractCheck.setEnabled(false);
			MainAudioManipulator.getInstance().extractCheck.setSelected(false);
			MainAudioManipulator.getInstance().replaceCheck.setEnabled(false);
			MainAudioManipulator.getInstance().replaceCheck.setSelected(false);
			MainAudioManipulator.getInstance().overlayCheck.setEnabled(false);
			MainAudioManipulator.getInstance().overlayCheck.setSelected(false);

			MainAudioManipulator.getInstance().outputFileNameLabel
					.setEnabled(false);
			MainAudioManipulator.getInstance().outputFileName.setEnabled(false);
			MainAudioManipulator.getInstance().mp3Label.setEnabled(false);
			MainAudioManipulator.getInstance().starLabel2.setEnabled(false);
			MainAudioManipulator.getInstance().extractDurationCheck
					.setEnabled(false);
			MainAudioManipulator.getInstance().startTimeLabelExtract
					.setEnabled(false);
			MainAudioManipulator.getInstance().startTimeExtract
					.setEnabled(false);
			MainAudioManipulator.getInstance().lengthLabelExtract
					.setEnabled(false);
			MainAudioManipulator.getInstance().lengthExtract.setEnabled(false);
			MainAudioManipulator.getInstance().inputAudioReplaceButton
					.setEnabled(false);
			MainAudioManipulator.getInstance().replacePlayButton
					.setEnabled(false);
			MainAudioManipulator.getInstance().inputFile = "";

			MainAudioManipulator.getInstance().starLabel2.setEnabled(false);
			MainAudioManipulator.getInstance().overlayDurationCheck
					.setEnabled(false);
			MainAudioManipulator.getInstance().overlayDurationCheck
					.setSelected(false);
			MainAudioManipulator.getInstance().inputOverlayButton
					.setEnabled(false);
			MainAudioManipulator.getInstance().deleteOverlayButton
					.setEnabled(false);
			MainAudioManipulator.getInstance().playOverlayButton
					.setEnabled(false);
			MainAudioManipulator.getInstance().listOverlayLabel
					.setEnabled(false);
			MainAudioManipulator.getInstance().scrollPane.setEnabled(false);
			MainAudioManipulator.getInstance().audioFilesList.setEnabled(false);
			MainAudioManipulator.getInstance().overlayEnable = false;

			MainAudioManipulator.getInstance().startTimeLabelOverlay
					.setEnabled(false);
			MainAudioManipulator.getInstance().startTimeOverlay
					.setEnabled(false);
			MainAudioManipulator.getInstance().startTimeOverlay.setText("");
			MainAudioManipulator.getInstance().lengthLabelOverlay
					.setEnabled(false);
			MainAudioManipulator.getInstance().lengthOverlay.setEnabled(false);
			MainAudioManipulator.getInstance().lengthOverlay.setText("");

			MainAudioManipulator.getInstance().apf.enableAudioMan(false);

			return false;
		}

		// if the video file has more than 0 lines, it has an audio signal and
		// if an audio file has one line, it has an audio signal
		else {
			return true;
		}
	}

	/**
	 * allChecksExtract Method does all the checks for extract such as check
	 * time formats, check whether times are within video range, detect empty
	 * fields and also checks for existing files.
	 * 
	 */

	protected boolean allChecksExtract() {
		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedOrNot = true;

		// if duration is enabled
		if (MainAudioManipulator.getInstance().extractDurationEnable) {

			// check whether the input startTime and lengthTime is in the right
			// format or not using timeChecks()
			int passedTimeCheckExtract = timeChecks(
					MainAudioManipulator.getInstance().startTimeExtract
							.getText().trim(),
					MainAudioManipulator.getInstance().lengthExtract.getText()
							.trim());

			// if both are in the wrong format, inform the user and allow them
			// to enter the start time and length again
			if (passedTimeCheckExtract == 3) {
				MainAudioManipulator.getInstance().startTimeExtract.setText("");
				MainAudioManipulator.getInstance().lengthExtract.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(
								null,
								"ERROR: start time and length for extract command can only be in the format hh:mm:ss");
			}

			// if start time is in the wrong format, inform the user and allow
			// them to enter the start time again
			else if (passedTimeCheckExtract == 1) {
				MainAudioManipulator.getInstance().startTimeExtract.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: start time for extract command can only be in the format hh:mm:ss");

			}

			// if length is in the wrong format, inform the user and allow them
			// to enter the length again
			else if (passedTimeCheckExtract == 2) {
				MainAudioManipulator.getInstance().lengthExtract.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: length for extract command can only be in the format hh:mm:ss");
			}

			// check for whether the given duration + start time is smaller than
			// the length of the video or not
			else if (passedOrNot) {
				int convertedStartTime = convertToSeconds(MainAudioManipulator
						.getInstance().startTimeExtract.getText().trim());
				int convertedLength = convertToSeconds(MainAudioManipulator
						.getInstance().lengthExtract.getText().trim());
				int totalTime = convertedStartTime + convertedLength;
				int lengthOfVideo = (int) (Double
						.parseDouble(MainAudioManipulator.getInstance().videoLength));

				// if totalTime is more than the length of the video, inform the
				// user and allow them to enter the start time and length again
				if (totalTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane
							.showMessageDialog(
									null,
									"ERROR: start time + length for extract can not be more than the length of the video - "
											+ lengthOfVideo + " seconds");
					MainAudioManipulator.getInstance().startTimeExtract
							.setText("");
					MainAudioManipulator.getInstance().lengthExtract
							.setText("");
				}
			}
		}

		// check for whether the outputFileName field is empty
		if (MainAudioManipulator.getInstance().outputFileName.getText().trim()
				.equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output file name for extract");
			passedOrNot = false;
		}

		// check whether the outputFileName specified user already exists in the
		// project directory
		String outputFile = MainAudioManipulator.getInstance().workingDir + "/"
				+ MainAudioManipulator.getInstance().outputFileName.getText()
				+ ".mp3";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() :
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Overwrite", "Cancel" };
			int optionChosen = JOptionPane.showOptionDialog(
					null,
					"ERROR: "
							+ MainAudioManipulator.getInstance().outputFileName
									.getText() + ".mp3 already exists. "
							+ "Do you want to overwrite the existing file?",
					"File Exists!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			if (optionChosen == 0) {
				f.delete(); // Delete the existing file
			} else {
				MainAudioManipulator.getInstance().outputFileName.setText("");
				passedOrNot = false;
			}
		}
		return passedOrNot;
	}

	/**
	 * allChecksOverlay Method does all the checks for overlay such as check
	 * time formats, check whether times are within video range and detects
	 * empty fields.
	 */

	protected boolean allChecksOverlay() {
		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();
		boolean passedOrNot = true;

		// if duration is enabled
		if (MainAudioManipulator.getInstance().overlayDurationEnable) {

			// check whether the input startTime and lengthTime is in the right
			// format or not using timeChecks()
			int passedTimeCheckOverlay = timeChecks(
					MainAudioManipulator.getInstance().startTimeOverlay
							.getText().trim(),
					MainAudioManipulator.getInstance().lengthOverlay.getText()
							.trim());

			// if both are in the wrong format, inform the user and allow them
			// to enter the start time and length again
			if (passedTimeCheckOverlay == 3) {
				MainAudioManipulator.getInstance().startTimeOverlay.setText("");
				MainAudioManipulator.getInstance().lengthOverlay.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(
								null,
								"ERROR: start time and length for Overlay command can only be in the format hh:mm:ss");
			}

			// if start time is in the wrong format, inform the user and allow
			// them to enter the start time again
			else if (passedTimeCheckOverlay == 1) {
				MainAudioManipulator.getInstance().startTimeOverlay.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: start time for Overlay command can only be in the format hh:mm:ss");

			}

			// if length is in the wrong format, inform the user and allow them
			// to enter the length again
			else if (passedTimeCheckOverlay == 2) {
				MainAudioManipulator.getInstance().lengthOverlay.setText("");
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: length for Overlay command can only be in the format hh:mm:ss");
			}

			// check for whether the given duration + start time is smaller than
			// the length of the video or not
			else if (passedOrNot) {
				int convertedStartTime = convertToSeconds(MainAudioManipulator
						.getInstance().startTimeOverlay.getText().trim());
				int convertedLength = convertToSeconds(MainAudioManipulator
						.getInstance().lengthOverlay.getText().trim());
				int totalTime = convertedStartTime + convertedLength;
				int lengthOfVideo = (int) (Double
						.parseDouble(MainAudioManipulator.getInstance().videoLength));

				// if totalTime is more than the length of the video, inform the
				// user and allow them to enter the start time and length again
				if (totalTime > lengthOfVideo) {
					passedOrNot = false;
					JOptionPane
							.showMessageDialog(
									null,
									"ERROR: start time + length for Overlay can not be more than the length of the video - "
											+ lengthOfVideo + " seconds");
					MainAudioManipulator.getInstance().startTimeOverlay
							.setText("");
					MainAudioManipulator.getInstance().lengthOverlay
							.setText("");
				}
			}
		}

		// check for whether the audio list is empty
		if (MainAudioManipulator.getInstance().audioFiles.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please add audio files for overlay");

			passedOrNot = false;
		}

		return passedOrNot;
	}

	/**
	 * allChecksReplace Method does all the checks for replace such as checks
	 * for whether the input file is of the right format or not.
	 */

	protected boolean allChecksReplace() {
		boolean passedOrNot = true;

		// Reference to File.probeContentType
		// http://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileTypeDetector.html

		// check for whether the input audio file chosen by the user is an audio
		// file or not
		File file = new File(MainAudioManipulator.getInstance().inputFile);
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
									+ MainAudioManipulator.getInstance().inputFile
									+ " does not refer to a valid audio file. Please select a new input file!");
			passedOrNot = false;
		}
		return passedOrNot;
	}

	/**
	 * allChecksAudio Method does all the checks for Audio Manipulator - calls
	 * the methods allChecksExtract(), allChecksOverlay(), allChecksReplace()
	 * and audioSignalChecks() to determine whether the checks passed or not.
	 */
	protected boolean allChecksAudio() {

		boolean passedAllChecks = true;
		boolean passedSignal = true;
		boolean passedExtract = true;
		boolean passedReplace = true;
		boolean passedOverlay = true;

		// do the checks for audioSignal
		passedSignal = audioSignalCheck();

		// do all the checks for extract
		if (MainAudioManipulator.getInstance().extractEnable) {
			passedExtract = allChecksExtract();
		}

		// do all the checks for replace
		if (MainAudioManipulator.getInstance().replaceEnable) {
			passedReplace = allChecksReplace();
		}

		// do all the checks for overlay
		if (MainAudioManipulator.getInstance().overlayEnable) {

			passedOverlay = allChecksOverlay();

		}

		// if all four checks pass, return true, otherwise return false
		if (passedSignal && passedExtract && passedReplace && passedOverlay) {
			passedAllChecks = true;

		} else {
			passedAllChecks = false;
		}

		return passedAllChecks;
	}
}
