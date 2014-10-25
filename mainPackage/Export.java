package mainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - export class
 * 
 * Purpose: The purpose of this class is to perform the export functionality. It
 * does the required checks before exporting and then if the checks are passed,
 * it compiles the final export bash command and calls the ExportBackgroundTask
 * class to execute it. The output file is saved in the project directory chosen
 * by the user. This class is used in mainPackage.Menu.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class Export {

	/**
	 * export Method is a method that creates the FINAL bash commands in the
	 * form of a string according to all the options selected by the user
	 */

	protected void export() {
		int i = -1;
		// Loop until the user provides a valid output name or
		// cancels exporting
		while (i < 0) {
			// Take the output file name as an input from the user
			Menu.getInstance().outputName = JOptionPane.showInputDialog(null,
					"Please choose the output name for the final .mp4 file: ",
					"Export project", JOptionPane.DEFAULT_OPTION);

			// Cancel is pressed, go back to main frame
			if (Menu.getInstance().outputName == null) {
				i++;

			}
			// If it is empty
			else if (Menu.getInstance().outputName.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Sorry, output file name cannot be empty!");

			}

			else {
				/*
				 * Create a matcher to check that the output name has no space
				 * in middle Reference:
				 * http://stackoverflow.com/questions/4067809
				 * /how-to-check-space-in-string
				 */
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern
						.matcher(Menu.getInstance().outputName);
				boolean found = matcher.find();

				if (found) {
					// if there are spaces
					JOptionPane.showMessageDialog(null,
							"Sorry, output file name cannot contain spaces.");
				} else {
					// if there are no spaces

					// ensure that the video is not muted
					MediaPlayer.getInstance().video.mute(false);

					// Check that the file doesn't exists in the specified
					// directory
					String outputFileName = Menu.getInstance().workingDir + "/"
							+ Menu.getInstance().outputName + ".mp4";

					// Reference for JOptionPane():
					// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

					File f = new File(outputFileName);
					if (f.exists()) {
						// Allow user to choose either overwriting the existing
						// file or cancel exporting
						Object[] existOptions = { "Overwrite", "Cancel" };
						int optionChosen = JOptionPane
								.showOptionDialog(
										null,
										"File already exists."
												+ "Do you want to overwrite the existing media file?",
										"File Exists!",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										existOptions, existOptions[0]);

						if (optionChosen == 0) { // if overwrite, delete the
													// existing file
							f.delete();
							i++;

						} else { // Cancel exporting project
							return;
						}
					}
					i++; // to exit the while loop

					StringBuilder finalCmd = new StringBuilder();

					// Get the video path
					String videoPath = "";
					File f1 = new File(Menu.getInstance().projectPath);

					try {
						// Read the file and save the necessary variables
						BufferedReader reader;
						reader = new BufferedReader(new FileReader(f1));
						reader.readLine();
						reader.readLine();
						reader.readLine();
						videoPath = reader.readLine();
						reader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// Check what edits were performed previously

					File f2 = new File(Menu.getInstance().workingDir
							+ "/.videoFields");
					File f3 = new File(Menu.getInstance().workingDir
							+ "/.audioFields");
					File f4 = new File(Menu.getInstance().workingDir
							+ "/.creditFields");
					File f5 = new File(Menu.getInstance().workingDir
							+ "/.titleFields");

					// If no edits were done in the project do not export
					if (!f2.exists() && !f3.exists()
							&& !(f4.exists() || f5.exists())) {
						JOptionPane
								.showMessageDialog(null,
										"No changes were saved. Please save the changes before exporting");
						return;
					}
					// User did video manipulation only
					else if (f2.exists() && !f3.exists()
							&& !(f4.exists() || f5.exists())) {
						finalCmd.append(Main.videoMan.makeCommand(videoPath,
								outputFileName));

					}
					// User did audio manipulation only
					else if (!f2.exists() && f3.exists()
							&& !(f4.exists() || f5.exists())) {
						finalCmd.append(Main.audioMan.makeCommand(videoPath,
								outputFileName));

					}
					// User did text editing only
					else if (!f2.exists() && !f3.exists()
							&& (f4.exists() || f5.exists())) {
						finalCmd.append(Main.textEdit.makeCommand(videoPath,
								outputFileName));

					}
					// User did video manipulation and text editing
					else if (f2.exists() && !f3.exists()
							&& (f4.exists() || f5.exists())) {
						finalCmd.append(Main.videoMan.makeCommand(videoPath,
								Menu.getInstance().hiddenDir + "/temp.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(
								Menu.getInstance().hiddenDir + "/temp.mp4",
								outputFileName));
					}

					// User did audio manipulation and text editing
					else if (!f2.exists() && f3.exists()
							&& (f4.exists() || f5.exists())) {
						finalCmd.append(Main.audioMan.makeCommand(videoPath,
								Menu.getInstance().hiddenDir + "/temp.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(
								Menu.getInstance().hiddenDir + "/temp.mp4",
								outputFileName));
					}

					// User did video manipulation and audio manipulation
					else if (f2.exists() && f3.exists()
							&& !(f4.exists() || f5.exists())) {
						finalCmd.append(Main.videoMan.makeCommand(videoPath,
								Menu.getInstance().hiddenDir + "/temp.mp4"));
						finalCmd.append(Main.audioMan.makeCommand(
								Menu.getInstance().hiddenDir + "/temp.mp4",
								outputFileName));
					}
					// User did video manipulation and audio manipulation and
					// text editing
					else {
						finalCmd.append(Main.videoMan.makeCommand(videoPath,
								Menu.getInstance().hiddenDir + "/temp.mp4"));
						finalCmd.append(Main.audioMan.makeCommand(
								Menu.getInstance().hiddenDir + "/temp.mp4",
								Menu.getInstance().hiddenDir + "/temp2.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(
								Menu.getInstance().hiddenDir + "/temp2.mp4",
								outputFileName));
					}

					// Execute the final bash command for export
					ExportBackGroundTask longTask = new ExportBackGroundTask(
							finalCmd.toString());
					longTask.execute();

				}
			}
		}

	}
}
