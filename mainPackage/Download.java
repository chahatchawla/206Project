package mainPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 * SoftEng206 Project -download class
 * 
 * Purpose: The purpose of this class is to download a mp3 or a mp4 file from a
 * URL. This class is used in MainAudioMainpulator.java.
 * 
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 *         Reference: this download is based on the code in assignment 3 of @author
 *         zall747
 */

public class Download extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Initializing the text for the buttons
	private final String TEXT_DOWNLOAD = "Download";
	private final String TEXT_CANCEL = "Cancel";

	// Initializing the buttons
	private JButton downloadBt = new JButton(TEXT_DOWNLOAD);
	private JButton cancelBt = new JButton(TEXT_CANCEL);

	// Initializing the labels
	private JLabel downloadLabel = new JLabel("Download");
	private JLabel label = new JLabel("Please enter a url: ");

	// Initializing the textFields
	private JTextField url = new JTextField();

	// Initializing the progressBar
	private final JProgressBar progressBar = new JProgressBar(0, 100);
	private String percentageCompleted;

	// Initializing the separators
	private JLabel separator = new JLabel("");
	private JLabel separator2 = new JLabel("");

	// Initializing the fonts
	private Font defaultFont = new Font("TimesRoman", Font.PLAIN, 12);
	private Font btFont = new Font("TimesRoman", Font.BOLD, 12);

	private DownloadTask download;
	private File chosenFile;

	private String projectPath;
	private String workingDir;

	/**
	 * Constructor for Download() -Sets up the GUI for the download frame - sets
	 * up the default layout
	 * 
	 * @param hiddenDir
	 * @param workingDir
	 * @param hiddenDir2
	 */
	public Download(String projectPath, String workingDir) {

		// Store the directories
		this.workingDir = workingDir;
		this.projectPath = projectPath;

		// Set the download label
		downloadLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		add(downloadLabel);

		// Add blank space
		separator.setPreferredSize(new Dimension(500, 20));
		add(separator);

		// Create the user input area
		label.setFont(defaultFont);
		add(label);

		url.setPreferredSize(new Dimension(300, 25));
		add(url);

		// Create the progress bar
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(420, 20));
		progressBar.setValue(0);
		add(progressBar);

		// Add blank space
		separator2.setPreferredSize(new Dimension(500, 1));
		add(separator2);

		// Create the buttons
		downloadBt.addActionListener(this);
		downloadBt.setFont(btFont);
		downloadBt.setPreferredSize(new Dimension(150, 25));
		add(downloadBt);

		cancelBt.addActionListener(this);
		cancelBt.setFont(btFont);
		cancelBt.setPreferredSize(new Dimension(150, 25));
		cancelBt.setEnabled(false);
		add(cancelBt);

	}

	/**
	 * DownloadTask do the downloading in a background thread
	 */
	class DownloadTask extends SwingWorker<String, String> {

		private String url;
		private String chosenDir;

		// Get the url and the destination directory from ED thread
		public DownloadTask(String url, String chosenDir) {
			this.url = url;
			this.chosenDir = chosenDir;
		}

		private Process process;

		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected String doInBackground() throws Exception {

			try {

				// Download from the given url to the specified directory
				ProcessBuilder builder = new ProcessBuilder("wget",
						"--progress=bar:force", "-c", url, "-P", chosenDir);
				process = builder.start();

				// send the intermediate results (to update progress bar)
				InputStream error = process.getErrorStream();
				BufferedReader stderr = new BufferedReader(
						new InputStreamReader(error));

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

			return null;
		}

		/**
		 * process updates the progress bar during the longTask
		 */
		@Override
		protected void process(List<String> chunks) {
			if (!isCancelled()) {
				for (String s : chunks) {
					Pattern p = Pattern.compile("(\\d*)\\%");
					Matcher m = p.matcher(s);

					while (m.find()) {
						percentageCompleted = m.group(1);
						progressBar.setValue(Integer
								.parseInt(percentageCompleted));
					}

				}
			}
		}

		@Override
		protected void done() {

			// Check if the download process completed successfully or not
			if (!isCancelled()) {
				if (process.exitValue() == 0) {
					JOptionPane.showMessageDialog(null, "Download Successful!");

					// Disable importing another media and enable editing

					File file = new File(chosenFile.toString());
					Path path = file.toPath();
					String type = "";
					try {
						type = Files.probeContentType(path);
					} catch (IOException f) {
						f.printStackTrace();
					}

					if (type.equals("audio/mpeg")) { // If the file is an audio
														// only, enable
														// extracting only
						Main.tabbedPane.setEnabled(true);
						Main.apf.enableExtractOnly();
						Main.tpf.enableTextEdit(false);
						Main.vpf.enableVideoMan(false);
						Main.spf.enableSubtitle(false);

					} else { // 2> If it is a video enable all the editing
								// options
						Main.tabbedPane.setEnabled(true);
						Main.vpf.enableVideoMan(true);
						Main.apf.enableAudioMan(true);
						Main.tpf.enableTextEdit(true);
						Main.spf.enableSubtitle(true);
					}

					Menu.getInstance().submenu.setEnabled(false);
					Menu.getInstance().export.setEnabled(true);

					/*
					 * Store the name, directory and length of the input video
					 * in a txt file
					 */

					try {
						// Open the project file
						File f = new File(projectPath);
						FileWriter fw = new FileWriter(f, true);
						BufferedWriter bw = new BufferedWriter(fw);

						// Store the video info in the txt file
						File videoFile = new File(chosenFile.toString());
						bw.write(videoFile.getPath());
						bw.newLine();

						// Get the length of the video
						String cmd = "avprobe -loglevel error -show_streams "
								+ videoFile.getPath()
								+ " | grep -i duration | cut -f2 -d=";

						ProcessBuilder builder = new ProcessBuilder(
								"/bin/bash", "-c", cmd);
						builder.redirectErrorStream(true);
						Process process = builder.start();

						// read the output of the process
						InputStream outStr = process.getInputStream();
						BufferedReader stdout = new BufferedReader(
								new InputStreamReader(outStr));

						// Write the length of the input video
						bw.write(stdout.readLine());
						bw.newLine();
						bw.close();

						Menu.getInstance().finishDownload();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// Generate error messages
				} else if (process.exitValue() == 1) {
					JOptionPane.showMessageDialog(null, "Generic error code!");

				} else if (process.exitValue() == 2) {
					JOptionPane.showMessageDialog(null, "Parse error occured!");

				} else if (process.exitValue() == 3) {
					JOptionPane.showMessageDialog(null,
							"File input/output error occured!");

				} else if (process.exitValue() == 4) {
					JOptionPane.showMessageDialog(null,
							"Error: Network failure!");

				} else if (process.exitValue() == 5) {
					JOptionPane
							.showMessageDialog(null,
									"Error: Secure Sockets Layer (SSL) verification failure!");

				} else if (process.exitValue() == 6) {
					JOptionPane.showMessageDialog(null,
							"Error: Username/password authentication failure!");

				} else if (process.exitValue() == 7) {
					JOptionPane.showMessageDialog(null,
							"Error: Protocolerrors occured!");

				} else if (process.exitValue() == 8) {
					JOptionPane.showMessageDialog(null,
							"Sorry, server issued an error response!");
				} else {
					JOptionPane.showMessageDialog(null,
							"Sorry, error encountered!");
				}

			} else {
				process.destroy();
				JOptionPane
						.showMessageDialog(null, "Downloading was cancelled");
			}

			// Go back to initial state
			progressBar.setValue(0);
			downloadBt.setEnabled(true);
		}

	}

	public void actionPerformed(ActionEvent e) {

		// If the download button is clicked
		if (e.getSource() == downloadBt) {

			downloadBt.setEnabled(false);
			String inputURL = url.getText();

			// Check a url was inserted
			if (inputURL.length() == 0) {
				JOptionPane.showMessageDialog(null,
						"Please enter a url to download from");
				downloadBt.setEnabled(true);
				return;
			}

			// Get the file name from the url
			File f = new File(inputURL);
			String fileName = f.getName();

			// Check if the file exists locally
			chosenFile = new File(workingDir + "/" + fileName);
			if (chosenFile.exists()) {

				// Allow user to choose either resuming the download or
				// overwriting the current file
				Object[] existOptions = { "Proceed", "Overwrite" };
				int optionChosen = JOptionPane
						.showOptionDialog(
								null,
								"File already exists in your project directory. "
										+ "Do you want to proceed downloading or overwrite the existing file?",
								"File Exists!", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null,
								existOptions, existOptions[0]);

				if (optionChosen == 1) {
					chosenFile.delete(); // Delete the existing file
				} else if (optionChosen == -1) {
					downloadBt.setEnabled(true);
					return;
				}
			}

			// Check a url is open source
			Object[] OSoptions = { "Yes, it is.", "No, it is not" };
			int optionChosen = JOptionPane
					.showOptionDialog(null,
							"Please confirm that the file is open source.",
							"Confirm open source!", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, OSoptions,
							OSoptions[0]);
			if (optionChosen == 1 || optionChosen == -1) {
				JOptionPane.showMessageDialog(null,
						"Sorry, file must be an open source");
				downloadBt.setEnabled(true);
				url.setText("");
				return;
			}

			// Start downloading in background thread
			download = new DownloadTask(inputURL, workingDir);
			download.execute();
			cancelBt.setEnabled(true);
			url.setText("");

		}
		// If the cancel button is clicked
		else {
			// Cancel the download process
			if (download != null) {
				download.cancel(false);
			}
		}

	}

	/**
	 * Gets the full path of the obtained video
	 */
	public String getChosenFile() {
		if (chosenFile != null) {
			return chosenFile.toString();
		} else {
			return "No file yet";
		}
	}
}