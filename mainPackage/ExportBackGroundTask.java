package mainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import audioManipulator.AudioManipulator;

/**
 * Background Task class extends SwingWorker and handles all the long tasks.
 */
public class ExportBackGroundTask extends SwingWorker<Integer, String> {
	private String cmd = "";
	private Process process;
	private ProcessBuilder builder;

	private final JProgressBar progressBar = new JProgressBar(0, 100);
	private String percentageCompleted;

	//Set the export frame 
	private JFrame exportFrame = new JFrame("Exporting...");
	private JPanel exportPanel = new JPanel(new BorderLayout());

	private JLabel exportLabel = new JLabel("Export is in progress....");
	
	
	protected ExportBackGroundTask(String cmd) {
		this.cmd = cmd;



		exportPanel.add(exportLabel);
		exportPanel.add(progressBar);
		exportFrame.setContentPane(exportPanel);
		exportFrame.setLocation(450, 100);
		exportFrame.setResizable(false);
		exportFrame.setSize(800, 650);
		exportFrame.setVisible(true);
		//Create the progress bar
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(20, 20));
		progressBar.setValue(0);

	}

	// Override doInBackground() to execute longTask in the background
	@Override
	protected Integer doInBackground() throws Exception {
		try {

			builder = new ProcessBuilder("/bin/bash", "-c", 
					cmd);

			process = builder.start();

			//send the intermediate results (to update progress bar)
			InputStream error = process.getErrorStream();
			BufferedReader stderr = new BufferedReader(new InputStreamReader(error));

			String line = null;
			while ((line = stderr.readLine()) != null ) {
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
			for (String s : chunks) {
				Pattern p = Pattern.compile("(\\d*)\\%");
				Matcher m = p.matcher(s);

				while (m.find()) {
					percentageCompleted = m.group(1);
					progressBar.setValue(Integer.parseInt(percentageCompleted));
				}

			}
		}
	}

	protected void done(){


		if (process.exitValue() == 0){
			progressBar.setValue(0);
			exportFrame.dispose();
			JOptionPane.showMessageDialog(null, "Export is Successful!");

		}
		else {
			progressBar.setValue(0);
			exportFrame.dispose();
			JOptionPane.showMessageDialog(null, "Error encountered. Exporting is aborted!");

		}
		//Delete the hidden folder that holds the intermediate outputs
		File f6 = new File(Menu.getInstance().hiddenDir);
		f6.delete();

	}

}