package model;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * 
 * NOTE: I DID NOT MAKE THIS NOR DO I TAKE CREDIT </br>
 * I found this on <a href="https://stackoverflow.com">Stack Overflow</a> </br>
 * <a href=
 * "https://stackoverflow.com/questions/14860960/groupbox-titledborder-in-javafx-2">Source
 * and original code</a> </br>
 * Some things I changed from the original code
 * <ul>
 * <li>I moved the css to the code as opposed to another file</li>
 * <li>Changed the font</li>
 * <li>Changed the title alignment to the top left</li>
 * <li>Took out the code for the background of the stack pane being white</li>
 * </ul>
 * 
 * @author <a href= "https://stackoverflow.com/users/516245/zur4ik">zur4ik</a>
 * 
 */
public class TitledBorderPane extends StackPane {
	public TitledBorderPane(String titleString, Node content) {
		Label title = new Label(" " + titleString + " "); // spaces to avoid
															// clumping issues
		title.setFont(Font.font("Times New Roman", 13)); // changed font
		// changed background color to be the light gray as in the default
		// javafx stylesheet (caspian.css)
		title.setStyle(" -fx-background-color: #f4f4f4; -fx-translate-y: -12;");
		StackPane.setAlignment(title, Pos.TOP_LEFT); // changed alignment

		StackPane contentPane = new StackPane();
		content.setStyle("-fx-padding: 20 10 10 10");
		contentPane.getChildren().add(content);

		setStyle("-fx-content-display: top; -fx-border-insets: 20 15 15 15; -fx-border-color: black;"
				+ "-fx-border-width: 2;");
		getChildren().addAll(title, contentPane);
	}
}
