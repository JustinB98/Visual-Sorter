package panes;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import model.SortingManager;
import utility.GUIService;
import utility.Global;
import utility.KeyboardActions;
import utility.Utility;

public class ControlPane extends HBox {

	private RadioButton bubbleRadBtn, selectionRadBtn, insertionRadBtn, mergeRadBtn, shellRadBtn, quickRadBtn;
	private Button startBtn, randomizeBtn, stopBtn, pauseBtn;
	private CheckBox fastBox;
	private HBox btnPane;
	private SortingManager sorter;
	private BooleanProperty start, stop;

	public ControlPane(BooleanProperty start, BooleanProperty stop) {
		this.start = start;
		this.stop = stop;
		sorter = new SortingManager(fastBox.selectedProperty(), stop, start);
		initPropertyListeners();
	}

	private void initPropertyListeners() {
		start.addListener((v, old, newV) -> {
			if (!newV) {
				sorter.resumeNoMatterWhat();
				setPauseBtnText();
			}
			toggleBtnPane(newV);
			// don't mind this line
			Global.DATA_COLLECTION.getSetDataBtn().setDisable(newV);
		});
		stop.addListener((v, old, newV) -> {
			sorter.resumeNoMatterWhat();
			setPauseBtnText();
		});
	}

	private void setPauseBtnText() {
		pauseBtn.setText(( sorter.isPaused() ? "Unpause" : "Pause" ));
	}

	{
		setId("control-pane");
		initNodes();
		btnPane = new HBox(10, startBtn, randomizeBtn);
		btnPane.setAlignment(Pos.CENTER);
		setAlignment(Pos.CENTER);
		setSpacing(10);
		getChildren().addAll(bubbleRadBtn, selectionRadBtn, insertionRadBtn, mergeRadBtn, shellRadBtn, quickRadBtn,
				btnPane, fastBox);
	}

	private void initNodes() {
		initRadioButtons();
		initButtons();
		initCheckBox();
		initButtonToolTips();
	}

	public void setFastBoxSelected(boolean fast) {
		fastBox.setSelected(fast);
	}

	public boolean getFastBoxSelected() {
		return fastBox.isSelected();
	}

	public BooleanProperty fastBoxSelectionProperty() {
		return fastBox.selectedProperty();
	}

	private void initRadioButtons() {
		ToggleGroup tg = new ToggleGroup();
		bubbleRadBtn = getRadioButton("Bubble", "bubble", tg);
		bubbleRadBtn.setSelected(true);
		selectionRadBtn = getRadioButton("Selection", "selection", tg);
		insertionRadBtn = getRadioButton("Insertion", "insertion", tg);
		mergeRadBtn = getRadioButton("Merge", "merge", tg);
		shellRadBtn = getRadioButton("Shell", "shell", tg);
		quickRadBtn = getRadioButton("Quick", "quick", tg);
		// quickRadBtn.setSelected(true);
		initRadioButtonTooltips();
	}

	/**
	 * Generates a radio button with specified parameters
	 * 
	 * @param text
	 *            Text for the radio button
	 * @param userData
	 *            Object to be used to identify the radio button
	 * @param tg
	 *            toggle group
	 * @return generated radio button
	 */
	private RadioButton getRadioButton(String text, String userData, ToggleGroup tg) {
		RadioButton rb = new RadioButton(text);
		rb.setUserData(userData);
		rb.setToggleGroup(tg);
		return rb;
	}

	private void toggleBtnPane(boolean newV) {
		Platform.runLater(() -> {
			btnPane.getChildren().clear();
			if (newV) {
				btnPane.getChildren().addAll(stopBtn, pauseBtn);
			} else {
				btnPane.getChildren().addAll(startBtn, randomizeBtn);
			}
		});
	}

	public void pause() {
		sorter.togglePause();
		setPauseBtnText();
	}

	public void addToggleListener(ChangeListener<Toggle> e) {
		// could grab any radio button's toggle group or just have it as an
		// instance method
		bubbleRadBtn.getToggleGroup().selectedToggleProperty().addListener(e);
	}

	/* oops */
	private void initRadioButtonTooltips() {
		Utility.installTooltip(bubbleRadBtn,
				"The worst sort of them all" + "\nA lot of swapping and passes\nO(N\u00B2)");
		Utility.installTooltip(selectionRadBtn,
				"Not the worst, but still pretty bad." + "\nDoesn't swap as much as bubble sort\nO(N\u00B2)");
		Utility.installTooltip(insertionRadBtn,
				"Sort that better than bubble sort and selection sort,"
						+ "\nhowever, it's weakness is when the\narray is not in order."
						+ "\nBut if the array is in order, it's the fastest sort\nO(N\u00B2)");
		Utility.installTooltip(mergeRadBtn,
				"An advanced sort algorithm.\nBetter than the basic sorting algorithm."
						+ "\nOne weakness is that it needs twice the memory because it needs"
						+ "\nto make a temp array of the same size.\nO(Nlog(N))");
		Utility.installTooltip(shellRadBtn,
				"An advanced sort algorithm.\nAn improvement to the insertion sort."
						+ "\nBrings lower values to the bottom and higher values to the top."
						+ "\nUses intervals (h) to swap each element."
						+ "\nWhen the interval(h) is one, then an insertion sort is done\nO(N(logN))\u00B2");
		Utility.installTooltip(quickRadBtn,
				"Most frequently used sorting algorithm." + "\nWorks by partitioning sections of the"
						+ "\ndata using a pivot until the array is in order"
						+ "O(Nlog(N)), but on bad days, O(N\u00B2)");
	}

	/* Not being used at the moment, but you never know */
	public void draw() {
		Platform.runLater(() -> {
			getChildren().clear();
			getChildren().addAll(bubbleRadBtn, selectionRadBtn, insertionRadBtn, mergeRadBtn, shellRadBtn, btnPane,
					fastBox);
		});
	}

	private void initButtonToolTips() {
		Utility.installTooltip(randomizeBtn,
				"Randomizes the lines\nShortcut: " + KeyboardActions.RANDOMIZE_KEY.getName());
		Utility.installTooltip(stopBtn, "Stops the current sort\nShortcut: " + KeyboardActions.STOP_KEY.getName()
				+ "\n(Stops once it's safe to, so it may not stop right away)");
		Utility.installTooltip(startBtn,
				"Starts the set of the current data on screen\nShortcut: " + KeyboardActions.START_KEY.getName());
		Utility.installTooltip(pauseBtn,
				"Pauses the sort when it's running\nShortcut: " + KeyboardActions.PAUSE_KEY.getName());
	}

	private void initButtons() {
		randomizeBtn = new Button("Randomize");
		randomizeBtn.setOnAction(e -> Global.randomize());
		stopBtn = new Button("Stop");
		stopBtn.setOnAction(e -> stop.set(true));
		startBtn = new Button("Start");
		startBtn.setOnAction(e -> startSorting());
		pauseBtn = new Button("Pause");
		pauseBtn.setOnAction(e -> pause());
	}

	public void startSorting() {
		if (!start.get()) {
			if (bubbleRadBtn.isSelected()) {
				// new Thread(this::bubbleSort).start();
				new GUIService(sorter::bubbleSort).start();
				start.set(true);
			} else if (selectionRadBtn.isSelected()) {
				// new Thread(this::selectionSort).start();
				new GUIService(sorter::selectionSort).start();
				start.set(true);
			} else if (insertionRadBtn.isSelected()) {
				// new Thread(this::insertionSort).start();
				new GUIService(sorter::insertionSort).start();
				start.set(true);
			} else if (mergeRadBtn.isSelected()) {
				// new Thread(this::mergeSort).start();
				new GUIService(sorter::mergeSort).start();
				start.set(true);
			} else if (shellRadBtn.isSelected()) {
				new GUIService(sorter::shellSort).start();
				start.set(true);
			} else if (quickRadBtn.isSelected()) {
				new GUIService(sorter::quickSort).start();
				start.set(true);
			}
		}
	}

	public void setFocused() {
		startBtn.requestFocus();
	}

	private void initCheckBox() {
		fastBox = new CheckBox("Fast Sort");
		Utility.installTooltip(fastBox,
				"Sorts the lines faster to see the general sort,"
						+ "\nor slow to see the sort in detail\nMAY CAUSE SOME RENDERING ISSUES\nShortcut: "
						+ KeyboardActions.FAST_SORT_KEY);
	}

}