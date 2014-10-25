package subtitles;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - subtitle checks class
 * 
 * Purpose: The purpose of this class is to perform all the error checks for the
 * features create a new srt file and import subtitles. It also provides some
 * helper methods that are necessary for these checks. This class is used in
 * SubtitlesTable.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class SubtitleChecks {

	/**
	 * convertToSeconds Method is a helper method changes a string in the format
	 * hh:mm:ss to seconds
	 * 
	 * @param longFormat
	 */
	protected int convertToSeconds(String longFormat) {

		// Reference for convertingToSeconds:
		// http://stackoverflow.com/questions/11994790/parse-time-of-format-hhmmss

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
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
	protected int timeChecks(String startTime, String endTime) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the startTime and length are in the format
		// hh:mm:ss,mmm
		Pattern timePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d");
		Matcher startMatcher = timePattern.matcher(startTime);
		Matcher lengthMatcher = timePattern.matcher(endTime);

		int passed = 0;

		// if the startTime is not in the right format, return 1
		if (!(startMatcher.find() && startTime.length() == 12)) {
			passed = passed + 1;
		}
		// if the length is not in the right format, return 2, if startTime is
		// wrong too, it will return 3
		if (!(lengthMatcher.find() && endTime.length() == 12)) {
			passed = passed + 2;
		}

		return passed;
	}

	/**
	 * allChecksAdd Method does all the checks for when the user creates a new
	 * subtitle and adds it to the table such as check time formats, check
	 * whether times are within video range and detect empty fields.
	 * 
	 */
	protected boolean allChecksAdd() {

		// Reference for JOptionPane() :
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

		// Get the video path and length
		MainSubtitles.getInstance().spf.setVideoInfo();
		boolean passedOrNot = true;

		// check whether the input startTime and endTime is in the right
		// format or not using timeChecks()
		int passedTimeCheckExtract = timeChecks(
				MainSubtitles.getInstance().startTime.getText().trim(),
				MainSubtitles.getInstance().endTime.getText().trim());

		// if both are in the wrong format, inform the user and allow them
		// to enter the start time and endTime again
		if (passedTimeCheckExtract == 3) {
			MainSubtitles.getInstance().startTime.setText("");
			MainSubtitles.getInstance().endTime.setText("");
			passedOrNot = false;
			JOptionPane
					.showMessageDialog(null,
							"ERROR: start time and end time can only be in the format hh:mm:ss,mmm");
		}

		// if start time is in the wrong format, inform the user and allow
		// them to enter the start time again
		else if (passedTimeCheckExtract == 1) {
			MainSubtitles.getInstance().startTime.setText("");
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: start time can only be in the format hh:mm:ss,mmm");

		}

		// if endTime is in the wrong format, inform the user and allow them
		// to enter the endTime again
		else if (passedTimeCheckExtract == 2) {
			MainSubtitles.getInstance().endTime.setText("");
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: end time can only be in the format hh:mm:ss,mmm");
		}

		// check for whether the given endTime is smaller than the length of the
		// video and whether the end time is bigger than the start time or not
		else if (passedOrNot) {

			int startTime = convertToSeconds(MainSubtitles.getInstance().startTime
					.getText().trim());

			int endTime = convertToSeconds(MainSubtitles.getInstance().endTime
					.getText().trim());

			int lengthOfVideo = (int) (Double.parseDouble(MainSubtitles
					.getInstance().videoLength));

			// if endTime is more than the length of the video, inform the
			// user and allow them to enter the endTime again
			if (endTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: end time can not be more than the length of the video");
				MainSubtitles.getInstance().endTime.setText("");

			}
			// if endTime is smaller than the startTime, inform the
			// user and allow them to enter the startTime again
			else if (endTime < startTime) {
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(null,
								"ERROR: start time can not be more than the end time selected");
				MainSubtitles.getInstance().startTime.setText("");
			}

			// check for whether the text field is empty
			else if (MainSubtitles.getInstance().text.getText().trim()
					.equals("")) {
				JOptionPane.showMessageDialog(null,
						"ERROR: please specify some text for subtitles");
				passedOrNot = false;
			}
		}

		return passedOrNot;

	}

	/**
	 * allChecksGenerate Method does all the checks for generating the srt file
	 * such as detecting empty fields and checks for existing files.
	 * 
	 */
	protected boolean allChecksGenerate() {
		// Reference for JOptionPane() :
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
	
		// Get the video path and length
		MainSubtitles.getInstance().spf.setVideoInfo();
		boolean passedOrNot = true;
		
		if (MainSubtitles.getInstance().model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(null,
					"ERROR: subtitle table is empty, can not generate an empty .srt file");
			passedOrNot = false;
		}

		// check for whether the outputFileName field is empty
		else if (MainSubtitles.getInstance().outputFileName.getText().trim()
				.equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output .srt file name");
			passedOrNot = false;
		}

		// check whether the outputFileName specified user already exists in the
		// project directory
		String outputFile = MainSubtitles.getInstance().workingDir + "/"
				+ MainSubtitles.getInstance().outputFileName.getText() + ".srt";
		File f = new File(outputFile);
		if (f.exists()) {

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Overwrite", "Cancel" };
			int optionChosen = JOptionPane.showOptionDialog(null, "ERROR: "
					+ MainSubtitles.getInstance().outputFileName.getText()
					+ ".srt already exists. "
					+ "Do you want to overwrite the existing file?",
					"File Exists!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			if (optionChosen == 0) {
				f.delete(); // Delete the existing file
			} else {
				MainSubtitles.getInstance().outputFileName.setText("");
				passedOrNot = false;
			}
		}

		return passedOrNot;

	}
}
