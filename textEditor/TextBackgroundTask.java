package textEditor;

import java.io.File;

public class TextBackgroundTask{

	private String firstInput;
	private String lastOutput;

	protected String makeTextCommand(String input, String output){

		firstInput = input;
		lastOutput = output;
		// Get the video path and length
		MainTextEditor.getInstance().tpf.setVideoInfo();
		/*
		 * Reference for all the avconv commands
		 * https://libav.org/avconv.html and a combination of many
		 * searches found on google, final command selected after a lot
		 * of trials and testing
		 */

		// create text files for title screen and credit screen for the
		// purposes of checking which screen the user wants to implement
		File fileTitle = new File(MainTextEditor.getInstance().workingDir + "/.TitleText.txt");
		File fileCredit = new File(MainTextEditor.getInstance().workingDir + "/.CreditText.txt");

		// if the user wants to overlay
		if (MainTextEditor.getInstance().backgroundImageOption == 0) {

			// if the user has saved title screen settings but does not
			// want to implement credit screen
			if (fileTitle.exists() && !fileCredit.exists()) {
				StringBuilder finalTitleCommand = new StringBuilder();
				// add text to video
				finalTitleCommand
				.append("avconv -ss 0 -i "
						+ firstInput
						+ " -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().titleFontName
						+ "':textfile='"
						+ fileTitle
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().titleFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().titleFontColour + "\" -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4");
				finalTitleCommand.append(";");
				// concatenate the videos together = gives the final
				// output
				finalTitleCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts ; avconv -ss "
						+ MainTextEditor.getInstance().titleDuration
						+ " -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalTitleCommand.append(";");
				MainTextEditor.getInstance().titleCommand = finalTitleCommand.toString();
				System.out.println(MainTextEditor.getInstance().titleCommand);
				return MainTextEditor.getInstance().titleCommand;
			}

			// if the user has saved credit screen settings but does not
			// want to implement title screen
			else if (!fileTitle.exists() && fileCredit.exists()) {
				StringBuilder finalCreditCommand = new StringBuilder();
				// calculates the start time for credit screen
				int time = (int) (Double.parseDouble(MainTextEditor.getInstance().videoLength) - Integer
						.parseInt(MainTextEditor.getInstance().creditDuration));
				String startTime = "" + time;
				// add text to video
				finalCreditCommand
				.append("avconv -ss "
						+ startTime
						+ " -i "
						+ firstInput
						+ " -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().creditFontName
						+ "':textfile='"
						+ fileCredit
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().creditFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().creditFontColour + "\" -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4");
				finalCreditCommand.append(";");
				// concatenate the videos together = gives the final
				// output
				finalCreditCommand
				.append("avconv -ss 0 -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
						+ startTime
						+ " -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts ; avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalCreditCommand.append(";");
				MainTextEditor.getInstance().creditCommand = finalCreditCommand.toString();

				System.out.println(MainTextEditor.getInstance().creditCommand);
				return MainTextEditor.getInstance().creditCommand;
			}

			// if the user has saved settings for both title screen and
			// credit screen - wants to implement both
			else {
				StringBuilder finalBothCommand = new StringBuilder();

				// calculates the start time and the stop times
				int time = (int) (Double.parseDouble(MainTextEditor.getInstance().videoLength) - Integer
						.parseInt(MainTextEditor.getInstance().creditDuration));
				int time1 = time - Integer.parseInt(MainTextEditor.getInstance().titleDuration);
				String startTime = "" + time;
				String stopTime = "" + time1;
				// add text to title video
				finalBothCommand
				.append("avconv -ss 0 -i "
						+ firstInput
						+ " -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().titleFontName
						+ "':textfile='"
						+ fileTitle
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().titleFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().titleFontColour + "\" -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4");
				finalBothCommand.append(";");
				// add text to credit video
				finalBothCommand
				.append("avconv -ss "
						+ startTime
						+ " -i "
						+ firstInput
						+ " -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().creditFontName
						+ "':textfile='"
						+ fileCredit
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().creditFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().creditFontColour + "\" -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4");
				finalBothCommand.append(";");
				// concatenate the videos together = gives the final
				// output
				finalBothCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file3.ts ; avconv -ss "
						+ MainTextEditor.getInstance().titleDuration
						+ " -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y -t "
						+ stopTime
						+ " "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file5.ts; avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file6.ts ; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file3.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file5.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file6.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalBothCommand.append(";");
				MainTextEditor.getInstance().bothTitleAndCreditCommand = finalBothCommand.toString();

				System.out.println(MainTextEditor.getInstance().bothTitleAndCreditCommand);
				return MainTextEditor.getInstance().bothTitleAndCreditCommand;
			}
		}

		// if the user wants to use a default frame or select a frame
		// from the video
		else {
			String inputFrameTime;
			String inputFrameTime1;
			String cmd;

			// if the user has saved title screen settings but does not
			// want to implement credit screen
			if (fileTitle.exists() && !fileCredit.exists()) {
				StringBuilder finalTitleCommand = new StringBuilder();
				// if they choose to use a defaultFrame
				if (MainTextEditor.getInstance().backgroundImageOption == 1) {
					// screenshot will be taken at 00:00:01
					inputFrameTime = "00:00:01";
				}
				// if they select a frame from the video
				else {
					// screenshot will be taken at the time specified by
					// the user
					inputFrameTime = MainTextEditor.getInstance().titleFrameTime;
				}
				// take a screenshot from the video at the given
				// inputFrametime
				finalTitleCommand.append("avconv -i " + firstInput
						+ " -ss " + inputFrameTime
						+ " -f image2 -vframes 1 " + MainTextEditor.getInstance().hiddenDir
						+ "/out.png");
				finalTitleCommand.append(";");
				// create a video from image for the duration given by
				// the user
				finalTitleCommand
				.append("avconv -loop 1 -shortest -y -i "
						+ MainTextEditor.getInstance().hiddenDir + "/out.png -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4");
				finalTitleCommand.append(";");
				// add text
				finalTitleCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().titleFontName
						+ "':textfile='"
						+ fileTitle
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().titleFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().titleFontColour + "\" -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4");
				finalTitleCommand.append(";");

				// concatenate the videos together = gives the final
				// output
				finalTitleCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts ; avconv -ss 0"
						+ " -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalTitleCommand.append(";");

				MainTextEditor.getInstance().titleCommand = finalTitleCommand.toString();
				return MainTextEditor.getInstance().titleCommand;
			}
			// if the user has saved credit screen settings but does not
			// want to implement title screen
			else if (!fileTitle.exists() && fileCredit.exists()) {
				StringBuilder finalCreditCommand = new StringBuilder();
				// if they choose to use a defaultFrame
				if (MainTextEditor.getInstance().backgroundImageOption == 1) {
					// screenshot will be taken at 00:00:01
					inputFrameTime = "00:00:01";
				}
				// if they select a frame from the video
				else {
					// screenshot will be taken at the time specified by
					// the user
					inputFrameTime = MainTextEditor.getInstance().creditFrameTime;
				}
				// take a screenshot from the video at the given
				// inputFrametime
				finalCreditCommand.append("avconv -i " + firstInput
						+ " -ss " + inputFrameTime
						+ " -f image2 -vframes 1 " + MainTextEditor.getInstance().hiddenDir
						+ "/out.png");
				finalCreditCommand.append(";");
				// create a video from image for the duration given by
				// the user
				finalCreditCommand
				.append("avconv -loop 1 -shortest -y -i "
						+ MainTextEditor.getInstance().hiddenDir + "/out.png -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4");
				finalCreditCommand.append(";");
				// add text
				finalCreditCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().creditFontName
						+ "':textfile='"
						+ fileCredit
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().creditFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().creditFontColour + "\" -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4");
				finalCreditCommand.append(";");
				// concatenate the videos together = gives the final
				// output
				finalCreditCommand
				.append("avconv -ss 0 -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts ; avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file1.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalCreditCommand.append(";");
				MainTextEditor.getInstance().creditCommand = finalCreditCommand.toString();
				return MainTextEditor.getInstance().creditCommand;
			}

			// if the user has saved settings for both title screen and
			// credit screen - wants to implement both
			else {
				StringBuilder finalBothCommand = new StringBuilder();
				// if they choose to use a defaultFrame
				if (MainTextEditor.getInstance().backgroundImageOption == 1) {
					// screenshot will be taken at 00:00:01 for both
					// title screen and credit screen
					inputFrameTime = "00:00:01";
					inputFrameTime1 = "00:00:01";
				}
				// if they select a frame from the video
				else {
					// screenshots will be taken at the time specified
					// by the user
					inputFrameTime = MainTextEditor.getInstance().titleFrameTime;
					inputFrameTime1 = MainTextEditor.getInstance().creditFrameTime;
				}
				// take a screenshot from the video at the given
				// inputFrametime for title screen
				finalBothCommand.append("avconv -i " + firstInput
						+ " -ss " + inputFrameTime
						+ " -f image2 -vframes 1 " + MainTextEditor.getInstance().hiddenDir
						+ "/out.png");
				finalBothCommand.append(";");
				// take a screenshot from the video at the given
				// inputFrametime for credit screen
				finalBothCommand.append("avconv -i " + firstInput
						+ " -ss " + inputFrameTime1
						+ " -f image2 -vframes 1 " + MainTextEditor.getInstance().hiddenDir
						+ "/out1.png");
				finalBothCommand.append(";");
				// create video from image for the duration given by the
				// user for title screen
				finalBothCommand
				.append("avconv -loop 1 -shortest -y -i "
						+ MainTextEditor.getInstance().hiddenDir + "/out.png -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4");
				finalBothCommand.append(";");
				// create video from image for the duration given by the
				// user for credit screen
				finalBothCommand
				.append("avconv -loop 1 -shortest -y -i "
						+ MainTextEditor.getInstance().hiddenDir + "/out1.png -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/result1.mp4");
				finalBothCommand.append(";");
				// add text for title screen
				finalBothCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().titleFontName
						+ "':textfile='"
						+ fileTitle
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().titleFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().titleFontColour + "\" -t "
						+ MainTextEditor.getInstance().titleDuration + " -y " + MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4");
				finalBothCommand.append(";");
				// add text for credit screen
				finalBothCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/result1.mp4 -strict experimental -vf \"drawtext=fontfile='"
						+ MainTextEditor.getInstance().fontDir
						+ MainTextEditor.getInstance().creditFontName
						+ "':textfile='"
						+ fileCredit
						+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
						+ MainTextEditor.getInstance().creditFontSize + ":fontcolor="
						+ MainTextEditor.getInstance().creditFontColour + "\" -t "
						+ MainTextEditor.getInstance().creditDuration + " -y " +MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4");
				finalBothCommand.append(";");

				// concatenate the videos together = gives the final
				// output
				finalBothCommand
				.append("avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file3.ts ; avconv -ss 0 -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file5.ts; avconv -ss 0 -i "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file6.ts ; avconv -i concat:\""
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file3.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file5.ts|"
						+ MainTextEditor.getInstance().hiddenDir
						+ "/file6.ts\" -c copy -bsf:a aac_adtstoasc -y "
						+ lastOutput);
				finalBothCommand.append(";");

				MainTextEditor.getInstance().bothTitleAndCreditCommand = finalBothCommand.toString();

				return MainTextEditor.getInstance().bothTitleAndCreditCommand;
			}
		}

	}


}