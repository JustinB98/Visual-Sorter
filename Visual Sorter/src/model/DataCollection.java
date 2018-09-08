package model;

import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utility.Global;
import utility.KeyboardActions;
import utility.Utility;

public class DataCollection {

	private TextField[] textFields = new TextField[Global.MAX_LEN];
	private final int SCROLL_PANE_WIDTH = 100;
	private final int TEXTFIELD_WIDTH = 60;
	private final Button setDataBtn;
	private VBox dataPane;

	{
		for (int i = 0; i < textFields.length; ++i) {
			textFields[i] = new IntegerField();
			textFields[i].setMaxWidth(TEXTFIELD_WIDTH);
		}
		setDataBtn = new Button("Set Data");
		initBtn();
		initPane();
	}

	private void initBtn() {
		setDataBtn.setOnAction(e -> Global.DATA_MANAGER.collectData());
		Utility.installTooltip(setDataBtn, "Sets the values for the lines with the inputted data\nShortcut: "
				+ KeyboardActions.SET_DATA_KEY.getName());
	}

	public Button getSetDataBtn() {
		return setDataBtn;
	}

	// the label isn't being used at the time
	@SuppressWarnings("unused")
	private void initPane() {
		// not sure if i should use
		Text label = new Text("Enter Data");
		GridPane textFieldPane = new GridPane();
		textFieldPane.setHgap(4);
		for (int i = 0; i < textFields.length; ++i) {
			Text number = new Text(( i + 1 ) + "");
			textFieldPane.add(number, 1, i);
			textFieldPane.add(textFields[i], 0, i);
		}
		ScrollPane sp = new ScrollPane(textFieldPane);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setMinWidth(SCROLL_PANE_WIDTH);
		// VBox pane = new VBox(5, label, sp, setDataBtn);
		dataPane = new VBox(5, setDataBtn, sp);
		dataPane.setAlignment(Pos.CENTER);
		dataPane.setPadding(new Insets(10, 0, 0, 0));
		dataPane.setId("data-pane");
	}

	public Pane getDataPane() {
		return dataPane;
	}

	public double[] getData() {
		LinkedList<Double> linkedData = new LinkedList<>();
		String text;
		for (int i = 0; i < textFields.length; ++i) {
			text = textFields[i].getText();
			if (Utility.isDouble(text)) {
				linkedData.add(Double.parseDouble(text));
			}
		}
		return Utility.convertListToArrayDouble(linkedData);
	}

	public void clear() {
		for (TextField tf : textFields) {
			tf.clear();
		}
	}

	public void setTo(int[] data) {
		if (data != null) {
			int len = Math.min(data.length, textFields.length);
			for (int i = 0; i < len; ++i) {
				if (data[i] >= 0) {
					textFields[i].setText(String.valueOf(data[i]));
				}
			}
		}
	}

}
