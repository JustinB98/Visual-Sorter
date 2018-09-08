package model;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utility.FileUtility;
import utility.Global;

public class DataFileCollector {

	private Stage stage;
	private TextField[] textFields = new TextField[Global.MAX_LEN];
	private final int TEXTFIELD_WIDTH = 60;
	private final int SCROLL_PANE_WIDTH = 100;

	{
		initTextFields();
		Platform.runLater(() -> {
			init();
		});
	}

	private void init() {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		Text text = new Text("Enter Values");
		GridPane gp = new GridPane();
		gp.setHgap(4);
		gp.setAlignment(Pos.CENTER);
		for (int i = 0; i < textFields.length; ++i) {
			Text number = new Text(( i + 1 ) + "");
			gp.add(number, 1, i);
			gp.add(textFields[i], 0, i);
		}
		
		CheckBox cb = new CheckBox("Set on startup");
		
		Button btn = new Button("Save");
		btn.setOnAction(e -> {
			FileUtility.setDataFile(textFields, cb.isSelected());
			stage.close();
		});
		ScrollPane sp = new ScrollPane(gp);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setMinWidth(SCROLL_PANE_WIDTH);
		VBox dataPane = new VBox(5, text, cb, sp, btn);
		// VBox pane = new VBox(5, label, sp, setDataBtn);
		dataPane.setAlignment(Pos.CENTER);
		dataPane.setPadding(new Insets(10, 0, 0, 0));
		dataPane.setId("data-pane");
		stage.setScene(new Scene(dataPane, 100, 500));
	}

	public void showStage() {
		stage.showAndWait();
	}

	private void initTextFields() {
		for (int i = 0; i < textFields.length; ++i) {
			textFields[i] = new IntegerField();
			textFields[i].setMaxWidth(TEXTFIELD_WIDTH);
		}
	}

}
