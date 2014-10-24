package audioManipulator;

import java.io.IOException;

import javax.swing.SwingWorker;



public class ReplacePreview {
	

	private ReplaceBackgroundTask longTask;
	protected void replacePreview(){

		// Get the video path and length
		MainAudioManipulator.getInstance().apf.setVideoInfo();

		// Run the allChecksReplace() to check whether or not the user
		// inputs
		// are valid
		boolean replaceChecksPassed = MainAudioManipulator.getInstance().ac.allChecksReplace();

		// If all checks are passed, run a bash command avplay to enable the
		// user to preview the audio file they chose
		if (replaceChecksPassed) {
			
			longTask = new ReplaceBackgroundTask();
			longTask.execute();

		}
		
		
		
	}
	
	class ReplaceBackgroundTask  extends SwingWorker<Void, String> {
		StringBuilder cmd = new StringBuilder();
		@Override
		protected Void doInBackground() throws Exception {
			

			String cmd = "avplay -i " + MainAudioManipulator.getInstance().inputFile
					+ " -window_title playChosenAudio -x 400 -y 100";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c",
					cmd);
			Process process;
			try {
				process = builder.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
			
		}
	}
}
