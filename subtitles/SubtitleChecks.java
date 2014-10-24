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

public class SubtitleChecks {

	/**
	 * convertToSeconds Method changes a string in the format hh:mm:ss to
	 * seconds
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
	 * timeChecks Method checks whether the time inputs given by the user were
	 * in the hh:mm:ss,mmm format or not
	 * 
	 * @param startTime
	 *            , endTime
	 */

	protected int timeChecks(String startTime, String endTime) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the startTime and length are in the format hh:mm:ss,mmm
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
	 * allChecksExtract Method does all the checks for extract
	 */

	protected boolean allChecksAdd() {
		// Get the video path and length
		MainSubtitles.getInstance().spf.setVideoInfo();
		boolean passedOrNot = true;


		// check whether the input startTime and lengthTime is in the right
		// format or not using timeChecks()
		int passedTimeCheckExtract = timeChecks(MainSubtitles.getInstance().startTime.getText()
				.trim(), MainSubtitles.getInstance().endTime.getText().trim());

		// if both are in the wrong format, inform the user and allow them
		// to enter the start time and length again
		if (passedTimeCheckExtract == 3) {
			MainSubtitles.getInstance().startTime.setText("");
			MainSubtitles.getInstance().endTime.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(
					null,
					"ERROR: start time and end time can only be in the format hh:mm:ss,mmm");
		}

		// if start time is in the wrong format, inform the user and allow
		// them to enter the start time again
		else if (passedTimeCheckExtract == 1) {
			MainSubtitles.getInstance().startTime.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: start time can only be in the format hh:mm:ss,mmm");

		}

		// if length is in the wrong format, inform the user and allow them
		// to enter the length again
		else if (passedTimeCheckExtract == 2) {
			MainSubtitles.getInstance().endTime.setText("");
			passedOrNot = false;
			JOptionPane
			.showMessageDialog(null,
					"ERROR: end time can only be in the format hh:mm:ss,mmm");
		}

		// check for whether the given duration + start time is smaller than
		// the length of the video or not
		else if (passedOrNot) {


			int startTime = convertToSeconds(MainSubtitles.getInstance().startTime.getText()
					.trim());

			int endTime = convertToSeconds(MainSubtitles.getInstance().endTime.getText()
					.trim());

			int lengthOfVideo = (int) (Double.parseDouble(MainSubtitles.getInstance().videoLength));

			// if totalTime is more than the length of the video, inform the
			// user and allow them to enter the start time and length again
			if (endTime > lengthOfVideo) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: end time can not be more than the length of the video");
				MainSubtitles.getInstance().endTime.setText("");

			}

			else if (endTime < startTime) {
				passedOrNot = false;
				JOptionPane
				.showMessageDialog(
						null,
						"ERROR: start time can not be more than the end time selected");
				MainSubtitles.getInstance().startTime.setText("");	
			}

			// check for whether the text field is empty
			else if (MainSubtitles.getInstance().text.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null,
						"ERROR: please specify some text for subtitles");
				passedOrNot = false;
			}
		}


		return passedOrNot;




	}

	/**
	 * allChecksGenerate Method does all the checks for extract
	 */

	protected boolean allChecksGenerate() {
		// Get the video path and length
		MainSubtitles.getInstance().spf.setVideoInfo();
		boolean passedOrNot = true;


		// check for whether the outputFileName field is empty
		if (MainSubtitles.getInstance().outputFileName.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"ERROR: please specify an output .srt file name");
			passedOrNot = false;
		}

		// check whether the outputFileName specified user already exists in the
		// project directory
		String outputFile = MainSubtitles.getInstance().workingDir + "/" + MainSubtitles.getInstance().outputFileName.getText()
				+ ".srt";
		File f = new File(outputFile);
		if (f.exists()) {

			// Reference for JOptionPane() as above.

			// Allow user to choose either overwriting the current file or
			// change the output file name
			Object[] existOptions = {"Overwrite", "Cancel"};
			int optionChosen = JOptionPane.showOptionDialog(null, "ERROR: "
					+ MainSubtitles.getInstance().outputFileName.getText() + ".srt already exists. "
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
