package model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SliderStage {

	private Stage stage;
	private Slider lengthSelecter;
	private Button inputBtn;
	private int value = -1;
	private ScrollingNumbers scrollingNumbers;

	private int min, max, current;

	public SliderStage(int min, int max) {
		this.min = min;
		this.max = max;
		init();
		stage = new Stage();
		stage.setTitle("Choose New Length");
		stage.setResizable(false);
		stage.setScene(new Scene(getRoot(), 400, 200));
		stage.setOnCloseRequest(e -> {
			if (value < 0) {
				value = current;
			}
		});
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.getScene().setOnKeyPressed(this::handleKeyEvent);
	}

	private void handleKeyEvent(KeyEvent e) {
		if (e.getCode().isDigitKey()) {
			scrollingNumbers.appendText(Integer.parseInt(e.getText()));
		} else if (e.getCode() == KeyCode.BACK_SPACE) {
			scrollingNumbers.setTo(current);
		} else if (e.getCode() == KeyCode.ENTER) {
			value = (int) scrollingNumbers.getValue();
			stage.close();
		} else if (e.getCode().isArrowKey()) {
			if (e.getCode() == KeyCode.RIGHT) {
				scrollingNumbers.increment();
			} else if (e.getCode() == KeyCode.LEFT) {
				scrollingNumbers.decrement();
			}
		}
		stage.requestFocus();
	}

	private Pane getRoot() {
		lengthSelecter.valueProperty().addListener((v, old, newV) -> {
			scrollingNumbers.setValue(newV.intValue());
		});
		VBox root = new VBox(5, lengthSelecter, scrollingNumbers, inputBtn);
		root.setAlignment(Pos.CENTER);
		return root;
	}

	private void init() {
		lengthSelecter = new Slider(min, max, 1);
//		lengthSelecter.setValue(Global.len());
		lengthSelecter.setMajorTickUnit(10);
		lengthSelecter.setShowTickLabels(true);
		lengthSelecter.setShowTickMarks(true);
		scrollingNumbers = new ScrollingNumbers(min, max);
		scrollingNumbers.setAction(() -> {
			lengthSelecter.setValue(scrollingNumbers.getValue());
		});
//		scrollingNumbers.setValue(Global.len());
		inputBtn = new Button("Input");
		inputBtn.setOnAction(e -> {
			value = (int) lengthSelecter.getValue();
			stage.close();
		});
	}

	public void setCurrent(int value) {
		current = value;
		lengthSelecter.setValue(value);
		scrollingNumbers.setValue(value);
	}
	
	public int getValue() {
		stage.showAndWait();
		return value;
	}

}
