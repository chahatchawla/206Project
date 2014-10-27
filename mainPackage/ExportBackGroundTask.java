package mainPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project - export background task class
 * 
 * Purpose: The purpose of this class is to extend SwingWorker and handle all
 * the long tasks to execute the final bash command for all the edits (video
 * manipulation, audio manipulation and text editing) made by the user. This
 * class is used by mainPackage.Export.java
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class ExportBackGroundTask extends SwingWorker<Integer, String> {
	private String cmd = "";
	private Process process;
	private ProcessBuilder builder;

	// Initialize the progress bar
	private final JProgressBar progressBar = new JProgressBar(0, 100);;

	// Set the export frame
	private JFrame exportFrame = new JFrame("Exporting...");
	private JPanel exportPanel = new JPanel();

	private JLabel exportLabel = new JLabel("Export is in progress....");
	private JLabel separator = new JLabel("");

	/**
	 * ExportBackGroundTask Constructor, takes a string as an input which is the
	 * final bash command for export
	 */
	protected ExportBackGroundTask(String cmd) {
		this.cmd = cmd;

		exportLabel.setFont(new Font("TimesRoman", Font.BOLD, 12));

		// Create the progress bar
		progressBar.setPreferredSize(new Dimension(220, 20));
		separator.setPreferredSize(new Dimension(220, 5));
		progressBar.setValue(0);

		// Set the frame settings
		exportPanel.add(separator);
		exportPanel.add(exportLabel);
		exportPanel.add(progressBar);
		exportFrame.setContentPane(exportPanel);
		exportFrame.setLocation(700, 400);
		exportFrame.setResizable(false);
		exportFrame.setSize(250, 100);
		exportFrame.setVisible(true);

	}

	/**
	 * doInBackground() performs all the long tasks so the application does not
	 * freeze
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		try {

			// process builder to execute the bash command
			builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			process = builder.start();

			InputStream error = process.getErrorStream();
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					error));

			String line = null;
			while ((line = stderr.readLine()) != null) {
				if (!isCancelled()) {
					publish(line);
				} else {
					process.destroy();
					return null;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 1;
	}

	/**
	 * process updates the progress bar
	 */
	@Override
	protected void process(List<String> chunks) {
		if (!isCancelled()) {

			progressBar.setIndeterminate(true);

		}
	}

	protected void done() {

		// if the export is successful, ask the user if they want to preview the
		// exported video
		if (process.exitValue() == 0) {
			progressBar.setValue(0);
			exportFrame.dispose();

			// Reference:
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
			Object[] existOptions = { "Yes, Please", "No, Thank You" };
			int optionChosen = JOptionPane
					.showOptionDialog(
							null,
							"Export was successful! Would you like to preview the exported video?",
							"Export", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, existOptions,
							existOptions[0]);

			// execute the preview export background task if the user says yes
			if (optionChosen == 0) {
				PrevExportBackgroundTask longTask = new PrevExportBackgroundTask();
				longTask.execute();

			}

		}
		// if the export is un successful, inform the user that exporting was
		// aborted
		else {
			progressBar.setValue(0);
			exportFrame.dispose();
			JOptionPane.showMessageDialog(null,
					"Error encountered. Exporting is aborted!");

		}

	}

}