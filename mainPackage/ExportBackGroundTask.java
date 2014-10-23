package mainPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
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
 * Background Task class extends SwingWorker and handles all the long tasks.
 */
public class ExportBackGroundTask extends SwingWorker<Integer, String> {
	private String cmd = "";
	private Process process;
	private ProcessBuilder builder;

	private final JProgressBar progressBar = new JProgressBar(0, 100);
;

	//Set the export frame 
	private JFrame exportFrame = new JFrame("Exporting...");
	private JPanel exportPanel = new JPanel();

	private JLabel exportLabel = new JLabel("Export is in progress....");
	private JLabel separator= new JLabel("");

	protected ExportBackGroundTask(String cmd) {
		this.cmd = cmd;
		
		exportLabel.setFont(new Font("TimesRoman", Font.BOLD, 12));
		//Create the progress bar
		progressBar.setPreferredSize(new Dimension(220, 20));
		separator.setPreferredSize(new Dimension(220, 5));
		progressBar.setValue(0);
		exportPanel.add(separator);
		exportPanel.add(exportLabel);
		exportPanel.add(progressBar);
		exportFrame.setContentPane(exportPanel);
		exportFrame.setLocation(700, 400);
		exportFrame.setResizable(false);
		exportFrame.setSize(250,100);
		exportFrame.setVisible(true);


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

			progressBar.setIndeterminate(true);



		}
	}


	protected void done(){


		if (process.exitValue() == 0){
			progressBar.setValue(0);
			exportFrame.dispose();
			Object[] existOptions = {"Yes, Please","No, Thankyou"};
			int optionChosen = JOptionPane.showOptionDialog(null,
					"Export was successful! Would you like to preview the exported video?",
					"Export", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, existOptions,
					existOptions[0]);
			
			if (optionChosen == 0){
				PrevExportBackgroundTask longTask = new PrevExportBackgroundTask();
				longTask.execute();
				
			}

		}
		else {
			progressBar.setValue(0);
			exportFrame.dispose();
			JOptionPane.showMessageDialog(null, "Error encountered. Exporting is aborted!");

		}
		

	}

}