package model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MessageBox extends Stage {

	private Text messageText;

	public MessageBox() {
		this("", Color.BLACK);
	}

	public MessageBox(String initalMessage) {
		this(initalMessage, Color.BLACK);
	}

	public MessageBox(String initalMessage, Color color) {
		messageText = new Text(initalMessage);
		messageText.setFill(color);
		messageText.setTextAlignment(TextAlignment.CENTER);
		setScene(new Scene(getRoot(), 200, 200));
		getScene().setOnKeyPressed(e -> close());
	}

	private VBox getRoot() {
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(e -> close());
		VBox root = new VBox(5, messageText, exitBtn);
		root.setAlignment(Pos.CENTER);
		return root;
	}

	public void setText(String newText) {
		messageText.setText(newText);
	}
	
	public void setTextAlignment(TextAlignment value) {
		messageText.setTextAlignment(value);
	}

	public void setTextColor(Color color) {
		messageText.setFill(color);
	}

}
