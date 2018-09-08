package panes;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.TitledBorderPane;
import model.TutorialSortingManager;
import utility.GUIService;
import utility.Global;
import utility.KeyboardActions;
import utility.Utility;

public class TutorialControlPane extends VBox {

	private RadioButton mergeRadBtn, shellRadBtn, quickRadBtn;
	private HBox btnPane;
	private Button nextStepBtn, startBtn, cancelBtn, randomizeBtn;
	private Text stepText;
	private TutorialSortingManager sorter;
	private BooleanProperty start;

	public TutorialControlPane(BooleanProperty start, BooleanProperty stop) {
		this.start = start;
		sorter = new TutorialSortingManager(stop, start, stepText.textProperty());
		Pane sub = getSubRoot();
		HBox textPane = new HBox(stepText);
		textPane.setAlignment(Pos.CENTER);
		textPane.setPadding(new Insets(-50, 0, 0, 0));
		// sub.setPadding(new Insets(0, -500, 0, -500));
		getChildren().addAll(textPane, sub);
		initListener();
		initTooltips();
		setId("tutorial-control-pane");
	}

	private Pane getSubRoot() {
		HBox subPane = new HBox(5, mergeRadBtn, shellRadBtn, quickRadBtn, btnPane);
		subPane.setAlignment(Pos.CENTER);
		TitledBorderPane titledSubPane = new TitledBorderPane("Controls", subPane);
		titledSubPane.setAlignment(Pos.CENTER);
		HBox subRoot = new HBox(titledSubPane);
		subRoot.setAlignment(Pos.CENTER);
		return subRoot;
	}

	private void initListener() {
		start.addListener((v, old, newV) -> {
			btnPane.getChildren().clear();
			if (newV) {
				btnPane.getChildren().addAll(nextStepBtn, cancelBtn);
			} else {
				btnPane.getChildren().addAll(startBtn, randomizeBtn);
			}
		});
	}

	private void initTooltips() {
		Utility.installTooltip(startBtn, "Starts the tutorial\nShortcut: " + KeyboardActions.START_KEY.getName());
		Utility.installTooltip(randomizeBtn,
				"Randomizes the set\nShortcut: " + KeyboardActions.RANDOMIZE_KEY.getName());
		Utility.installTooltip(nextStepBtn, "Advances the tutorial to the next step.\nShortcut: "
				+ KeyboardActions.START_KEY.getName() + "\nShortcut works when the tutorial has started");
		Utility.installTooltip(cancelBtn, "Cancels the tutorial\nShortcut: " + KeyboardActions.STOP_KEY.getName());
	}

	{
		stepText = new Text();
		stepText.setTextAlignment(TextAlignment.CENTER);
		initButtons();
		initRadBtns();
		setSpacing(5);
		setAlignment(Pos.CENTER);
	}

	private void initRadBtns() {
		ToggleGroup tg = new ToggleGroup();
		mergeRadBtn = getRadBtn("Merge", tg);
		shellRadBtn = getRadBtn("Shell", tg);
		quickRadBtn = getRadBtn("Quick", tg);
		quickRadBtn.setSelected(true);
	}

	private RadioButton getRadBtn(String text, ToggleGroup tg) {
		RadioButton rb = new RadioButton(text);
		rb.setToggleGroup(tg);
		return rb;
	}

	private void initButtons() {
		nextStepBtn = new Button("Next Step");
		nextStepBtn.setOnAction(e -> sorter.unpause());
		startBtn = new Button("Start");
		startBtn.setOnAction(e -> startTutorial());
		cancelBtn = new Button("Cancel");
		cancelBtn.setOnAction(e -> sorter.stop());
		randomizeBtn = new Button("Randomize");
		randomizeBtn.setOnAction(e -> Global.randomize());
		initBtnPane();
	}

	private void initBtnPane() {
		btnPane = new HBox(5, startBtn, randomizeBtn);
		btnPane.setAlignment(Pos.CENTER);
	}

	private void startTutorial() {
		// startBtn.setDisable(true);
		if (mergeRadBtn.isSelected()) {
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

	public void nextStep() {
		if (start.get()) {
			sorter.unpause();
		} else {
			startTutorial();
		}
	}

	public void setText(String s) {
		stepText.setText(s);
	}

	public Button getNextStepBtn() {
		return nextStepBtn;
	}

	public void setFocused() {
		startBtn.requestFocus();
	}

}
