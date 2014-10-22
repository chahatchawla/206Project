package audioManipulator;

import java.io.IOException;

public class ReplacePreview {
	protected void replacePreview(){

		// Get the video path and length
		AudioManipulator.getInstance().apf.setVideoInfo();

		// Run the allChecksReplace() to check whether or not the user
		// inputs
		// are valid
		boolean replaceChecksPassed = AudioManipulator.getInstance().ac.allChecksReplace();

		// If all checks are passed, run a bash command avplay to enable the
		// user to preview the audio file they chose
		if (replaceChecksPassed) {
			String cmd = "avplay -i " + AudioManipulator.getInstance().inputFile
					+ " -window_title playChosenAudio -x 400 -y 100";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c",
					cmd);
			Process process;
			try {
				process = builder.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		
	}
}
