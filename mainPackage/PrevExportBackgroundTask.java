package mainPackage;

import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - preview export background task class
 * 
 * Purpose: The purpose of this class is to play the exported file (playing the
 * file is done in a swing worker so the application does not freeze. This class
 * is used in mainPackage.ExportBackgroundTask.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 **/
public class PrevExportBackgroundTask extends SwingWorker<Void, String> {

	/**
	 * doInBackground() performs all the long tasks so the application does not
	 * freeze
	 */
	@Override
	protected Void doInBackground() throws Exception {
		StringBuilder cmd = new StringBuilder();

		// Reference: https://libav.org/avplay.html

		cmd.append("avplay -i " + Menu.getInstance().workingDir + "/"
				+ Menu.getInstance().outputName + ".mp4"
				+ " -window_title previewScreen -x 500 -y 350");

		cmd.append(";");

		// run the preview Command for the exportVideo
		Process process;
		ProcessBuilder builder;
		try {

			builder = new ProcessBuilder("/bin/bash", "-c", cmd.toString());
			process = builder.start();
			process.waitFor();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
}
