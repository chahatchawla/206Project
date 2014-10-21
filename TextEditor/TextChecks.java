package TextEditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class TextChecks {
	
	
	protected boolean backgroundImageCheck() {
		boolean passed = false;

		// If no background option was specified
		if (!TextEditor.getInstance().defaultCheck.isSelected() && !TextEditor.getInstance().overlayCheck.isSelected()
				&& !TextEditor.getInstance().frameCheck.isSelected()) {
			passed = false;
			JOptionPane.showMessageDialog(null,
					"ERROR: please select of the background options");
		}

		else if (TextEditor.getInstance().backgroundImageTitle == 3 && TextEditor.getInstance().backgroundImageCredit != 3) {
			passed = true;
		} else if (TextEditor.getInstance().backgroundImageCredit == 3 && TextEditor.getInstance().backgroundImageTitle != 3) {
			passed = true;
		}

		else if (TextEditor.getInstance().backgroundImageTitle == 3 && TextEditor.getInstance().backgroundImageCredit == 3) {
			passed = true;
		} else if (TextEditor.getInstance().backgroundImageTitle == TextEditor.getInstance().backgroundImageCredit) {
			passed = true;
		} else {
			passed = false;

			JOptionPane
			.showMessageDialog(
					null,
					"ERROR: background image option needs to be the same if both title and credit screen are selected");

			TextEditor.getInstance().overlayCheck.setSelected(false);
			TextEditor.getInstance().defaultCheck.setSelected(false);
			TextEditor.getInstance().frameCheck.setSelected(false);
		}

		return passed;
	}

	protected int timeChecks(String frameTime) {

		// Reference: from assignment 3 of Chahat Chawla
		// checks whether the startTime and length are in the format hh:mm:ss
		Pattern timePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
		Matcher startMatcher = timePattern.matcher(frameTime);
		int passed = 0;
		// if the startTime is not in the right format
		if (!(startMatcher.find() && frameTime.length() == 8)) {
			passed = 1;
		}
		return passed;
	}

	/**
	 * Method returns true if the text editor tab passed all the checks (user
	 * input is valid)
	 */
	protected boolean allChecks() {
		boolean passedOrNot = true;
		boolean passedBackgroundCheck;

		if (TextEditor.getInstance().screenType.equals("Title Screen")) {

			// set the backgroundImageTitle;
			TextEditor.getInstance().backgroundImageTitle = TextEditor.getInstance().backgroundImageOption;

			// check for the backgroundImageOption
			passedBackgroundCheck = backgroundImageCheck();

		} else {
			// set the backgroundImageCredit;
			TextEditor.getInstance().backgroundImageCredit = TextEditor.getInstance().backgroundImageOption;

			// check for the backgroundImageOption
			passedBackgroundCheck = backgroundImageCheck();
		}

		if (passedBackgroundCheck) {

			// If no duration was specified
			if (TextEditor.getInstance().addDuration.getText().trim().equals("")) {
				passedOrNot = false;
				JOptionPane.showMessageDialog(null,
						"ERROR: please specify the duration");

				/*
				 * If text area has lines > 10 the number of lines were used to
				 * limit the title/credit length specifying number of lines can
				 * decrease the possibility of having large texts that exceeds
				 * the screen dimensions since the maximum font size is 72.
				 * Although it doesn't guarantee that the input will always be
				 * in right size, limiting line numbers is easier for the user
				 * since he can go back and count the extra lines to delete
				 * them. Which is fairly hard task if words or characters were
				 * used as a limit (they also don't guarantee right text size)
				 */
			} else if (!TextEditor.getInstance().addDuration.getText().trim().equals("")){
				Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
				Matcher matchesInteger = integerPattern.matcher(TextEditor.getInstance().addDuration.getText());
				boolean isInteger = matchesInteger.matches();

				if (!isInteger){
					JOptionPane.showMessageDialog(null,
							"ERROR: please specify the duration that is 1 or more seconds");
					passedOrNot = false;
				}


			} else if (TextEditor.getInstance().addTextArea.getLineCount() > 10) {
				JOptionPane.showMessageDialog(null,
						"ERROR: Text can only be 10 lines.");
				passedOrNot = false;

				// If a frame background was chosen without specifying the frame
				// time
			} else if (TextEditor.getInstance().backgroundImageOption == 2) {
				int passedTimeCheck = timeChecks(TextEditor.getInstance().addTimeFrame.getText().trim());
				if (passedTimeCheck == 1) {
					TextEditor.getInstance().addTimeFrame.setText("");
					passedOrNot = false;
					JOptionPane.showMessageDialog(null,
							"ERROR: please specify the frame time in hh:mm:ss");
				}

				if (passedOrNot) {
					int convertedFrameTime = convertToSeconds(TextEditor.getInstance().addTimeFrame
							.getText().trim());
					int lengthOfVideo = (int) (Double.parseDouble(TextEditor.getInstance().videoLength));
					// Check the time of frame is within the range of the video
					if (convertedFrameTime > lengthOfVideo) {
						passedOrNot = false;
						JOptionPane
						.showMessageDialog(null,
								"ERROR: frame time can not be more than the length of the video - " + lengthOfVideo+ " seconds");
						TextEditor.getInstance().addTimeFrame.setText("");
					}
				}
			}
		}
		else {
			passedOrNot = false;
		}
		return passedOrNot;
	}

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
