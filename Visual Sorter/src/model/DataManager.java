package model;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utility.Global;

/**
 * Manages user data, randomizes the line data, and redraws the lines on the
 * screen when needed
 */
public class DataManager {

	private VisualLine[] lines = Global.LINES;
	private int userDataLen;
	private boolean usingUserData;

	public boolean isUsingUserData() {
		return usingUserData;
	}

	// didn't ever need to set it true
	public void notUsingUserData() {
		usingUserData = false;
	}

	public int getLen() {
		// check because of earlier version
		// probably won't remove it
		return usingUserData ? userDataLen : lines.length;
	}

	public void setData(double[] data) {
		Platform.runLater(() -> {
			usingUserData = true;
			userDataLen = Math.min(data.length, lines.length);
			for (int i = 0; i < userDataLen; ++i) {
				lines[i].setHeight(data[i] * Global.SIZE_MULTIPIER);
				lines[i].setStroke(Color.BLACK);
			}
		});
		Global.draw();
	}

	@SuppressWarnings("unused")
	private VBox getLine(int index) {
		Text number = new Text(lines[index].getHeight() + "");
		number.setFont(Font.font(8));
		VBox line = new VBox(2, lines[index], number);
		line.setAlignment(Pos.CENTER);
		return line;
	}

	public void collectData() {
		setData(Global.DATA_COLLECTION.getData());
	}

}
