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
	 * allChecksSnapShot Method does all the checks for snapshot option
	 */

	private boolean allChecksSnapshot() {
		// Get the video path and length
		VideoManipulator.getInstance().vpf.setVideoInfo();
		boolean passedOrNot = true;

		// check whether the input frame time is in the right format or not,
		// using timeChecks()
		boolean passedTimeCheckSnap = timeChecks(VideoManipulator.getInstance().timeSnapshot.getText().trim());

		// if it is in the wrong format, inform the user and allow them
		// to enter the time again
		if (passedTimeCheckSnap == false) {
			VideoManipulator.getInstance().timeSnapshot.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: snapshot frame time can only be in the format hh:mm:ss");
		}

		// check for whether the given time smaller than the length of the video
		else if (passedOrNot) {

			// convert the time from hh:mm:ss format to seconds
			int convertedTime = convertToSeconds(VideoManipulator.getInstance().timeSnapshot.getText().trim());

			int lengthOfVideo = (int) (Double.parseDouble(VideoManipulator.getInstance().videoLength));

			// if convertedTime is more than the length of the video, inform the
			// user and allow them to enter the snapshot frame time again
			if (convertedTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(null,
						"ERROR: snapshot frame time can not be more than the length of the video - " + lengthOfVideo+ " seconds");
				VideoManipulator.getInstance().timeSnapshot.setText("");
			}
		}

		// check for whether the outputSnapshotName field is empty
		if (VideoManipulator.getInstance().outputSnapshotName.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output snapshot name");
		}

		// check whether the outputSnapshotName specified user already exists in
		// the
		// project directory
		String outputFile = VideoManipulator.getInstance().workingDir + "/" + VideoManipulator.getInstance().outputSnapshotName.getText()
				+ ".png";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Cancel", "Overwrite" };
			int optionChosen = JOptionPane.showOptionDialog(null, "ERROR: "
					+ VideoManipulator.getInstance().outputSnapshotName.getText() + ".png already exists. "
					+ "Do you want to overwrite the existing file?",
					"File Exists!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			if (optionChosen == 1) {
				f.delete(); // Delete the existing file
			} else {
				VideoManipulator.getInstance().outputSnapshotName.setText("");
				passedOrNot = false;
			}
		}
		return passedOrNot;
	}

	/**
	 * allChecksLoop Method does all the checks for loop video option
	 */

	private boolean allChecksLoop() {
		boolean passedOrNot = true;
		// Get the video path and length
		VideoManipulator.getInstance().vpf.setVideoInfo();

		// check whether the start time is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckStart = timeChecks(VideoManipulator.getInstance().timeStart.getText().trim());

		// check whether the length is in the right format or not, using
		// timeChecks()
		boolean passedTimeCheckLength = timeChecks(VideoManipulator.getInstance().timeLength.getText().trim());

		// if the start time is in the wrong format, inform the user and allow
		// them
		// to enter the time again
		if (passedTimeCheckStart == false) {
			VideoManipulator.getInstance().timeStart.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video start time can only be in the format hh:mm:ss");
		}

		// if the length is in the wrong format, inform the user and allow them
		// to enter the time again
		if (passedTimeCheckLength == false) {
			VideoManipulator.getInstance().timeLength.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: loop video length can only be in the format hh:mm:ss");
		}

		// check for whether the given length + start time is smaller than
		// the length of the video or not
		else if (passedOrNot) {
			int convertedStartTime = convertToSeconds(VideoManipulator.getInstance().timeStart.getText()
					.trim());
			int convertedLength = convertToSeconds(VideoManipulator.getInstance().timeLength.getText().trim());
			int totalTime = convertedStartTime + convertedLength;
			int lengthOfVideo = (int) (Double.parseDouble(VideoManipulator.getInstance().videoLength));

			// if totalTime is more than the length of the video, inform the
			// user and allow them to enter the start time and length again
			if (totalTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time + length for loop video can not be more than the length of the video - " + lengthOfVideo+ " seconds");
				VideoManipulator.getInstance().timeStart.setText("");
				VideoManipulator.getInstance().timeLength.setText("");
			}
		}

		// check for whether the loop field is empty
		if (VideoManipulator.getInstance().loop.getText().trim().equals("")) {
			JOptionPane
			.showMessageDialog(
					null,
					"ERROR: please specify how many times you would like to loop the extracted video");
			passedOrNot = false;
		}
		else {
			Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
			Matcher matchesInteger = integerPattern.matcher(VideoManipulator.getInstance().loop.getText());
			boolean isInteger = matchesInteger.matches();

			if (!isInteger){         
				JOptionPane.showMessageDialog(null,
						"ERROR: please specify the loop number that is 1 or more");
				passedOrNot = false;
			}
		}

		// check for whether the outputLoopVideoName field is empty
		if (VideoManipulator.getInstance().outputLoopVideoName.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output loop video name");
			passedOrNot = false;
		}

		// check whether the outputLoopVideoName specified user already exists
		// in the
		// project directory
		String outputFile = VideoManipulator.getInstance().workingDir + "/" + VideoManipulator.getInstance().outputLoopVideoName.getText()
				+ ".mp4";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = { "Cancel", "Overwrite" };
			int optionChosen = JOptionPane.showOptionDialog(null, "ERROR: "
					+ VideoManipulator.getInstance().outputLoopVideoName.getText() + ".mp4 already exists. "
					+ "Do you want to overwrite the existing file?",
					"File Exists!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			if (optionChosen == 1) {
				f.delete(); // Delete the existing file
			} else {
				VideoManipulator.getInstance().outputLoopVideoName.setText("");
				passedOrNot = false;
			}
		}

		return passedOrNot;
	}

	/**
	 * allChecksFilter Method does all the checks for filter option
	 */

	private boolean allChecksFilter() {
		boolean passedOrNot = true;

		if (VideoManipulator.getInstance().filter.equals("")) {

			// if none of the six are selected, show an error message to
			// the user and allow them to choose one
			passedOrNot = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select one of the filter options");
		}

		return passedOrNot;
	}

	/**
	 * allChecksVideo Method does all the checks for Video Manipulator
	 */
	protected boolean allChecksVideo() {
		boolean passedAllChecks = true;

		// do all the checks for snapshot
		if (VideoManipulator.getInstance().snapshotEnable) {
			passedAllChecks = allChecksSnapshot();
		}

		// do all the checks for loopVideo
		if (VideoManipulator.getInstance().loopVideoEnable) {
			passedAllChecks = allChecksLoop();
		}

		// do all the checks for filter
		if (VideoManipulator.getInstance().filterEnable) {
			passedAllChecks = allChecksFilter();
		}
		return passedAllChecks;
	}
}
