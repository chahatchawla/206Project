package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - new project class
 * 
 * Purpose: The purpose of this class is to perform the new project
 * functionality. It does the required checks before creating a new project and
 * then if the checks are passed, creates a new project in the chosen directory
 * of the user. This class is used in mainPackage.Menu.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class NewProject {
	/**
	 * newProject Method is a method that creates a new project in the directory
	 * selected by the user
	 */
	protected void newProject() {

		// Refresh the project
		Menu.getInstance().export.setEnabled(false);

		Main.tabbedPane.setEnabled(false);
		Main.apf.enableAudioMan(false);
		Main.vpf.enableVideoMan(false);
		Main.tpf.enableTextEdit(false);
		Main.spf.enableSubtitle(false);

		Main.vpf.refreshVideoMan();
		Main.apf.refreshAudioMan();
		Main.tpf.refreshtextEdit();
		Main.tpf.refreshTitleScreen();
		Main.tpf.refreshCreditScreen();
		Main.spf.refreshSubtitles();

		// Stop the played video
		MediaPlayer.getInstance().stopVideo();
		Menu.getInstance().inputVideo = null;

		int i = -1;
		while (i < 0) { // Loop until project is created successfully or
						// cancelled
			// Reference:
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

			// Take the project name as an input from the user
			String projectName = JOptionPane.showInputDialog(null,
					"Project name: ", "Create new project",
					JOptionPane.DEFAULT_OPTION);
			if (projectName == null) {
				// Cancel is pressed, go back to main frame
				i++;

			} else if (projectName.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Please enter the project's name!");

			} else {
				/*
				 * Create a matcher to check that the output name has no space
				 * in middle
				 * 
				 * @reference:
				 * http://stackoverflow.com/questions/4067809/how-to-
				 * check-space-in-string
				 */
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(projectName);
				boolean found = matcher.find();

				if (found) {
					JOptionPane.showMessageDialog(null,
							"Sorry, project name cannot contain spaces.");

				} else {
					// Allow the user to choose the project file destination and
					// set it as his working directory
					JFileChooser dirChooser = new JFileChooser();
					dirChooser.setDialogTitle("Choose Working Directory.");
					dirChooser
							.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = dirChooser.showSaveDialog(null);
					// If no directory was selected, don't go back to main frame
					if (returnVal != JFileChooser.APPROVE_OPTION) {
						return;
					}

					// Get the name of the chosen directory
					Menu.getInstance().workingDir = dirChooser
							.getSelectedFile().getPath() + "/" + projectName;
					// Create the working directory
					File workDir = new File(Menu.getInstance().workingDir);
					// workDir.mkdir();

					// Check that the project doesn't exists in the specified
					// directory
					String outputFileName = Menu.getInstance().workingDir + "/"
							+ projectName + ".vamix";
					File f = new File(outputFileName);
					Menu.getInstance().hiddenDir = Menu.getInstance().workingDir
							+ "/." + projectName + ".vamix";
					File hidden = new File(Menu.getInstance().hiddenDir);

					if (workDir.exists()) {

						// Reference:
						// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
						// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html

						// Allow user to choose either overwriting the current
						// project or cancel creating new project
						Object[] existOptions = { "Overwrite", "Cancel" };
						int optionChosen = JOptionPane
								.showOptionDialog(
										null,
										"Project already exists. "
												+ "Do you want to overwrite the existing project?",
										"Project Exists!",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										existOptions, existOptions[0]);

						// if overwrite, delete the existing project and create
						// a new one
						if (optionChosen == 0) {

							String cmd = "rm -r "
									+ Menu.getInstance().workingDir;
							ProcessBuilder builder = new ProcessBuilder(
									"/bin/bash", "-c", cmd);
							@SuppressWarnings("unused")
							Process process;
							try {
								process = builder.start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}

							try {
								// Create the working directory
								workDir.mkdir();
								// Create the main project file
								f.createNewFile();
								// Create the hidden directory that holds all
								// the results of the intermediate processes
								hidden.mkdir();

								// Write the hidden directory and the working
								// directory to the main project file
								FileWriter fw = new FileWriter(f);
								BufferedWriter bw = new BufferedWriter(fw);
								Menu.getInstance().projectPath = f.getPath();
								// Store the project path
								bw.write(Menu.getInstance().projectPath);
								bw.newLine();
								// Store the hidden folder path
								bw.write(hidden.toString());
								bw.newLine();
								// Store the working directory
								bw.write(Menu.getInstance().workingDir);
								bw.newLine();
								bw.close();

								// When project is created successfully, enable
								// importing media
								Menu.getInstance().submenu.setEnabled(true);

							} catch (IOException e1) {
								e1.printStackTrace();
							}

							i++;

						} else { // Cancel creating new project
							return;
						}
					}
					i++; // to exit the while loop

					try {

						workDir.mkdir();
						// Create the main project file
						f.createNewFile();
						// Create the hidden directory that holds all the
						// results of the intermediate processes
						hidden.mkdir();

						// Reference:
						// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html

						// Write the hidden directory and the working directory
						// to the main project file
						FileWriter fw = new FileWriter(f);
						BufferedWriter bw = new BufferedWriter(fw);
						Menu.getInstance().projectPath = f.getPath();
						// Store the project path
						bw.write(Menu.getInstance().projectPath);
						bw.newLine();
						// Store the hidden folder path
						bw.write(hidden.toString());
						bw.newLine();
						// Store the working directory
						bw.write(Menu.getInstance().workingDir);
						bw.newLine();
						bw.close();

						// When project is created successfully, enable
						// importing media
						Menu.getInstance().submenu.setEnabled(true);

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
