package menus;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import model.MessageBox;

public class DeveloperNotesSubMenu extends Menu {

	private MenuItem generalNotesItem, bugNotesItem;
	private MessageBox generalNotesMsgBox, bugsMsgBox;

	public DeveloperNotesSubMenu() {
		super("Developer Notes");
		getItems().addAll(generalNotesItem, bugNotesItem);
	}

	{
		initMessageBoxes();
		generalNotesItem = new MenuItem("General Notes");
		generalNotesItem.setOnAction(e -> generalNotesMsgBox.showAndWait());
		bugNotesItem = new MenuItem("Bugs");
		bugNotesItem.setOnAction(e -> bugsMsgBox.showAndWait());
	}

	private void initMessageBoxes() {
		Platform.runLater(() -> {
			initGeneralNotesMsgBox();
			initBugNotesMsgBox();
		});
	}

	private void initGeneralNotesMsgBox() {
		String message = "Made by Justin Behrman\nI was inspired to make this program because of the visual\nsorting videos that can be found on youtube\n\n"
				+ "If you want to know what something does or means, try hovering over it!"
				+ "\n\nIf you are having trouble seeing all the lines,\n"
				+ "try changing the length of the array. (Options/Set Length)\n\n"
				+ "These sorts aren't accurate in their speed. Most of them were too fast\nto show, so they had to be slowed down";
		generalNotesMsgBox = new MessageBox(message);
		generalNotesMsgBox.setTitle("General Notes");
		generalNotesMsgBox.setTextAlignment(TextAlignment.CENTER);
		generalNotesMsgBox.setWidth(410);
		generalNotesMsgBox.setHeight(300);
		generalNotesMsgBox.initModality(Modality.APPLICATION_MODAL);
	}

	private void initBugNotesMsgBox() {
		String message = "Bugs:\nThere are many bugs with this program, some of which include:\n\t1. Random crashing\n\t"
				+ "2. Rendering issues\n\t3. Scaling issues with the lines\nand much, much more...\n\n"
				+ "\tWhen the program crashes, there will be a message box that\n"
				+ "may or may not render correctly just to warn you.\n"
				+ "\tAlso have the window maximized may cause rendering issues\n"
				+ "\tFast sort is unstable and may cause rendering issues.\n"
				+ "\tThe easiest fix for rendering issues is just restarting the program."
				+ "\n\nOther issues are still a work in progress...";
		bugsMsgBox = new MessageBox(message);
		bugsMsgBox.setTextAlignment(TextAlignment.LEFT);
		bugsMsgBox.setTitle("Bugs");
		bugsMsgBox.setWidth(400);
		bugsMsgBox.setHeight(320);
		bugsMsgBox.initModality(Modality.APPLICATION_MODAL);
	}

}
