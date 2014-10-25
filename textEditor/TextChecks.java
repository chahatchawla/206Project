package textEditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - text checks class
 * 
 * Purpose: The purpose of this class is to perform all the error checks for the
 * features adding a title and/or credit screen . It also provides some helper
 * methods that are necessary for these checks. This class is used in
 * TextSave.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class TextChecks {
	/**
	 * backgroundImageCheck Method does all the checks for the background image
	 * such as check if no option is selected and checks to ensure that the
	 * background image is the same for both title and credit screen
	 * 
	 */
	protected boolean backgroundImageCheck() {
		boolean passed = false;

		// If no background option was specified
		if (!MainTextEditor.getInstance().defaultCheck.isSelected()
				&& !MainTextEditor.getInstance().overlayCheck.isSelected()
				&& !MainTextEditor.getInstance().frameCheck.isSelected()) {
			passed = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select of the background options");
		}
		// If only credit screen is enabled
		else if (MainTextEditor.getInstance().backgroundImageTitle == 3
				&& MainTextEditor.getInstance().backgroundImageCredit != 3) {
			passed = true;
		}
		// If only title screen is enabled
		else if (MainTextEditor.getInstance().backgroundImageCredit == 3
				&& MainTextEditor.getInstance().backgroundImageTitle != 3) {
			passed = true;
		}
		// If both title screen and credit screen are not enabled
		else if (MainTextEditor.getInstance().backgroundImageTitle == 3
				&& MainTextEditor.getInstance().backgroundImageCredit == 3) {
			passed = true;
		}
		// If both title screen and credit screen are enabled and both have the
		// same background image
		else if (MainTextEditor.getInstance().backgroundImageTitle == MainTextEditor
				.getInstance().backgroundImageCredit) {
			passed = true;
		}
		// If both title screen and credit screen are enabled but both do not
		// have the same background image
		else {
			passed = false;

			// Inform the user
			JOptionPane
					.showMessageDialog(
							null,
							"ERROR: background image option needs to be the same if both title and credit screen are selected");

			MainTextEditor.getInstance().overlayCheck.setSelected(false);
			MainTextEditor.getInstance().defaultCheck.setSelected(false);
			MainTextEditor.getInstance().frameCheck.setSelected(false);
		}

		return passed;
	}

	/**
	 * timeChecks Method is a helper method that checks whether the time inputs
	 * given by the user were in the hh:mm:ss format or not
	 * 
	 * @param frameTime
	 * 
	 */
	protected int timeChecks(String frameTime) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the frameTime is in the format hh:mm:ss
		Pattern timePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
		Matcher startMatcher = timePattern.matcher(frameTime);
		int passed = 0;
		// if the frameTime is not in the right format
		if (!(startMatcher.find() && frameTime.length() == 8)) {
			passed = 1;
		}
		return passed;
	}

	/**
	 * allChecks Method does all the checks for text edit such as check for
	 * backgroundImageOption, detects empty fields, integer checks and also
	 * checks for whether the duration is within the video range
	 * 
	 */

	protected boolean allChecks() {
		boolean passedOrNot = true;
		boolean passedBackgroundCheck;

		if (MainTextEditor.getInstance().screenType.equals("Title Screen")) {

			// set the backgroundImageTitle;
			MainTextEditor.getInstance().backgroundImageTitle = MainTextEditor
					.getInstance().backgroundImageOption;

			// check for the backgroundImageOption
			passedBackgroundCheck = backgroundImageCheck();

		} else {
			// set the backgroundImageCredit;
			MainTextEditor.getInstance().backgroundImageCredit = MainTextEditor
					.getInstance().backgroundImageOption;

			// check for the backgroundImageOption
			passedBackgroundCheck = backgroundImageCheck();
		}

		if (passedBackgroundCheck) {

			// If no duration was specified
			if (MainTextEditor.getInstance().addDuration.getText().trim()
					.equals("")) {
				passedOrNot = false;
				JOptionPane.showMessageDialog(null,
						"ERROR: please specify the duration");

			} else if (!MainTextEditor.getInstance().addDuration.getText()
					.trim().equals("")) {
				Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
				Matcher matchesInteger = integerPattern.matcher(MainTextEditor
						.getInstance().addDuration.getText());
				boolean isInteger = matchesInteger.matches();

				// if duration is an integer
				if (isInteger) {
					int duration = Integer.parseInt(MainTextEditor
							.getInstance().addDuration.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(MainTextEditor.getInstance().videoLength));

					// if duration is more than the length of the video
					if (duration > lengthOfVideo) {
						JOptionPane.showMessageDialog(null,
								"ERROR: duration cannot be more than "
										+ lengthOfVideo + " seconds");
						MainTextEditor.getInstance().addDuration.setText("");
						passedOrNot = false;
					}

				}
				// if duration is not an integer
				else {
					JOptionPane
							.showMessageDialog(null,
									"ERROR: please specify the duration that is 1 or more seconds and is an integer");
					MainTextEditor.getInstance().addDuration.setText("");
					passedOrNot = false;
				}

				// If a frame background was chosen without specifying the frame
				// time
			} else if (MainTextEditor.getInstance().backgroundImageOption == 2) {
				int passedTimeCheck = timeChecks(MainTextEditor.getInstance().addTimeFrame
						.getText().trim());
				if (passedTimeCheck == 1) {
					MainTextEditor.getInstance().addTimeFrame.setText("");
					passedOrNot = false;
					JOptionPane.showMessageDialog(null,
							"ERROR: please specify the frame time in hh:mm:ss");
				}

				if (passedOrNot) {
					int convertedFrameTime = convertToSeconds(MainTextEditor
							.getInstance().addTimeFrame.getText().trim());
					int lengthOfVideo = (int) (Double
							.parseDouble(MainTextEditor.getInstance().videoLength));
					// Check the time of frame is within the range of the video
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - "
										+ lengthOfVideo + " seconds");
						MainTextEditor.getInstance().addTimeFrame.setText("");
					}
				}
			}
		} else {
			passedOrNot = false;
		}
		return passedOrNot;
	}

	/**
	 * convertToSeconds Method is a helper method changes a string in the format
	 * hh:mm:ss to seconds
	 * 
	 * @param longFormat
	 */
	protected int convertToSeconds(String myDateString) {

		// Reference for convertingToSeconds:
		// http://stackoverflow.com/questions/11994790/parse-time-of-format-hhmmss

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = null;

		// parse the input to a Date format
		try {
			date = sdf.parse(myDateString);
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
}
