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
		TextEditor.getInstance().projectPath = Menu.getProjectPath();
		File f = new File(TextEditor.getInstance().projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			TextEditor.getInstance().hiddenDir = reader.readLine();
			TextEditor.getInstance().workingDir = reader.readLine();
			TextEditor.getInstance().videoPath = reader.readLine();
			TextEditor.getInstance().videoLength = reader.readLine();
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
		if (TextEditor.getInstance().fontType == 0) {
			font.append("Arial");
		} else if (TextEditor.getInstance().fontType == 1) {
			font.append("Courier_New");
		} else if (TextEditor.getInstance().fontType == 2) {
			font.append("Georgia");
		} else if (TextEditor.getInstance().fontType == 2) {
			font.append("Times_New_Roman");
		} else {
			font.append("Verdana");
		}
		// fontStyle options
		if (TextEditor.getInstance().fontStyle == 1) {
			font.append("_Bold");
		} else if (TextEditor.getInstance().fontStyle == 2) {
			font.append("_Italic");
		} else if (TextEditor.getInstance().fontStyle == 3) {
			font.append("_Bold_Italic");
		}
		// append .ttf as font types are ttf files
		font.append(".ttf");
		TextEditor.getInstance().fontName = font.toString();
		// set font variables for title screen
		TextEditor.getInstance().titleFontSize = TextEditor.getInstance().fontSize;
		TextEditor.getInstance().titleFontColour = TextEditor.getInstance().fontColour;
		TextEditor.getInstance().titleFontName = TextEditor.getInstance().fontName;
		TextEditor.getInstance().titleFontStyle = TextEditor.getInstance().fontStyle;
		TextEditor.getInstance().titlePreviewFont = TextEditor.getInstance().prevFont;
	}

	/**
	 * Method concatenates the selected fontType, fontStyle to a string called
	 * fontName and initializes all the font variables for title screen
	 */
	public void setCreditFontSettings() {
		StringBuilder font = new StringBuilder();
		// fontType options
		if (TextEditor.getInstance().fontType == 0) {
			font.append("Arial");
		} else if (TextEditor.getInstance().fontType == 1) {
			font.append("Courier_New");
		} else if (TextEditor.getInstance().fontType == 2) {
			font.append("Georgia");
		} else if (TextEditor.getInstance().fontType == 2) {
			font.append("Times_New_Roman");
		} else {
			font.append("Verdana");
		}
		// fontStyle options
		if (TextEditor.getInstance().fontStyle == 1) {
			font.append("_Bold");
		} else if (TextEditor.getInstance().fontStyle == 2) {
			font.append("_Italic");
		} else if (TextEditor.getInstance().fontStyle == 3) {
			font.append("_Bold_Italic");
		}
		// append .ttf as font types are ttf files
		font.append(".ttf");
		TextEditor.getInstance().fontName = font.toString();
		// set font variables for credit screen
		TextEditor.getInstance().creditFontSize = TextEditor.getInstance().fontSize;
		TextEditor.getInstance().creditFontColour = TextEditor.getInstance().fontColour;
		TextEditor.getInstance().creditFontName = TextEditor.getInstance().fontName;
		TextEditor.getInstance().creditFontStyle = TextEditor.getInstance().fontStyle;
		TextEditor.getInstance().creditPreviewFont = TextEditor.getInstance().prevFont;
	}
	/**
	 * enableTextEdit Method enables or disable all the fields in the text
	 * editing tab depending on the state
	 * 
	 * @param state
	 */

	public void enableTextEdit(boolean state) {

		TextEditor.getInstance().textEditorLabel.setEnabled(state);
		TextEditor.getInstance().screenLabel.setEnabled(state);
		TextEditor.getInstance().durationLabel.setEnabled(state);
		TextEditor.getInstance().addTextLabel.setEnabled(state);
		TextEditor.getInstance().backgroundImageLabel.setEnabled(state);
		TextEditor.getInstance().wordLimitLabel.setEnabled(state);
		TextEditor.getInstance().chooseFontLabel.setEnabled(state);
		TextEditor.getInstance().chooseFontStyleLabel.setEnabled(state);
		TextEditor.getInstance().chooseFontSizeLabel.setEnabled(state);
		TextEditor.getInstance().chooseColorLabel.setEnabled(state);
		TextEditor.getInstance().addDuration.setEnabled(state);
		TextEditor.getInstance().addTextArea.setEnabled(state);

		TextEditor.getInstance().overlayCheck.setEnabled(state);
		TextEditor.getInstance().screenList.setEnabled(state);
		TextEditor.getInstance().fontsList.setEnabled(state);
		TextEditor.getInstance().stylesList.setEnabled(state);
		TextEditor.getInstance().sizesList.setEnabled(state);
		TextEditor.getInstance().coloursList.setEnabled(state);

		TextEditor.getInstance().deleteBtn.setEnabled(state);
		TextEditor.getInstance().prevBtn.setEnabled(state);
		TextEditor.getInstance().saveButton.setEnabled(state);
		TextEditor.getInstance().helpButton.setEnabled(state);


	}
	/**
	 * Method sets all the fields of related to the title screen into the
	 * previously saved values
	 * 
	 * @param textFieldsPath
	 *            : the path of the stored text fields
	 */
	public void setTitleFields(String textFieldsPath) {
		TextEditor.getInstance().titleFields = textFieldsPath;
		File f = new File(TextEditor.getInstance().workingDir + "/.titleFields");
		if (f.exists()) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(f)); // Read the file
				// that saved
				// the fields
				TextEditor.getInstance().screenList.setSelectedItem(reader.readLine());
				TextEditor.getInstance().titleDuration = reader.readLine();

				TextEditor.getInstance().addDuration.setText(TextEditor.getInstance().titleDuration);
				TextEditor.getInstance().overlayCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (TextEditor.getInstance().overlayCheck.isSelected()){
					TextEditor.getInstance().backgroundImageTitle = 0;

				}
				TextEditor.getInstance().defaultCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (TextEditor.getInstance().defaultCheck.isSelected()){
					TextEditor.getInstance().backgroundImageTitle = 1;

				}
				TextEditor.getInstance().frameCheck.setSelected(Boolean.parseBoolean(reader.readLine()));

				if (TextEditor.getInstance().frameCheck.isSelected()){
					TextEditor.getInstance().backgroundImageTitle = 2;

				}
				TextEditor.getInstance().titleFrameTime = reader.readLine();
				TextEditor.getInstance().addTimeFrame.setText(TextEditor.getInstance().titleFrameTime);
				TextEditor.getInstance().titleFontSize = Integer.parseInt(reader.readLine());
				TextEditor.getInstance().sizesList.setSelectedItem("" + TextEditor.getInstance().titleFontSize);
				TextEditor.getInstance().titleFontColour = reader.readLine();
				TextEditor.getInstance().coloursList.setSelectedItem(TextEditor.getInstance().titleFontColour);
				switch (TextEditor.getInstance().titleFontColour) {
				case "black":
					TextEditor.getInstance().addTextArea.setForeground(Color.black);
					break;
				case "green":
					TextEditor.getInstance().addTextArea.setForeground(Color.green);
					break;
				case "blue":
					TextEditor.getInstance().addTextArea.setForeground(Color.blue);
					break;
				case "yellow":
					TextEditor.getInstance().addTextArea.setForeground(Color.yellow);
					break;
				case "red":
					TextEditor.getInstance().addTextArea.setForeground(Color.red);
					break;
				case "white":
					TextEditor.getInstance().addTextArea.setForeground(Color.white);
					break;
				case "pink":
					TextEditor.getInstance().addTextArea.setForeground(Color.pink);
					break;
				}
				TextEditor.getInstance().titleFontName = reader.readLine();
				TextEditor.getInstance().titleFontStyle = Integer.parseInt(reader.readLine());
				TextEditor.getInstance().stylesList.setSelectedIndex(TextEditor.getInstance().titleFontStyle);
				TextEditor.getInstance().titlePreviewFont = reader.readLine();
				TextEditor.getInstance().fontsList.setSelectedItem(TextEditor.getInstance().titlePreviewFont);
				String line;
				StringBuilder allText = new StringBuilder();
				while (((line = reader.readLine()) != null)) {
					allText.append(line);
					allText.append(System.getProperty("line.separator"));
				}
				TextEditor.getInstance().addTextArea.setText(allText.toString());
				TextEditor.getInstance().addTextArea.setFont(new Font(TextEditor.getInstance().titlePreviewFont, TextEditor.getInstance().titleFontStyle,
						TextEditor.getInstance().titleFontSize));
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
		TextEditor.getInstance().titleFields = textFieldsPath;
		File f = new File(TextEditor.getInstance().workingDir + "/.creditFields");
		if (f.exists()) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(f));
				TextEditor.getInstance().screenList.setSelectedItem(reader.readLine()); // Credit Screen
				TextEditor.getInstance().creditDuration = reader.readLine();
				TextEditor.getInstance().addDuration.setText(TextEditor.getInstance().creditDuration);
				TextEditor.getInstance().overlayCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (TextEditor.getInstance().overlayCheck.isSelected()){
					TextEditor.getInstance().backgroundImageCredit = 0;

				}
				TextEditor.getInstance().defaultCheck
				.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (TextEditor.getInstance().defaultCheck.isSelected()){
					TextEditor.getInstance().backgroundImageCredit = 1;

				}
				TextEditor.getInstance().frameCheck.setSelected(Boolean.parseBoolean(reader.readLine()));
				if (TextEditor.getInstance().frameCheck.isSelected()){
					TextEditor.getInstance().backgroundImageCredit = 2;

				}
				TextEditor.getInstance().creditFrameTime = reader.readLine();
				TextEditor.getInstance().addTimeFrame.setText(TextEditor.getInstance().creditFrameTime);
				TextEditor.getInstance().creditFontSize = Integer.parseInt(reader.readLine());
				TextEditor.getInstance().sizesList.setSelectedItem("" + TextEditor.getInstance().creditFontSize);
				TextEditor.getInstance().creditFontColour = reader.readLine();
				TextEditor.getInstance().coloursList.setSelectedItem(TextEditor.getInstance().creditFontColour);
				switch (TextEditor.getInstance().creditFontColour) {
				case "black":
					TextEditor.getInstance().addTextArea.setForeground(Color.black);
					break;
				case "green":
					TextEditor.getInstance().addTextArea.setForeground(Color.green);
					break;
				case "blue":
					TextEditor.getInstance().addTextArea.setForeground(Color.blue);
					break;
				case "yellow":
					TextEditor.getInstance().addTextArea.setForeground(Color.yellow);
					break;
				case "red":
					TextEditor.getInstance().addTextArea.setForeground(Color.red);
					break;
				case "white":
					TextEditor.getInstance().addTextArea.setForeground(Color.white);
					break;
				case "pink":
					TextEditor.getInstance().addTextArea.setForeground(Color.pink);
					break;
				}
				TextEditor.getInstance().creditFontName = reader.readLine();
				TextEditor.getInstance().creditFontStyle = Integer.parseInt(reader.readLine());
				TextEditor.getInstance().stylesList.setSelectedIndex(TextEditor.getInstance().creditFontStyle);
				TextEditor.getInstance().creditPreviewFont = reader.readLine();
				TextEditor.getInstance().fontsList.setSelectedItem(TextEditor.getInstance().creditPreviewFont);
				String line;
				StringBuilder allText = new StringBuilder();
				while (((line = reader.readLine()) != null)) {
					allText.append(line);
					allText.append(System.getProperty("line.separator"));
				}
				TextEditor.getInstance().addTextArea.setText(allText.toString());
				TextEditor.getInstance().addTextArea.setFont(new Font(TextEditor.getInstance().creditPreviewFont,
						TextEditor.getInstance().creditFontStyle, TextEditor.getInstance().creditFontSize));
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
		TextEditor.getInstance().addDuration.setText("");
		TextEditor.getInstance().addTextArea.setText("");
		TextEditor.getInstance().addTimeFrame.setText("hh:mm:ss");
		TextEditor.getInstance().overlayCheck.setSelected(true);
		TextEditor.getInstance().defaultCheck.setSelected(false);
		TextEditor.getInstance().frameCheck.setSelected(false);
		TextEditor.getInstance().defaultCheck.setEnabled(false);
		TextEditor.getInstance().addTimeFrame.setEnabled(false);
		TextEditor.getInstance().frameCheck.setEnabled(false);
		if ((TextEditor.getInstance().projectPath != null) && (!TextEditor.getInstance().projectPath.isEmpty())) {
			TextEditor.getInstance().fontsList.setSelectedIndex(0);
			TextEditor.getInstance().stylesList.setSelectedIndex(0);
			TextEditor.getInstance().sizesList.setSelectedIndex(6);
			TextEditor.getInstance().coloursList.setSelectedIndex(0);
		}
		TextEditor.getInstance().backgroundImageOption = 0;
		TextEditor.getInstance().titleDuration = "";
		TextEditor.getInstance().titleFrameTime = "";
		TextEditor.getInstance().fontType = 0;
		TextEditor.getInstance().fontStyle = 0;
		TextEditor.getInstance().titleFontSize = 30;
		TextEditor.getInstance().fontSize = 30;
		TextEditor.getInstance().titleFontColour = "black";
		TextEditor.getInstance().fontColour = "black";
		TextEditor.getInstance().titleFontName = "";
		TextEditor.getInstance().fontName = "";
		TextEditor.getInstance().titleFontStyle = 0;
		TextEditor.getInstance().titlePreviewFont = "Arial";
		TextEditor.getInstance().prevFont = "Arial";
	}

	/**
	 * Method changes the enable state of all the field in the text edit tab
	 * except the screen list
	 */
	public void setFieldsEnabled(boolean state) {
		TextEditor.getInstance().deleteBtn.setEnabled(state);
		TextEditor.getInstance().saveButton.setEnabled(state);
		TextEditor.getInstance().helpButton.setEnabled(state);
		TextEditor.getInstance().prevBtn.setEnabled(state);

		TextEditor.getInstance().addDuration.setEnabled(state);
		TextEditor.getInstance().addTextArea.setEnabled(state);
		TextEditor.getInstance().addTimeFrame.setEnabled(state);

		if (!state){
			TextEditor.getInstance().getTime1Button.setEnabled(state);
		}

		TextEditor.getInstance().fontsList.setEnabled(state);
		TextEditor.getInstance().stylesList.setEnabled(state);
		TextEditor.getInstance().sizesList.setEnabled(state);
		TextEditor.getInstance().coloursList.setEnabled(state);

	}

	/**
	 * Method refreshes all the credits fields in the text editing tab
	 */
	public void refreshCreditScreen() {
		TextEditor.getInstance().addDuration.setText("");
		TextEditor.getInstance().addTextArea.setText("");
		TextEditor.getInstance().addTimeFrame.setText("hh:mm:ss");
		TextEditor.getInstance().addTimeFrame.setEnabled(false);
		TextEditor.getInstance().overlayCheck.setSelected(true);
		TextEditor.getInstance().defaultCheck.setSelected(false);
		TextEditor.getInstance().frameCheck.setSelected(false);
		TextEditor.getInstance().defaultCheck.setEnabled(false);
		TextEditor.getInstance().frameCheck.setEnabled(false);
		if ((TextEditor.getInstance().projectPath != null) && (!TextEditor.getInstance().projectPath.isEmpty())) {
			TextEditor.getInstance().fontsList.setSelectedIndex(0);
			TextEditor.getInstance().stylesList.setSelectedIndex(0);
			TextEditor.getInstance().sizesList.setSelectedIndex(6);
			TextEditor.getInstance().coloursList.setSelectedIndex(0);
		}
		TextEditor.getInstance().backgroundImageOption = 0;
		TextEditor.getInstance().creditDuration = "";
		TextEditor.getInstance().creditFrameTime = "";
		TextEditor.getInstance().fontType = 0;
		TextEditor.getInstance().fontStyle = 0;
		TextEditor.getInstance().fontSize = 30;
		TextEditor.getInstance().creditFontSize = 30;
		TextEditor.getInstance().fontColour = "black";
		TextEditor.getInstance().creditFontColour = "black";
		TextEditor.getInstance().fontName = "";
		TextEditor.getInstance().creditFontName = "";
		TextEditor.getInstance().creditFontStyle = 0;
		TextEditor.getInstance().creditPreviewFont = "Arial";
		TextEditor.getInstance().prevFont = "Arial";
	}

	/**
	 * Method refreshes all the project fields in the text editing tab
	 */
	public void refreshtextEdit() {
		if ((TextEditor.getInstance().projectPath != null) && (!TextEditor.getInstance().projectPath.isEmpty())) {
			TextEditor.getInstance().screenList.setSelectedIndex(0);
		}
		TextEditor.getInstance().screenType = "";
		TextEditor.getInstance().projectPath = "";
		TextEditor.getInstance().hiddenDir = "";
		TextEditor.getInstance().videoPath = "";
		TextEditor.getInstance().videoLength = "";
		TextEditor.getInstance().workingDir = "";
		TextEditor.getInstance().titleFields = "";
		TextEditor.getInstance().creditFields = "";
	}
}
