package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Legend;
import model.TitledBorderPane;
import panes.ControlPane;
import panes.TutorialControlPane;
import utility.FileUtility;
import utility.Global;
import utility.KeyboardActions;
import utility.LegendGetter;
import utility.Utility;

/**
 * @author Justin Behrman
 *
 */
public class MainWindow extends Application {

	/* UI */
	private BooleanProperty start, stop;
	private Stage primaryStage;
	private Pane legend = new Pane();
	private ControlPane controlPane;
	private TutorialControlPane tutControlPane;
	private Pane standardSortingCenter;
	private Pane standardSortingBottom;

	/* Other */
	private boolean standardSorting = true;

	@Override
	public void init() throws Exception {
		// order of method calls are for a reason
		// initCheckBox();
		// initRadioButtons();
		setLegendTo(LegendGetter.bubbleSortLegend);
		initProperties();
		initPropertyListeners();
		// linePane = new HBox(1);
		controlPane = new ControlPane(start, stop);
		tutControlPane = new TutorialControlPane(start, stop);
		initControlPane();
		initOther();
	}

	private void initOther() {
		Global.setModeItemOnAction(e -> {
			if (!start.get()) {
				standardSorting = !standardSorting;
				togglePane();
			} else {
				Global.TOP_BAR.toggleModeItem();
			}
		});
	}

	private void initControlPane() {
		controlPane.setAlignment(Pos.CENTER);
		controlPane.addToggleListener((v, old, newV) -> handleToggleListener(newV));
	}

	private void handleToggleListener(Toggle toggle) {
		switch (toggle.getUserData().toString().trim()) {
			case "bubble":
				setLegendTo(LegendGetter.bubbleSortLegend);
				break;
			case "selection":
				setLegendTo(LegendGetter.selectionSortLegend);
				break;
			case "insertion":
				setLegendTo(LegendGetter.insertionSortLegend);
				break;
			case "merge":
				setLegendTo(LegendGetter.mergeSortLegend);
				break;
			case "shell":
				setLegendTo(LegendGetter.shellSortLegend);
				break;
			case "quick":
				setLegendTo(LegendGetter.quickSortLegend);
				break;
			default:
				throw new UnsupportedOperationException("Didn't make a legend for that radio button yet... :(");
		}
	}

	private void initPropertyListeners() {
		start.addListener((v, old, newV) -> {
			// don't mind this line
			Global.DATA_COLLECTION.getSetDataBtn().setDisable(newV);
		});
		stop.addListener((v, old, newV) -> {
			Utility.resetLineColors();
		});
	}

	private void initProperties() {
		start = Global.START;
		stop = Global.STOP;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		initRoot();
		// primaryStage.widthProperty().addListener((v, oldV, newV) ->
		// System.out.println(newV));
		primaryStage.setTitle("Visual Sorter");
		// height at an offset because of the top pane
		primaryStage.setScene(new Scene(Global.root(), Global.WINDOW_WIDTH + 180, Global.WINDOW_HEIGHT + 140));
		primaryStage.getScene().setOnKeyPressed(this::handleKeyEvent);
		if (FileUtility.STYLE_FILE_EXISTS) {
			primaryStage.getScene().getStylesheets().add("file:style.css");
		}
		primaryStage.show();
	}

	private void handleKeyEvent(KeyEvent e) {
		if (e.getCode() == KeyboardActions.START_KEY) {
			startAction();
		} else if (e.getCode() == KeyboardActions.FAST_SORT_KEY) {
			// fastBox.setSelected(!fastBox.isSelected());
			controlPane.setFastBoxSelected(!controlPane.getFastBoxSelected());
		} else if (e.getCode() == KeyboardActions.RANDOMIZE_KEY && !start.get()) {
			Global.randomize();
		} else if (e.getCode() == KeyboardActions.STOP_KEY && start.get()) {
			stop.set(true);
		} else if (e.getCode() == KeyboardActions.PAUSE_KEY && start.get()) {
			controlPane.pause();
		} else if (e.getCode() == KeyboardActions.CLEAR_DATA_KEY && !start.get()) {
			Global.DATA_COLLECTION.clear();
		} else if (e.getCode() == KeyboardActions.SET_DATA_KEY && !start.get()) {
			Global.DATA_MANAGER.collectData();
		}
	}

	private void startAction() {
		if (standardSorting) {
			controlPane.startSorting();
		} else {
			tutControlPane.nextStep();
		}
	}

	private void togglePane() {
		Utility.resetLineColors();
		if (standardSorting) {
			Global.setBottom(standardSortingBottom);
			controlPane.setFocused();
		} else {
			Global.setBottom(tutControlPane);
			tutControlPane.setFocused();
		}
	}

	private void initRoot() {
		TitledBorderPane topBorderPane = new TitledBorderPane("Controls", controlPane);
		HBox bottom = new HBox(topBorderPane, legend);
		bottom.setAlignment(Pos.CENTER);
		this.standardSortingBottom = bottom;
		this.standardSortingCenter = scrollingLinePane();
		setBorderPane(bottom, Global.DATA_COLLECTION.getDataPane(), this.standardSortingCenter);
		initLinePane();
	}

	private GridPane scrollingLinePane() {
		GridPane gp = new GridPane();
		gp.scaleXProperty().bind(primaryStage.widthProperty().divide(Global.WINDOW_WIDTH + 1100).multiply(2));
		// 1.6
		gp.scaleYProperty().bind(primaryStage.heightProperty().divide(Global.WINDOW_HEIGHT + 1100).multiply(1.6)); // 1.6441789
		ScrollPane sp = new ScrollPane(Global.LINE_PANE);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		gp.setAlignment(Pos.CENTER);
		gp.getChildren().addAll(sp);
		return gp;
	}

	private void setBorderPane(Node bottom, Node left, Node center) {
		Global.setBottom(bottom);
		Global.setCenter(center);
		Global.setLeft(left);
	}

	private void initLinePane() {
		Global.addToLinePane(Global.LINES);
		Global.LINE_PANE.setAlignment(Pos.BOTTOM_CENTER);
		// just in case user entered in a length
		Global.draw();
	}

	private void setLegendTo(Legend newLegend) {
		legend.getChildren().clear();
		legend.getChildren().add(newLegend);
	}

	@Override
	public void stop() throws Exception {
		Platform.exit(); // safely shutdown javafx app
		System.exit(0); // stops every thread
	}

	public static void main(String[] args) {
		launch(args);
	}
}