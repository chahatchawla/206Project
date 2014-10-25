package mainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - import from folder class
 * 
 * Purpose: The purpose of this class is to import a media file that is selected
 * by the user from a folder and perform all the checks required in that
 * procedure. This class is used in MainAudioManipulator.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class ImportFromFolder {

	/**
	 * audioHelp() Method imports a media file from a folder and does the checks
	 * for it
	 */
	protected void importFromFolder() {

		//Reference:
		//http://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
		JFileChooser chooser = new JFileChooser();

		// Show only correct extension type file by default
		chooser.setFileFilter(Menu.getInstance().mediaFilter);
		int returnVal = chooser.showOpenDialog(null);

		// Store the chosen file as an input video
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			Menu.getInstance().inputVideo = chooser.getSelectedFile()
					.toString();

			// Reference to File.probeContentType
			// http://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileTypeDetector.html

			File file = new File(Menu.getInstance().inputVideo);
			Path path = file.toPath();
			String type = "";
			try {
				type = Files.probeContentType(path);
			} catch (IOException f) {
				f.printStackTrace();
			}

			// if the file is NOT an audio or a video or audio file, notify the
			// user and
			// allow them to import again
			if (!(type.equals("audio/mpeg"))
					&& !((type.split("/")[0]).equals("video"))) {
				JOptionPane
						.showMessageDialog(
								null,
								"ERROR: file imported does not"
										+ "refer to a valid audio or video file. Please select a new input file!");
				return;
			} 
			
			// If the file is an audio only, enable extracting only
			else if (type.equals("audio/mpeg")) { 
				Main.tabbedPane.setEnabled(true);
				Main.apf.enableExtractOnly();
				Main.tpf.enableTextEdit(false);
				Main.vpf.enableVideoMan(false);
				Main.spf.enableSubtitle(false);

			} else { // 2> If it is a video enable all the editing options
				Main.tabbedPane.setEnabled(true);
				Main.vpf.enableVideoMan(true);
				Main.apf.enableAudioMan(true);
				Main.tpf.enableTextEdit(true);
				Main.spf.enableSubtitle(true);
			}

			// Enable export, disable submenu
			Menu.getInstance().export.setEnabled(true);
			Menu.getInstance().submenu.setEnabled(false);

		} else { // No media was imported
			return;
		}

		/*
		 * Store the name, directory and length of the input video in a txt file
		 */
		try {
			
			// Reference: http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
			// Reference: https://libav.org/avprobe.html
			
			// Open the video project file
			File f = new File(Menu.getInstance().projectPath);
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);

			// Store the video info in the txt file
			File videoFile = new File(Menu.getInstance().inputVideo);
			bw.write(videoFile.getPath());
			bw.newLine();

			// Get the length of the video
			String cmd = "avprobe -loglevel error -show_streams "
					+ videoFile.getPath() + " | grep -i duration | cut -f2 -d=";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			builder.redirectErrorStream(true);
			Process process = builder.start();

			// read the output of the process
			InputStream outStr = process.getInputStream();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					outStr));

			// Write the length of the input video
			bw.write(stdout.readLine());
			bw.newLine();
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
