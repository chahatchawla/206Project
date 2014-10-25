package VideoManipulator;

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
 * SoftEng206 Project - video checks class
 * 
 * Purpose: The purpose of this class is to perform all the error checks for the
 * features snapshot, extract then loop video and filter. It also provides some
 * helper methods that are necessary for these checks. This class is used in
 * VideoSave.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class VideoChecks {

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
	 * 
	 */

	protected boolean timeChecks(String time) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the user specified time is in the format hh:mm:ss
		Pattern timePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
		Matcher timeMatcher = timePattern.matcher(time);

		boolean passed = true;

		// if the time is not in the right format, return false
		if (!(timeMatcher.find() && time.length() == 8)) {
			passed = false;
		}
		return passed;
	}

	/**
	 * allChecksSnapshot Method does all the checks for snapshot such as check
	 * time formats, check whether times are within video range, detect empty
	 * fields and also checks for existing files.
	 * 
	 */

	private boolean allChecksSnapshot() {

		// Reference for JOptionPane() :
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();
		boolean passedOrNot = true;

		// check whether the input frame time is in the right format or not,
		// using timeChecks()
		boolean passedTimeCheckSnap = timeChecks(MainVideoManipulator
				.getInstance().timeSnapshot.getText().trim());

		// if it is in the wrong format, inform the user and allow them
		// to enter the time again
		if (passedTimeCheckSnap == false) {
			MainVideoManipulator.getInstance().timeSnapshot.setText("");
			passedOrNot = false;
			JOptionPane
					.showMessageDialog(null,
							"ERROR: snapshot frame time can only be in the format hh:mm:ss");
		}

		// check for whether the given time smaller than the length of the video
		else if (passedOrNot) {

			// convert the time from hh:mm:ss format to seconds
			int convertedTime = convertToSeconds(MainVideoManipulator
					.getInstance().timeSnapshot.getText().trim());

			int lengthOfVideo = (int) (Double.parseDouble(MainVideoManipulator
					.getInstance().videoLength));

			// if convertedTime is more than the length of the video, inform the
			// user and allow them to enter the snapshot frame time again
			if (convertedTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane.showMessageDialog(null,
						"ERROR: snapshot frame time can not be more than the length of the video - "
								+ lengthOfVideo + " seconds");
				MainVideoManipulator.getInstance().timeSnapshot.setText("");
			}
		}

		// check for whether the outputSnapshotName field is empty
		if (MainVideoManipulator.getInstance().outputSnapshotName.getText()
				.trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output snapshot name");
			passedOrNot = false;

		}

		// check whether the outputSnapshotName specified user already exists in
		// the
		// project directory
		String outputFile = MainVideoManipulator.getInstance().workingDir
				+ "/"
				+ MainVideoManipulator.getInstance().outputSnapshotName
						.getText() + ".png";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Overwrite", "Cancel" };
			int optionChosen = JOptionPane
					.showOptionDialog(
							null,
							"ERROR: "
									+ MainVideoManipulator.getInstance().outputSnapshotName
											.getText()
									+ ".png already exists. "
									+ "Do you want to overwrite the existing file?",
							"File Exists!", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, existOptions,
							existOptions[0]);
			if (optionChosen == 0) {
				f.delete(); // Delete the existing file
			} else {
				MainVideoManipulator.getInstance().outputSnapshotName
						.setText("");
				passedOrNot = false;
			}
		}
		return passedOrNot;
	}

	/**
	 * allChecksLoop Method does all the checks for loop such as check time
	 * formats, check whether times are within video range, whether loop input
	 * is an integer, checks for existing files and detects empty fields.
	 */
	private boolean allChecksLoop() {

		// Reference for JOptionPane() :
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

		boolean passedOrNot = true;
		// Get the video path and length
		MainVideoManipulator.getInstance().vpf.setVideoInfo();

		// check whether the start time is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckStart = timeChecks(MainVideoManipulator
				.getInstance().timeStart.getText().trim());

		// check whether the length is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckLength = timeChecks(MainVideoManipulator
				.getInstance().timeLength.getText().trim());

		// if the start time is in the wrong format, inform the user and allow
		// them
		// to enter the time again
		if (passedTimeCheckStart == false) {
			MainVideoManipulator.getInstance().timeStart.setText("");
			passedOrNot = false;
			JOptionPane
					.showMessageDialog(null,
							"ERROR: loop video start time can only be in the format hh:mm:ss");
		}

		// if the length is in the wrong format, inform the user and allow them
		// to enter the time again
		if (passedTimeCheckLength == false) {
			MainVideoManipulator.getInstance().timeLength.setText("");
			passedOrNot = false;
			JOptionPane
					.showMessageDialog(null,
							"ERROR: loop video length can only be in the format hh:mm:ss");
		}

		// check for whether the given length + start time is smaller than
		// the length of the video or not
		else if (passedOrNot) {
			int convertedStartTime = convertToSeconds(MainVideoManipulator
					.getInstance().timeStart.getText().trim());
			int convertedLength = convertToSeconds(MainVideoManipulator
					.getInstance().timeLength.getText().trim());
			int totalTime = convertedStartTime + convertedLength;
			int lengthOfVideo = (int) (Double.parseDouble(MainVideoManipulator
					.getInstance().videoLength));

			// if totalTime is more than the length of the video, inform the
			// user and allow them to enter the start time and length again
			if (totalTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
						.showMessageDialog(
								null,
								"ERROR: start time + length for loop video can not be more than the length of the video - "
										+ lengthOfVideo + " seconds");
				MainVideoManipulator.getInstance().timeStart.setText("");
				MainVideoManipulator.getInstance().timeLength.setText("");
			}
		}

		// check for whether the loop field is empty
		if (MainVideoManipulator.getInstance().loop.getText().trim().equals("")) {
			JOptionPane
					.showMessageDialog(
							null,
							"ERROR: please specify how many times you would like to loop the extracted video");
			passedOrNot = false;
		}
		// check for whether the loop input is an integer
		else {
			Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
			Matcher matchesInteger = integerPattern
					.matcher(MainVideoManipulator.getInstance().loop.getText());
			boolean isInteger = matchesInteger.matches();
			// if it is not an integer, inform the user
			if (!isInteger) {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please specify the loop number that is 1 or more");
				passedOrNot = false;
			}
		}

		// check for whether the outputLoopVideoName field is empty
		if (MainVideoManipulator.getInstance().outputLoopVideoName.getText()
				.trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output loop video name");
			passedOrNot = false;
		}

		// check whether the outputLoopVideoName specified user already exists
		// in the project directory
		String outputFile = MainVideoManipulator.getInstance().workingDir
				+ "/"
				+ MainVideoManipulator.getInstance().outputLoopVideoName
						.getText() + ".mp4";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Overwrite", "Cancel" };
			int optionChosen = JOptionPane
					.showOptionDialog(
							null,
							"ERROR: "
									+ MainVideoManipulator.getInstance().outputLoopVideoName
											.getText()
									+ ".mp4 already exists. "
									+ "Do you want to overwrite the existing file?",
							"File Exists!", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, existOptions,
							existOptions[0]);
			if (optionChosen == 0) {
				f.delete(); // Delete the existing file
			} else {
				MainVideoManipulator.getInstance().outputLoopVideoName
						.setText("");
				passedOrNot = false;
			}
		}

		return passedOrNot;
	}

	/**
	 * allChecksFilter Method does all the checks for filter option such as
	 * detects empty fields.
	 */

	private boolean allChecksFilter() {

		// Reference for JOptionPane() :
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
		boolean passedOrNot = true;

		if (MainVideoManipulator.getInstance().filter.equals("")) {

			// if none of the six are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the filter options");
		}

		return passedOrNot;
	}

	/**
	 * allChecksVideo Method does all the checks for Video Manipulator - calls
	 * the methods allChecksSnapshot(), allChecksLoop() and allChecksFilter() to
	 * determine whether the checks passed or not.
	 */
	protected boolean allChecksVideo() {

		boolean passedAllChecks = true;
		boolean passedSnapshot = true;
		boolean passedLoop = true;
		boolean passedFilter = true;

		// do all the checks for snapshot
		if (MainVideoManipulator.getInstance().snapshotEnable) {
			passedSnapshot = allChecksSnapshot();
		}

		// do all the checks for loopVideo
		if (MainVideoManipulator.getInstance().loopVideoEnable) {
			passedLoop = allChecksLoop();
		}

		// do all the checks for filter
		if (MainVideoManipulator.getInstance().filterEnable) {
			passedFilter = allChecksFilter();
		}
		// if all three checks pass, return true, otherwise return false
		if (passedSnapshot && passedLoop && passedFilter) {
			passedAllChecks = true;

		} else {
			passedAllChecks = false;
		}
		return passedAllChecks;
	}
}
