package model;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import utility.Utility;

public class ScrollingNumbers extends HBox {

	private int min, max, currentNumber;
	private TextField outputNumber;
	private Button upBtn, downBtn;
	private Accepter action;
	private final double HEIGHT = 60;
	private final double BTN_WIDTH = 30;
	// width leaves room for triple digits 
	private final double OUTPUT_WIDTH = 45;

	public ScrollingNumbers(int min, int max) {
		super();
		setAlignment(Pos.CENTER);
		validateParams(min, max);
		this.min = currentNumber = min;
		outputNumber.setText(min + "");
		this.max = max;
		VBox subRoot = new VBox(upBtn, downBtn);
		subRoot.setAlignment(Pos.CENTER);
		getChildren().addAll(outputNumber, subRoot);
	}

	private void validateParams(int min, int max) {
		if (min > max)
			throw new IllegalArgumentException("Min should be < Max");
	}

	{
		outputNumber = new TextField();
		outputNumber.setFocusTraversable(false);
		upBtn = new Button("\u2191"); // Up arrow
		downBtn = new Button("\u2193"); // Down arrow
		initProperties();
		initTextListener();
	}

	public boolean setTo(int d) {
		d = Utility.constrainInt(d, min, max);
		outputNumber.setText(d + "");
		return d > max || d < min; // useless?? d should always be in the range
	}

	public void setToMin() {
		currentNumber = min;
		outputNumber.setText(min + "");
	}

	public void setToMax() {
		currentNumber = max;
		outputNumber.setText(max + "");
	}

	public void appendText(int d) {
		if (outputNumber.getText().length() == 2) {
			outputNumber.clear();
		}
		outputNumber.appendText(d + "");
	}

	private void initTextListener() {
		outputNumber.textProperty().addListener((v, old, newV) -> {
			if (!newV.isEmpty() && !old.equals(newV)) {
				int value = Integer.parseInt(newV);
				value = Utility.constrainInt(value, min, max);
				currentNumber = value;
				outputNumber.setText(value + "");
				performAction();
			}
		});
	}

	private void initProperties() {
		outputNumber.setFont(Font.font(15));
		outputNumber.setEditable(false);
		setFinalSize(outputNumber, OUTPUT_WIDTH, HEIGHT);
		setFinalSize(upBtn, BTN_WIDTH, HEIGHT / 2);
		setFinalSize(downBtn, BTN_WIDTH, HEIGHT / 2);
		initEventHandling();
	}

	private void initEventHandling() {
		upBtn.setOnAction(e -> increment());

		downBtn.setOnAction(e -> decrement());
	}

	public void decrement() {
		currentNumber = currentNumber <= min ? max : currentNumber - 1;
		outputNumber.setText(currentNumber + "");
		performAction();
		downBtn.requestFocus();
	}

	public void increment() {
		currentNumber = currentNumber >= max ? min : currentNumber + 1;
		outputNumber.setText(currentNumber + "");
		performAction();
		upBtn.requestFocus();
	}

	private void performAction() {
		if (action != null) {
			action.accept();
		}
	}

	public boolean setValue(int value) {
		if (value < min || value > max) {
			return false;
		} else {
			currentNumber = value;
			outputNumber.setText(value + "");
			performAction();
			return true;
		}
	}

	public int getValue() {
		return currentNumber;
	}

	public void setAction(Accepter action) {
		this.action = action;
	}

	private void setFinalSize(Control node, double width, double height) {
		node.setMinSize(width, height);
		node.setMaxSize(width, height);
	}

}
