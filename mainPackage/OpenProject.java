package mainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - open project class
 * 
 * Purpose: The purpose of this class is to perform the open project
 * functionality. It does the required checks before opening project and then if
 * the checks are passed, opens the project chosen by the user. This class is
 * used in mainPackage.Menu.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class OpenProject {

	/**
	 * openProject Method is a method that opens project selected by the user
	 */
	protected void openProject() {

		// Refresh the project
		MediaPlayer.getInstance().stopVideo();
		Main.vpf.refreshVideoMan();
		Main.apf.refreshAudioMan();
		Main.tpf.refreshtextEdit();
		Main.tpf.refreshTitleScreen();
		Main.tpf.refreshCreditScreen();
		Main.spf.refreshSubtitles();

		Main.tabbedPane.setEnabled(false);
		Main.apf.enableAudioMan(false);
		Main.vpf.enableVideoMan(false);
		Main.tpf.enableTextEdit(false);
		Main.spf.enableSubtitle(false);

		// Reference:
		// http://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
		// Prompt the user to choose a project
		JFileChooser chooser = new JFileChooser();

		// Show only correct extension type file by default
		chooser.setFileFilter(Menu.getInstance().projectFilter);

		int returnVal = chooser.showOpenDialog(null);

		// Go back to the main frame if choosing project was cancelled
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		Menu.getInstance().projectPath = chooser.getSelectedFile().toString();
		File f = new File(Menu.getInstance().projectPath);
		try {
			// Read the project file
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			// if the project file selected is not valid
			if (!Menu.getInstance().projectPath.equals(reader.readLine())) {
				JOptionPane.showMessageDialog(null,
						"Project file selected is no valid, please try again.");
				reader.close();
				return;
			}

			// Store the values of the main fields
			Menu.getInstance().hiddenDir = reader.readLine();
			Menu.getInstance().workingDir = reader.readLine();
			Menu.getInstance().inputVideo = reader.readLine();
			reader.close();

			// Check that the opened project contains a valid media
			if (Menu.getInstance().inputVideo == null) {
				JOptionPane
						.showMessageDialog(
								null,
								"Error! no video was imported in this project\nPlease start a new project or open a valid one.");
				return;
			}

			// Check that the video is still in the working directory
			File f1 = new File(Menu.getInstance().inputVideo);
			if (!f1.exists()) {
				JOptionPane
						.showMessageDialog(
								null,
								"Error! The video imported is not found.\n(check that it is in the correct directory)");
				return;
			}

			// enable editing the video and exporting
			Menu.getInstance().submenu.setEnabled(false);
			File file = new File(Menu.getInstance().inputVideo);
			Path path = file.toPath();
			String type = "";
			try {
				type = Files.probeContentType(path);
			} catch (IOException g) {
				g.printStackTrace();
			}

			if (type.equals("audio/mpeg")) { // If the file is an audio only,
												// enable extracting only
				Main.tabbedPane.setEnabled(true);
				Main.apf.enableExtractOnly();
				Main.vpf.enableVideoMan(false);
				Main.tpf.enableTextEdit(false);
				Main.spf.enableSubtitle(false);

			} else { // 2> If it is a video enable all the editing options
				Main.tabbedPane.setEnabled(true);
				Main.vpf.enableVideoMan(true);
				Main.apf.enableAudioMan(true);
				Main.tpf.enableTextEdit(true);
				Main.spf.enableSubtitle(true);

			}

			MediaPlayer.getInstance().playVideo(Menu.getInstance().inputVideo);
			Menu.getInstance().export.setEnabled(true);

			// Set the main project and video info in the editing tabs
			Main.vpf.setVideoInfo();
			Main.apf.setVideoInfo();
			Main.tpf.setVideoInfo();
			Main.spf.setVideoInfo();

			// Check what edits were performed previously
			File f2 = new File(Menu.getInstance().workingDir + "/.videoFields");
			File f3 = new File(Menu.getInstance().workingDir + "/.audioFields");
			File f4 = new File(Menu.getInstance().workingDir + "/.creditFields");
			File f5 = new File(Menu.getInstance().workingDir + "/.titleFields");
			File f6 = new File(Menu.getInstance().workingDir
					+ "/.subtitleFields");

			// If video manipulation was performed
			if (f2.exists()) {
				Main.vpf.setAllFields(Menu.getInstance().workingDir
						+ "/.videoFields");
			}
			// If audio manipulation was performed
			if (f3.exists()) {
				Main.apf.setAllFields(Menu.getInstance().workingDir
						+ "/.audioFields");
			}
			// If subtitles was performed
			if (f6.exists()) {
				Main.spf.setAllFields(Menu.getInstance().workingDir
						+ "/.subtitleFields");
			}

			// If text editing was performed
			if (f4.exists() && !f5.exists()) {
				Main.tpf.setCreditFields(Menu.getInstance().workingDir
						+ "/.creditFields");
			} else if (!f4.exists() && f5.exists()) {
				Main.tpf.setTitleFields(Menu.getInstance().workingDir
						+ "/.titleFields");
			} else {
				Main.tpf.setTitleFields(Menu.getInstance().workingDir
						+ "/.titleFields");
				Main.tpf.setCreditFields(Menu.getInstance().workingDir
						+ "/.creditFields");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
