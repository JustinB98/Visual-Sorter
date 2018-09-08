package menus;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import model.MessageBox;
import utility.KeyboardActions;

public class HelpMenu extends Menu {

	private MenuItem shortcutsItem, styleHelpItem;
	private MessageBox shortcutMsgBox;
	private MessageBox styleMsgBox;
	private DeveloperNotesSubMenu developerNotes;

	public HelpMenu() {
		super("Help");
		getItems().addAll(developerNotes, shortcutsItem, styleHelpItem);
	}

	{
		initShortcutMsgBox();
		initStyleMsgBox();
		shortcutsItem = new MenuItem("Shortcuts");
		shortcutsItem.setOnAction(e -> shortcutMsgBox.showAndWait());
		developerNotes = new DeveloperNotesSubMenu();
		styleHelpItem = new MenuItem("Styling");
		styleHelpItem.setOnAction(e -> styleMsgBox.showAndWait());
	}

	private void initShortcutMsgBox() {
		Platform.runLater(() -> {
			String message = KeyboardActions.START_KEY.getName() + ":\tStart sort or tutorial\n"
					+ KeyboardActions.STOP_KEY.getName() + ":\tstops Sort or tutorial\n"
					+ KeyboardActions.RANDOMIZE_KEY.getName() + ":\tRandomizes current set (will replace user data)\n"
					+ KeyboardActions.PAUSE_KEY.getName()
					+ ":\tPauses current sort (doesn't work on tutorials)\nF:\tEnable/Disable fast sort\n"
					+ KeyboardActions.CLEAR_DATA_KEY.getName() + ":\tClears any data in the text fields\n"
					+ KeyboardActions.SET_DATA_KEY.getName()
					+ ":\tSets the data from the data area\n\nRemember! You need to have the stage itself selected for these to work,\n"
					+ "not have a text field selected!!";
			shortcutMsgBox = new MessageBox(message);
			shortcutMsgBox.setTitle("Shortcuts");
			shortcutMsgBox.setWidth(400);
			shortcutMsgBox.initModality(Modality.APPLICATION_MODAL);
			shortcutMsgBox.setTextAlignment(TextAlignment.LEFT);
		});
	}

	private void initStyleMsgBox() {
		Platform.runLater(() -> {
			String message = "If you want to set your own styles, create a file"
					+ "\ncalled \"style.css\" in the same directory as this program." + "\nId/Class names include:\n"
					+ "\tcontrol-pane\n" + "\tlegend\n" + "\ttutorial-control-pane\n" + "\tline-pane\n\tdata-pane\n"
					+ "Note that the styling isn't perfect";
			styleMsgBox = new MessageBox(message);
			styleMsgBox.setTitle("Styling");
			styleMsgBox.setWidth(350);
			styleMsgBox.initModality(Modality.APPLICATION_MODAL);
			styleMsgBox.setTextAlignment(TextAlignment.LEFT);
		});
	}

}
