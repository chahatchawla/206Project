package textEditor;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mainPackage.Menu;



public class TextProjectFunctions {

	/**
	 * Method stores the video info from the project file to the private fields
	 */
	public void setVideoInfo() {
		// Get the main project file
		MainTextEditor.getInstance().projectPath = Menu.getInstance().getProjectPath();
		File f = new File(MainTextEditor.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			MainTextEditor.getInstance().hiddenDir = reader.readLine();
			MainTextEditor.getInstance().workingDir = reader.readLine();
			MainTextEditor.getInstance().videoPath = reader.readLine();
			MainTextEditor.getInstance().videoLength = reader.readLine();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}



	/**
	 * Method concatenates the selected fontType, fontStyle to a string called
	 * fontName and initializes all the font variables for title screen
	 */
	public void setTitleFontSettings() {
		StringBuilder font = new StringBuilder();
		// fontType options
		if (MainTextEditor.getInstance().fontType == 0) {
			font.append("Arial");
		} else if (MainTextEditor.getInstance().fontType == 1) {
			font.append("Courier_New");
		} else if (MainTextEditor.getInstance().fontType == 2) {
			font.append("Georgia");
		} else if (MainTextEditor.getInstance().fontType == 2) {
			font.append("Times_New_Roman");
		} else {
			font.append("Verdana");
		}
		// fontStyle options
		if (MainTextEditor.getInstance().fontStyle == 1) {
			font.append("_Bold");
		} else if (MainTextEditor.getInstance().fontStyle == 2) {
			font.append("_Italic");
		} else if (MainTextEditor.getInstance().fontStyle == 3) {
			font.append("_Bold_Italic");
		}
		// append .ttf as font types are ttf files
		font.append(".ttf");
		MainTextEditor.getInstance().fontName = font.toString();
		// set font variables for title screen
		MainTextEditor.getInstance().titleFontSize = MainTextEditor.getInstance().fontSize;
		MainTextEditor.getInstance().titleFontColour = MainTextEditor.getInstance().fontColour;
		MainTextEditor.getInstance().titleFontName = MainTextEditor.getInstance().fontName;
		MainTextEditor.getInstance().titleFontStyle = MainTextEditor.getInstance().fontStyle;
		MainTextEditor.getInstance().titlePreviewFont = MainTextEditor.getInstance().prevFont;
	}

	/**
	 * Method concatenates the selected fontType, fontStyle to a string called
	 * fontName and initializes all the font variables for title screen
	 */
	public void setCreditFontSettings() {
		StringBuilder font = new StringBuilder();
		// fontType options
		if (MainTextEditor.getInstance().fontType == 0) {
			font.append("Arial");
		} else if (MainTextEditor.getInstance().fontType == 1) {
			font.append("Courier_New");
		} else if (MainTextEditor.getInstance().fontType == 2) {
			font.append("Georgia");
		} else if (MainTextEditor.getInstance().fontType == 2) {
			font.append("Times_New_Roman");
		} else {
			font.append("Verdana");
		}
		// fontStyle options
		if (MainTextEditor.getInstance().fontStyle == 1) {
			font.append("_Bold");
		} else if (MainTextEditor.getInstance().fontStyle == 2) {
			font.append("_Italic");
		} else if (MainTextEditor.getInstance().fontStyle == 3) {
			font.append("_Bold_Italic");
		}
		// append .ttf as font types are ttf files
		font.append(".ttf");
		MainTextEditor.getInstance().fontName = font.toString();
		// set font variables for credit screen
		MainTextEditor.getInstance().creditFontSize = MainTextEditor.getInstance().fontSize;
		MainTextEditor.getInstance().creditFontColour = MainTextEditor.getInstance().fontColour;
		MainTextEditor.getInstance().creditFontName = MainTextEditor.getInstance().fontName;
		MainTextEditor.getInstance().creditFontStyle = MainTextEditor.getInstance().fontStyle;
		MainTextEditor.getInstance().creditPreviewFont = MainTextEditor.getInstance().prevFont;
	}
	/**
	 * enableTextEdit Method enables or disable all the fields in the text
	 * editing tab depending on the state
	 * 
	 * @param state
	 */

	public void enableTextEdit(boolean state) {

		MainTextEditor.getInstance().textEditorLabel.setEnabled(state);
		MainTextEditor.getInstance().screenLabel.setEnabled(state);
		MainTextEditor.getInstance().durationLabel.setEnabled(state);
		MainTextEditor.getInstance().addTextLabel.setEnabled(state);
		MainTextEditor.getInstance().backgroundImageLabel.setEnabled(state);
		MainTextEditor.getInstance().wordLimitLabel.setEnabled(state);
		MainTextEditor.getInstance().chooseFontLabel.setEnabled(state);
		MainTextEditor.getInstance().chooseFontStyleLabel.setEnabled(state);
		MainTextEditor.getInstance().chooseFontSizeLabel.setEnabled(state);
		MainTextEditor.getInstance().chooseColorLabel.setEnabled(state);
		MainTextEditor.getInstance().addDuration.setEnabled(state);
		MainTextEditor.getInstance().addTextArea.setEnabled(state);

		MainTextEditor.getInstance().overlayCheck.setEnabled(state);
		MainTextEditor.getInstance().screenList.setEnabled(state);
		MainTextEditor.getInstance().fontsList.setEnabled(state);
		MainTextEditor.getInstance().stylesList.setEnabled(state);
		MainTextEditor.getInstance().sizesList.setEnabled(state);
		MainTextEditor.getInstance().coloursList.setEnabled(state);

		MainTextEditor.getInstance().deleteBtn.setEnabled(state);
		MainTextEditor.getInstance().prevBtn.setEnabled(state);
		MainTextEditor.getInstance().saveButton.setEnabled(state);
		MainTextEditor.getInstance().helpButton.setEnabled(state);


	}
	/**
	 * Method sets all the fields of related to the title screen into the
	 * previously saved values
	 * 
	 * @param textFieldsPath
	 *            : the path of the stored text fields
	 */
	public void setTitleFields(String textFieldsPath) {
		MainTextEditor.getInstance().titleFields = textFieldsPath;
		File f = new File(MainTextEditor.getInstance().workingDir + "/.titleFields");
		if (f.exists()) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(f)); // Read the file
				// that saved
				// the fields
				MainTextEditor.getInstance().screenList.setSelectedItem(reader.readLine());
				MainTextEditor.getInstance().titleDuration = reader.readLine();

				MainTextEditor.getInstance().addDuration.setText(MainTextEditor.getInstance().titleDuration);
				MainTextEditor.getInstance().overlayCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (MainTextEditor.getInstance().overlayCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageTitle = 0;

				}
				MainTextEditor.getInstance().defaultCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (MainTextEditor.getInstance().defaultCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageTitle = 1;

				}
				MainTextEditor.getInstance().frameCheck.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (MainTextEditor.getInstance().frameCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageTitle = 2;

				}
				MainTextEditor.getInstance().titleFrameTime = reader.readLine();
				MainTextEditor.getInstance().addTimeFrame.setText(MainTextEditor.getInstance().titleFrameTime);
				MainTextEditor.getInstance().titleFontSize = Integer.parseInt(reader.readLine());
				MainTextEditor.getInstance().sizesList.setSelectedItem("" + MainTextEditor.getInstance().titleFontSize);
				MainTextEditor.getInstance().titleFontColour = reader.readLine();
				MainTextEditor.getInstance().coloursList.setSelectedItem(MainTextEditor.getInstance().titleFontColour);
				switch (MainTextEditor.getInstance().titleFontColour) {
				case "black":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.black);
					break;
				case "green":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.green);
					break;
				case "blue":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.blue);
					break;
				case "yellow":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.yellow);
					break;
				case "red":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.red);
					break;
				case "white":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.white);
					break;
				case "pink":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.pink);
					break;
				}
				MainTextEditor.getInstance().titleFontName = reader.readLine();
				MainTextEditor.getInstance().titleFontStyle = Integer.parseInt(reader.readLine());
				MainTextEditor.getInstance().stylesList.setSelectedIndex(MainTextEditor.getInstance().titleFontStyle);
				MainTextEditor.getInstance().titlePreviewFont = reader.readLine();
				MainTextEditor.getInstance().fontsList.setSelectedItem(MainTextEditor.getInstance().titlePreviewFont);
				String line;
				StringBuilder allText = new StringBuilder();
				while (((line = reader.readLine()) != null)) {
					allText.append(line);
					allText.append(System.getProperty("line.separator"));
				}
				MainTextEditor.getInstance().addTextArea.setText(allText.toString());
				MainTextEditor.getInstance().addTextArea.setFont(new Font(MainTextEditor.getInstance().titlePreviewFont, MainTextEditor.getInstance().titleFontStyle,
						MainTextEditor.getInstance().titleFontSize));
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method sets all the fields of related to the credit screen into the
	 * previously saved values
	 * 
	 * @param textFieldsPath
	 *            : the path of the stored text fields
	 */
	public void setCreditFields(String textFieldsPath) {
		MainTextEditor.getInstance().titleFields = textFieldsPath;
		File f = new File(MainTextEditor.getInstance().workingDir + "/.creditFields");
		if (f.exists()) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(f));
				MainTextEditor.getInstance().screenList.setSelectedItem(reader.readLine()); // Credit Screen
				MainTextEditor.getInstance().creditDuration = reader.readLine();
				MainTextEditor.getInstance().addDuration.setText(MainTextEditor.getInstance().creditDuration);
				MainTextEditor.getInstance().overlayCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (MainTextEditor.getInstance().overlayCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageCredit = 0;

				}
				MainTextEditor.getInstance().defaultCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (MainTextEditor.getInstance().defaultCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageCredit = 1;

				}
				MainTextEditor.getInstance().frameCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (MainTextEditor.getInstance().frameCheck.isSelected()){
					MainTextEditor.getInstance().backgroundImageCredit = 2;

				}
				MainTextEditor.getInstance().creditFrameTime = reader.readLine();
				MainTextEditor.getInstance().addTimeFrame.setText(MainTextEditor.getInstance().creditFrameTime);
				MainTextEditor.getInstance().creditFontSize = Integer.parseInt(reader.readLine());
				MainTextEditor.getInstance().sizesList.setSelectedItem("" + MainTextEditor.getInstance().creditFontSize);
				MainTextEditor.getInstance().creditFontColour = reader.readLine();
				MainTextEditor.getInstance().coloursList.setSelectedItem(MainTextEditor.getInstance().creditFontColour);
				switch (MainTextEditor.getInstance().creditFontColour) {
				case "black":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.black);
					break;
				case "green":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.green);
					break;
				case "blue":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.blue);
					break;
				case "yellow":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.yellow);
					break;
				case "red":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.red);
					break;
				case "white":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.white);
					break;
				case "pink":
					MainTextEditor.getInstance().addTextArea.setForeground(Color.pink);
					break;
				}
				MainTextEditor.getInstance().creditFontName = reader.readLine();
				MainTextEditor.getInstance().creditFontStyle = Integer.parseInt(reader.readLine());
				MainTextEditor.getInstance().stylesList.setSelectedIndex(MainTextEditor.getInstance().creditFontStyle);
				MainTextEditor.getInstance().creditPreviewFont = reader.readLine();
				MainTextEditor.getInstance().fontsList.setSelectedItem(MainTextEditor.getInstance().creditPreviewFont);
				String line;
				StringBuilder allText = new StringBuilder();
				while (((line = reader.readLine()) != null)) {
					allText.append(line);
					allText.append(System.getProperty("line.separator"));
				}
				MainTextEditor.getInstance().addTextArea.setText(allText.toString());
				MainTextEditor.getInstance().addTextArea.setFont(new Font(MainTextEditor.getInstance().creditPreviewFont,
						MainTextEditor.getInstance().creditFontStyle, MainTextEditor.getInstance().creditFontSize));
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method refreshes all the title fields in the text editing tab
	 */
	public void refreshTitleScreen() {
		MainTextEditor.getInstance().addDuration.setText("");
		MainTextEditor.getInstance().addTextArea.setText("");
		MainTextEditor.getInstance().addTimeFrame.setText("hh:mm:ss");
		MainTextEditor.getInstance().overlayCheck.setSelected(true);
		MainTextEditor.getInstance().defaultCheck.setSelected(false);
		MainTextEditor.getInstance().frameCheck.setSelected(false);
		MainTextEditor.getInstance().defaultCheck.setEnabled(false);
		MainTextEditor.getInstance().addTimeFrame.setEnabled(false);
		MainTextEditor.getInstance().frameCheck.setEnabled(false);
		if ((MainTextEditor.getInstance().projectPath != null) && (!MainTextEditor.getInstance().projectPath.isEmpty())) {
			MainTextEditor.getInstance().fontsList.setSelectedIndex(0);
			MainTextEditor.getInstance().stylesList.setSelectedIndex(0);
			MainTextEditor.getInstance().sizesList.setSelectedIndex(6);
			MainTextEditor.getInstance().coloursList.setSelectedIndex(0);
		}
		MainTextEditor.getInstance().backgroundImageOption = 0;
		MainTextEditor.getInstance().titleDuration = "";
		MainTextEditor.getInstance().titleFrameTime = "";
		MainTextEditor.getInstance().fontType = 0;
		MainTextEditor.getInstance().fontStyle = 0;
		MainTextEditor.getInstance().titleFontSize = 30;
		MainTextEditor.getInstance().fontSize = 30;
		MainTextEditor.getInstance().titleFontColour = "black";
		MainTextEditor.getInstance().fontColour = "black";
		MainTextEditor.getInstance().titleFontName = "";
		MainTextEditor.getInstance().fontName = "";
		MainTextEditor.getInstance().titleFontStyle = 0;
		MainTextEditor.getInstance().titlePreviewFont = "Arial";
		MainTextEditor.getInstance().prevFont = "Arial";
	}

	/**
	 * Method changes the enable state of all the field in the text edit tab
	 * except the screen list
	 */
	public void setFieldsEnabled(boolean state) {
		MainTextEditor.getInstance().deleteBtn.setEnabled(state);
		MainTextEditor.getInstance().saveButton.setEnabled(state);
		MainTextEditor.getInstance().helpButton.setEnabled(state);
		MainTextEditor.getInstance().prevBtn.setEnabled(state);

		MainTextEditor.getInstance().addDuration.setEnabled(state);
		MainTextEditor.getInstance().addTextArea.setEnabled(state);
		MainTextEditor.getInstance().addTimeFrame.setEnabled(state);

		if (!state){
			MainTextEditor.getInstance().getTime1Button.setEnabled(state);
		}

		MainTextEditor.getInstance().fontsList.setEnabled(state);
		MainTextEditor.getInstance().stylesList.setEnabled(state);
		MainTextEditor.getInstance().sizesList.setEnabled(state);
		MainTextEditor.getInstance().coloursList.setEnabled(state);

	}

	/**
	 * Method refreshes all the credits fields in the text editing tab
	 */
	public void refreshCreditScreen() {
		MainTextEditor.getInstance().addDuration.setText("");
		MainTextEditor.getInstance().addTextArea.setText("");
		MainTextEditor.getInstance().addTimeFrame.setText("hh:mm:ss");
		MainTextEditor.getInstance().addTimeFrame.setEnabled(false);
		MainTextEditor.getInstance().overlayCheck.setSelected(true);
		MainTextEditor.getInstance().defaultCheck.setSelected(false);
		MainTextEditor.getInstance().frameCheck.setSelected(false);
		MainTextEditor.getInstance().defaultCheck.setEnabled(false);
		MainTextEditor.getInstance().frameCheck.setEnabled(false);
		if ((MainTextEditor.getInstance().projectPath != null) && (!MainTextEditor.getInstance().projectPath.isEmpty())) {
			MainTextEditor.getInstance().fontsList.setSelectedIndex(0);
			MainTextEditor.getInstance().stylesList.setSelectedIndex(0);
			MainTextEditor.getInstance().sizesList.setSelectedIndex(6);
			MainTextEditor.getInstance().coloursList.setSelectedIndex(0);
		}
		MainTextEditor.getInstance().backgroundImageOption = 0;
		MainTextEditor.getInstance().creditDuration = "";
		MainTextEditor.getInstance().creditFrameTime = "";
		MainTextEditor.getInstance().fontType = 0;
		MainTextEditor.getInstance().fontStyle = 0;
		MainTextEditor.getInstance().fontSize = 30;
		MainTextEditor.getInstance().creditFontSize = 30;
		MainTextEditor.getInstance().fontColour = "black";
		MainTextEditor.getInstance().creditFontColour = "black";
		MainTextEditor.getInstance().fontName = "";
		MainTextEditor.getInstance().creditFontName = "";
		MainTextEditor.getInstance().creditFontStyle = 0;
		MainTextEditor.getInstance().creditPreviewFont = "Arial";
		MainTextEditor.getInstance().prevFont = "Arial";
	}

	/**
	 * Method refreshes all the project fields in the text editing tab
	 */
	public void refreshtextEdit() {
		if ((MainTextEditor.getInstance().projectPath != null) && (!MainTextEditor.getInstance().projectPath.isEmpty())) {
			MainTextEditor.getInstance().screenList.setSelectedIndex(0);
		}
		MainTextEditor.getInstance().screenType = "";
		MainTextEditor.getInstance().projectPath = "";
		MainTextEditor.getInstance().hiddenDir = "";
		MainTextEditor.getInstance().videoPath = "";
		MainTextEditor.getInstance().videoLength = "";
		MainTextEditor.getInstance().workingDir = "";
		MainTextEditor.getInstance().titleFields = "";
		MainTextEditor.getInstance().creditFields = "";
	}
}
