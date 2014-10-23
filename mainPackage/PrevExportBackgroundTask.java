package mainPackage;

import javax.swing.SwingWorker;


public class PrevExportBackgroundTask  extends SwingWorker<Void, String> {

	@Override
	protected Void doInBackground() throws Exception {
		StringBuilder cmd = new StringBuilder();


		cmd.append("avplay -i "
				+ Menu.getInstance().workingDir+"/"+Menu.getInstance().outputName+".mp4"			
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


