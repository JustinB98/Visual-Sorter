package model;

import javafx.scene.control.TextField;

public class IntegerField extends TextField {

	public IntegerField() {
		this("");
	}

	public IntegerField(int i) {
		this(String.valueOf(i));
	}

	public IntegerField(String s) {
		super(s);
	}

	{
		textProperty().addListener((v, oldVal, newVal) -> {
			if (!newVal.matches("\\d*")) {
				setText(newVal.replaceAll("[^\\d]", ""));
			}
		});
	}

	public int getIntValue() {
		int value = 0;
		try {
			Integer.parseInt(getText());
		} catch (NumberFormatException e) {}
		return value;
	}

}
