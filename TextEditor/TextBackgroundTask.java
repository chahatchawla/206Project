package TextEditor;

import java.io.File;

import javax.swing.SwingWorker;
/**
 * Background Task class extends SwingWorker and handles all the long tasks.
 */
public class TextBackgroundTask extends SwingWorker<Integer, String> {
		String firstInput = "";
		String lastOutput = "";
		Process process;
		ProcessBuilder builder;

		protected TextBackgroundTask(String input, String output) {
			firstInput = input;
			lastOutput = output;
		}

		// Override doInBackground() to execute longTask in the background
		@Override
		protected Integer doInBackground() throws Exception {
			try {
				// Get the video path and length
				TextEditor.getInstance().tpf.setVideoInfo();
				/*
				 * Reference for all the avconv commands
				 * https://libav.org/avconv.html and a combination of many
				 * searches found on google, final command selected after a lot
				 * of trials and testing
				 */

				// create text files for title screen and credit screen for the
				// purposes of checking which screen the user wants to implement
				File fileTitle = new File(TextEditor.getInstance().workingDir + "/.TitleText.txt");
				File fileCredit = new File(TextEditor.getInstance().workingDir + "/.CreditText.txt");

				// if the user wants to overlay
				if (TextEditor.getInstance().backgroundImageOption == 0) {

					// if the user has saved title screen settings but does not
					// want to implement credit screen
					if (fileTitle.exists() && !fileCredit.exists()) {
						StringBuilder finalTitleCommand = new StringBuilder();
						// add text to video
						finalTitleCommand
						.append("avconv -ss 0 -i "
								+ firstInput
								+ " -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().titleFontName
								+ "':textfile='"
								+ fileTitle
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().titleFontSize + ":fontcolor="
								+ TextEditor.getInstance().titleFontColour + "\" -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text.mp4");
						finalTitleCommand.append(";");
						// concatenate the videos together = gives the final
						// output
						finalTitleCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts ; avconv -ss "
								+ TextEditor.getInstance().titleDuration
								+ " -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalTitleCommand.append(";");
						TextEditor.getInstance().titleCommand = finalTitleCommand.toString();
						
						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().titleCommand);
					}

					// if the user has saved credit screen settings but does not
					// want to implement title screen
					else if (!fileTitle.exists() && fileCredit.exists()) {
						StringBuilder finalCreditCommand = new StringBuilder();
						// calculates the start time for credit screen
						int time = (int) (Double.parseDouble(TextEditor.getInstance().videoLength) - Integer
								.parseInt(TextEditor.getInstance().creditDuration));
						String startTime = "" + time;
						// add text to video
						finalCreditCommand
						.append("avconv -ss "
								+ startTime
								+ " -i "
								+ firstInput
								+ " -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().creditFontName
								+ "':textfile='"
								+ fileCredit
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().creditFontSize + ":fontcolor="
								+ TextEditor.getInstance().creditFontColour + "\" -t "
								+ TextEditor.getInstance().creditDuration + " -y " + TextEditor.getInstance().hiddenDir
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
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts ; avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalCreditCommand.append(";");
						TextEditor.getInstance().creditCommand = finalCreditCommand.toString();
						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().creditCommand);
					}

					// if the user has saved settings for both title screen and
					// credit screen - wants to implement both
					else {
						StringBuilder finalBothCommand = new StringBuilder();
						
						// calculates the start time and the stop times
						int time = (int) (Double.parseDouble(TextEditor.getInstance().videoLength) - Integer
								.parseInt(TextEditor.getInstance().creditDuration));
						int time1 = time - Integer.parseInt(TextEditor.getInstance().titleDuration);
						String startTime = "" + time;
						String stopTime = "" + time1;
						// add text to title video
						finalBothCommand
						.append("avconv -ss 0 -i "
								+ firstInput
								+ " -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().titleFontName
								+ "':textfile='"
								+ fileTitle
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().titleFontSize + ":fontcolor="
								+ TextEditor.getInstance().titleFontColour + "\" -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text.mp4");
						finalBothCommand.append(";");
						// add text to credit video
						finalBothCommand
						.append("avconv -ss "
								+ startTime
								+ " -i "
								+ firstInput
								+ " -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().creditFontName
								+ "':textfile='"
								+ fileCredit
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().creditFontSize + ":fontcolor="
								+ TextEditor.getInstance().creditFontColour + "\" -t "
								+ TextEditor.getInstance().creditDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text1.mp4");
						finalBothCommand.append(";");
						// concatenate the videos together = gives the final
						// output
						finalBothCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file3.ts ; avconv -ss "
								+ TextEditor.getInstance().titleDuration
								+ " -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y -t "
								+ stopTime
								+ " "
								+ TextEditor.getInstance().hiddenDir
								+ "/file5.ts; avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file6.ts ; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file3.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file5.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file6.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalBothCommand.append(";");
						TextEditor.getInstance().bothTitleAndCreditCommand = finalBothCommand.toString();

						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().bothTitleAndCreditCommand);
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
						if (TextEditor.getInstance().backgroundImageOption == 1) {
							// screenshot will be taken at 00:00:01
							inputFrameTime = "00:00:01";
						}
						// if they select a frame from the video
						else {
							// screenshot will be taken at the time specified by
							// the user
							inputFrameTime = TextEditor.getInstance().titleFrameTime;
						}
						// take a screenshot from the video at the given
						// inputFrametime
						finalTitleCommand.append("avconv -i " + firstInput
								+ " -ss " + inputFrameTime
								+ " -f image2 -vframes 1 " + TextEditor.getInstance().hiddenDir
								+ "/out.png");
						finalTitleCommand.append(";");
						// create a video from image for the duration given by
						// the user
						finalTitleCommand
						.append("avconv -loop 1 -shortest -y -i "
								+ TextEditor.getInstance().hiddenDir + "/out.png -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/result.mp4");
						finalTitleCommand.append(";");
						// add text
						finalTitleCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().titleFontName
								+ "':textfile='"
								+ fileTitle
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().titleFontSize + ":fontcolor="
								+ TextEditor.getInstance().titleFontColour + "\" -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text.mp4");
						finalTitleCommand.append(";");

						// concatenate the videos together = gives the final
						// output
						finalTitleCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts ; avconv -ss 0"
								+ " -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalTitleCommand.append(";");

						TextEditor.getInstance().titleCommand = finalTitleCommand.toString();
						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().titleCommand);
					}
					// if the user has saved credit screen settings but does not
					// want to implement title screen
					else if (!fileTitle.exists() && fileCredit.exists()) {
						StringBuilder finalCreditCommand = new StringBuilder();
						// if they choose to use a defaultFrame
						if (TextEditor.getInstance().backgroundImageOption == 1) {
							// screenshot will be taken at 00:00:01
							inputFrameTime = "00:00:01";
						}
						// if they select a frame from the video
						else {
							// screenshot will be taken at the time specified by
							// the user
							inputFrameTime = TextEditor.getInstance().creditFrameTime;
						}
						// take a screenshot from the video at the given
						// inputFrametime
						finalCreditCommand.append("avconv -i " + firstInput
								+ " -ss " + inputFrameTime
								+ " -f image2 -vframes 1 " + TextEditor.getInstance().hiddenDir
								+ "/out.png");
						finalCreditCommand.append(";");
						// create a video from image for the duration given by
						// the user
						finalCreditCommand
						.append("avconv -loop 1 -shortest -y -i "
								+ TextEditor.getInstance().hiddenDir + "/out.png -t "
								+ TextEditor.getInstance().creditDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/result.mp4");
						finalCreditCommand.append(";");
						// add text
						finalCreditCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().creditFontName
								+ "':textfile='"
								+ fileCredit
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().creditFontSize + ":fontcolor="
								+ TextEditor.getInstance().creditFontColour + "\" -t "
								+ TextEditor.getInstance().creditDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text1.mp4");
						finalCreditCommand.append(";");
						// concatenate the videos together = gives the final
						// output
						finalCreditCommand
						.append("avconv -ss 0 -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts ; avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file1.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file2.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalCreditCommand.append(";");
						TextEditor.getInstance().creditCommand = finalCreditCommand.toString();
						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().creditCommand);
					}

					// if the user has saved settings for both title screen and
					// credit screen - wants to implement both
					else {
						StringBuilder finalBothCommand = new StringBuilder();
						// if they choose to use a defaultFrame
						if (TextEditor.getInstance().backgroundImageOption == 1) {
							// screenshot will be taken at 00:00:01 for both
							// title screen and credit screen
							inputFrameTime = "00:00:01";
							inputFrameTime1 = "00:00:01";
						}
						// if they select a frame from the video
						else {
							// screenshots will be taken at the time specified
							// by the user
							inputFrameTime = TextEditor.getInstance().titleFrameTime;
							inputFrameTime1 = TextEditor.getInstance().creditFrameTime;
						}
						// take a screenshot from the video at the given
						// inputFrametime for title screen
						finalBothCommand.append("avconv -i " + firstInput
								+ " -ss " + inputFrameTime
								+ " -f image2 -vframes 1 " + TextEditor.getInstance().hiddenDir
								+ "/out.png");
						finalBothCommand.append(";");
						// take a screenshot from the video at the given
						// inputFrametime for credit screen
						finalBothCommand.append("avconv -i " + firstInput
								+ " -ss " + inputFrameTime1
								+ " -f image2 -vframes 1 " + TextEditor.getInstance().hiddenDir
								+ "/out1.png");
						finalBothCommand.append(";");
						// create video from image for the duration given by the
						// user for title screen
						finalBothCommand
						.append("avconv -loop 1 -shortest -y -i "
								+ TextEditor.getInstance().hiddenDir + "/out.png -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/result.mp4");
						finalBothCommand.append(";");
						// create video from image for the duration given by the
						// user for credit screen
						finalBothCommand
						.append("avconv -loop 1 -shortest -y -i "
								+ TextEditor.getInstance().hiddenDir + "/out1.png -t "
								+ TextEditor.getInstance().creditDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/result1.mp4");
						finalBothCommand.append(";");
						// add text for title screen
						finalBothCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/result.mp4 -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().titleFontName
								+ "':textfile='"
								+ fileTitle
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().titleFontSize + ":fontcolor="
								+ TextEditor.getInstance().titleFontColour + "\" -t "
								+ TextEditor.getInstance().titleDuration + " -y " + TextEditor.getInstance().hiddenDir
								+ "/text.mp4");
						finalBothCommand.append(";");
						// add text for credit screen
						finalBothCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/result1.mp4 -strict experimental -vf \"drawtext=fontfile='"
								+ TextEditor.getInstance().fontDir
								+ TextEditor.getInstance().creditFontName
								+ "':textfile='"
								+ fileCredit
								+ "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize="
								+ TextEditor.getInstance().creditFontSize + ":fontcolor="
								+ TextEditor.getInstance().creditFontColour + "\" -t "
								+ TextEditor.getInstance().creditDuration + " -y " +TextEditor.getInstance().hiddenDir
								+ "/text1.mp4");
						finalBothCommand.append(";");

						// concatenate the videos together = gives the final
						// output
						finalBothCommand
						.append("avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file3.ts ; avconv -ss 0 -i "
								+ firstInput
								+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file5.ts; avconv -ss 0 -i "
								+ TextEditor.getInstance().hiddenDir
								+ "/text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "
								+ TextEditor.getInstance().hiddenDir
								+ "/file6.ts ; avconv -i concat:\""
								+ TextEditor.getInstance().hiddenDir
								+ "/file3.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file5.ts|"
								+ TextEditor.getInstance().hiddenDir
								+ "/file6.ts\" -c copy -bsf:a aac_adtstoasc -y "
								+ lastOutput);
						finalBothCommand.append(";");

						TextEditor.getInstance().bothTitleAndCreditCommand = finalBothCommand.toString();

						builder = new ProcessBuilder("/bin/bash", "-c",
								TextEditor.getInstance().bothTitleAndCreditCommand);
					}
				}

				process = builder.start();
				process.waitFor();
				return process.exitValue();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return 1;
		}
	
}