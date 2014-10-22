package VideoManipulator;

public class VideoBackgroundTask{
	
	private String firstInput;
	private String lastOutput;
	
	protected String makeVideoCommand(String input, String output){
		
		firstInput = input;
		lastOutput = output;

		// Reference for all the avconv commands
		// https://libav.org/avconv.html and a combination of many searches
		// found on google, final command selected after a lot of trials and
		// testing

		
			// if filter is enabled, constructs the command for adding the
			// filter to the input video
			if (VideoManipulator.getInstance().filterEnable) {

				StringBuilder bigFilterCmd = new StringBuilder();

				// if the filter is negate
				if (VideoManipulator.getInstance().filter.equals("Negate")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"negate=5\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is blur
				else if (VideoManipulator.getInstance().filter.equals("Blur")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf \"boxblur=2:1:0:0:0:0\" -codec:a copy -y "
							+ lastOutput);

				}
				// if the filter is horizontal flip
				else if (VideoManipulator.getInstance().filter.equals("Horizontal Flip")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"hflip\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is vertical flip
				else if (VideoManipulator.getInstance().filter.equals("Vertical Flip")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"vflip\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is fade in
				else if (VideoManipulator.getInstance().filter.equals("Fade In")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf fade=in:00:30 -strict experimental -codec:v libx264 -y "
							+ lastOutput);

				}
				// if the filter is transpose
				else if (VideoManipulator.getInstance().filter.equals("Transpose")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf transpose=dir=clock_flip -strict experimental -codec:v libx264 -y "
							+ lastOutput);

				}
				bigFilterCmd.append(";");
				VideoManipulator.getInstance().filterCmd = bigFilterCmd.toString();
			}

			// if snapshot is enabled, constructs the command for snapshot
			if (VideoManipulator.getInstance().snapshotEnable) {

				StringBuilder bigsSnapshotCmd = new StringBuilder();

				// take a screen shot
				bigsSnapshotCmd.append("avconv -i " + firstInput + " -ss "
						+ VideoManipulator.getInstance().timeSnapshot.getText()
						+ " -f image2 -vframes 1 -y " + VideoManipulator.getInstance().workingDir + "/"
						+ VideoManipulator.getInstance().outputSnapshotName.getText() + ".png");
				bigsSnapshotCmd.append(";");
				VideoManipulator.getInstance().snapshotCmd = bigsSnapshotCmd.toString();

				if (!VideoManipulator.getInstance().filterEnable){

					bigsSnapshotCmd.append("avconv -i ");
					bigsSnapshotCmd.append(firstInput);
					bigsSnapshotCmd.append(" -strict experimental -y ");
					bigsSnapshotCmd.append(lastOutput);
					bigsSnapshotCmd.append(";");

				}

			}

			// if loopVideo is enabled, constructs the command for making a
			// loop video of a chosen frame
			if (VideoManipulator.getInstance().loopVideoEnable) {

				StringBuilder bigLoopVideoCmd = new StringBuilder();



				int loopNumber = Integer.parseInt(VideoManipulator.getInstance().loop.getText());

				StringBuilder loop = new StringBuilder();
				for (int i = 0; i < loopNumber; i++) {

					if (loopNumber - 1 == i) {
						loop.append(VideoManipulator.getInstance().hiddenDir + "/file1.ts\"");

					} else {
						loop.append(VideoManipulator.getInstance().hiddenDir + "/file1.ts");
						loop.append("|");
					}
				}
				String loopString = loop.toString();

				// extract the video and make a .ts file for it
				bigLoopVideoCmd
				.append("avconv -ss "
						+ VideoManipulator.getInstance().timeStart.getText()
						+ " -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
						+ VideoManipulator.getInstance().timeLength.getText() + " -y " + VideoManipulator.getInstance().hiddenDir
						+ "/file1.ts");
				bigLoopVideoCmd.append(";");

				// create a loop video from the .ts file
				bigLoopVideoCmd.append("avconv -i concat:\"");
				bigLoopVideoCmd.append(loopString);
				bigLoopVideoCmd.append(" -c copy -bsf:a aac_adtstoasc -y "
						+ VideoManipulator.getInstance().workingDir + "/" + VideoManipulator.getInstance().outputLoopVideoName.getText()
						+ ".mp4");
				bigLoopVideoCmd.append(";");

				if (!VideoManipulator.getInstance().filterEnable){

					bigLoopVideoCmd.append("avconv -i ");
					bigLoopVideoCmd.append(firstInput);
					bigLoopVideoCmd.append(" -strict experimental -y ");
					bigLoopVideoCmd.append(lastOutput);
					bigLoopVideoCmd.append(";");

				}


				VideoManipulator.getInstance().loopVideoCmd = bigLoopVideoCmd.toString();
			}

			// construct the final command for video manipulation
			StringBuilder finalCommand = new StringBuilder();

			if (VideoManipulator.getInstance().snapshotEnable) {
				finalCommand.append(VideoManipulator.getInstance().snapshotCmd);
			}
			if (VideoManipulator.getInstance().loopVideoEnable) {
				finalCommand.append(VideoManipulator.getInstance().loopVideoCmd);
			}
			if (VideoManipulator.getInstance().filterEnable) {
				finalCommand.append(VideoManipulator.getInstance().filterCmd);
			}
			// start the builder for the bash command so it is executed
			String cmd = finalCommand.toString();
			System.out.println(cmd);
			return cmd;
			
	}
}

