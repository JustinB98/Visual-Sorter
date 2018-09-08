package menus;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.DataFileCollector;
import model.SliderStage;
import utility.Global;

public class OptionsMenu extends Menu {

	private CheckMenuItem modeItem;
	private DisplaySubMenu displayMenu;
	private MenuItem setLengthItem, setDataFieldItem;
	private DataFileCollector dataFileCollector = new DataFileCollector();

	
	private SliderStage sliderInput;

	public OptionsMenu() {
		super("Options");
		getItems().addAll(modeItem, displayMenu, setLengthItem, setDataFieldItem);
		Platform.runLater(() -> sliderInput = new SliderStage(0, Global.MAX_LEN));
	}

	{
		modeItem = new CheckMenuItem("Regular");
		modeItem.selectedProperty().addListener((v, old, newV) -> changeModeItemText(newV));
		displayMenu = new DisplaySubMenu();
		setLengthItem = new MenuItem("Set Length");
		setLengthItem.setOnAction(e -> setLength());
		setDataFieldItem = new MenuItem("Set Data Fields");
		setDataFieldItem.setOnAction(e -> dataFileCollector.showStage());
	}

	private void setLength() {
		if (!Global.START.get()) {
			sliderInput.setCurrent(Global.len());
			int value = sliderInput.getValue();
			if (value > 0) {
				Global.setCurrentLen(value);
			}
		}
	}

	public void toggleModeItem() {
		modeItem.setSelected(!modeItem.isSelected());
	}

	private void changeModeItemText(boolean newValue) {
		modeItem.setText(newValue ? "Tutorial" : "Regular");
	}

	public void setModeItemOnAction(EventHandler<ActionEvent> e) {
		modeItem.setOnAction(e);
	}

}