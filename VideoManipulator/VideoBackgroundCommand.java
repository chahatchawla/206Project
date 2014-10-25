package VideoManipulator;

public class VideoBackgroundCommand{
	
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
			if (MainVideoManipulator.getInstance().filterEnable) {

				StringBuilder bigFilterCmd = new StringBuilder();

				// if the filter is negate
				if (MainVideoManipulator.getInstance().filter.equals("Negate")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"negate=5\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is blur
				else if (MainVideoManipulator.getInstance().filter.equals("Blur")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf \"boxblur=2:1:0:0:0:0\" -codec:a copy -y "
							+ lastOutput);

				}
				// if the filter is horizontal flip
				else if (MainVideoManipulator.getInstance().filter.equals("Horizontal Flip")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"hflip\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is vertical flip
				else if (MainVideoManipulator.getInstance().filter.equals("Vertical Flip")) {

					bigFilterCmd.append("avconv -i " + firstInput
							+ " -vf \"vflip\" -strict experimental -y "
							+ lastOutput);

				}
				// if the filter is fade in
				else if (MainVideoManipulator.getInstance().filter.equals("Fade In")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf fade=in:00:30 -strict experimental -codec:v libx264 -y "
							+ lastOutput);

				}
				// if the filter is transpose
				else if (MainVideoManipulator.getInstance().filter.equals("Transpose")) {

					bigFilterCmd
					.append("avconv -i "
							+ firstInput
							+ " -vf transpose=dir=clock_flip -strict experimental -codec:v libx264 -y "
							+ lastOutput);

				}
				bigFilterCmd.append(";");
				MainVideoManipulator.getInstance().filterCmd = bigFilterCmd.toString();
			}

			// if snapshot is enabled, constructs the command for snapshot
			if (MainVideoManipulator.getInstance().snapshotEnable) {

				StringBuilder bigsSnapshotCmd = new StringBuilder();

				// take a screen shot
				bigsSnapshotCmd.append("avconv -i " + firstInput + " -ss "
						+ MainVideoManipulator.getInstance().timeSnapshot.getText()
						+ " -f image2 -vframes 1 -y " + MainVideoManipulator.getInstance().workingDir + "/"
						+ MainVideoManipulator.getInstance().outputSnapshotName.getText() + ".png");
				bigsSnapshotCmd.append(";");
				MainVideoManipulator.getInstance().snapshotCmd = bigsSnapshotCmd.toString();

				if (!MainVideoManipulator.getInstance().filterEnable){

					bigsSnapshotCmd.append("avconv -i ");
					bigsSnapshotCmd.append(firstInput);
					bigsSnapshotCmd.append(" -strict experimental -y ");
					bigsSnapshotCmd.append(lastOutput);
					bigsSnapshotCmd.append(";");

				}

			}

			// if loopVideo is enabled, constructs the command for making a
			// loop video of a chosen frame
			if (MainVideoManipulator.getInstance().loopVideoEnable) {

				StringBuilder bigLoopVideoCmd = new StringBuilder();



				int loopNumber = Integer.parseInt(MainVideoManipulator.getInstance().loop.getText());

				StringBuilder loop = new StringBuilder();
				for (int i = 0; i < loopNumber; i++) {

					if (loopNumber - 1 == i) {
						loop.append(MainVideoManipulator.getInstance().hiddenDir + "/file1.ts\"");

					} else {
						loop.append(MainVideoManipulator.getInstance().hiddenDir + "/file1.ts");
						loop.append("|");
					}
				}
				String loopString = loop.toString();

				// extract the video and make a .ts file for it
				bigLoopVideoCmd
				.append("avconv -ss "
						+ MainVideoManipulator.getInstance().timeStart.getText()
						+ " -i "
						+ firstInput
						+ " -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t "
						+ MainVideoManipulator.getInstance().timeLength.getText() + " -y " + MainVideoManipulator.getInstance().hiddenDir
						+ "/file1.ts");
				bigLoopVideoCmd.append(";");

				// create a loop video from the .ts file
				bigLoopVideoCmd.append("avconv -i concat:\"");
				bigLoopVideoCmd.append(loopString);
				bigLoopVideoCmd.append(" -c copy -bsf:a aac_adtstoasc -y "
						+ MainVideoManipulator.getInstance().workingDir + "/" + MainVideoManipulator.getInstance().outputLoopVideoName.getText()
						+ ".mp4");
				bigLoopVideoCmd.append(";");

				if (!MainVideoManipulator.getInstance().filterEnable){

					bigLoopVideoCmd.append("avconv -i ");
					bigLoopVideoCmd.append(firstInput);
					bigLoopVideoCmd.append(" -strict experimental -y ");
					bigLoopVideoCmd.append(lastOutput);
					bigLoopVideoCmd.append(";");

				}


				MainVideoManipulator.getInstance().loopVideoCmd = bigLoopVideoCmd.toString();
			}

			// construct the final command for video manipulation
			StringBuilder finalCommand = new StringBuilder();

			if (MainVideoManipulator.getInstance().snapshotEnable) {
				finalCommand.append(MainVideoManipulator.getInstance().snapshotCmd);
			}
			if (MainVideoManipulator.getInstance().loopVideoEnable) {
				finalCommand.append(MainVideoManipulator.getInstance().loopVideoCmd);
			}
			if (MainVideoManipulator.getInstance().filterEnable) {
				finalCommand.append(MainVideoManipulator.getInstance().filterCmd);
			}
			// start the builder for the bash command so it is executed
			String cmd = finalCommand.toString();
			System.out.println(cmd);
			return cmd;
			
	}
}

