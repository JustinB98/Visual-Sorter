package utility;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import menus.TopMenuBar;
import model.DataCollection;
import model.DataManager;
import model.MessageBox;
import model.RandomNumberGetter;
import model.VisualLine;

/**
 * Manager of the project
 */
public final class Global {

	/**
	 * if START.get() == true, then the sort has started</br>
	 * if START.get() == false, then there is no sort running
	 */
	public final static BooleanProperty START = new SimpleBooleanProperty(null, "start", false);

	/**
	 * if STOP.get() == true, then a signal has been sent to stop the sort</br>
	 * if STOP.get() == false, then the program is running normally, and a sort
	 * may or may not be happening</br>
	 * NOTE: START.get() and STOP.get() could be true at the same time. This
	 * will happen if the sort is running </br>
	 * and there has been a request to stop it
	 */
	public final static BooleanProperty STOP = new SimpleBooleanProperty(null, "stop", false);

	/**
	 * Width of the line pane
	 */
	public final static double WINDOW_WIDTH = 970;

	/**
	 * Height of the line pane
	 */
	public final static double WINDOW_HEIGHT = 600;

	/**
	 * Maximum number of lines
	 */
	public final static int MAX_LEN = 60;

	/**
	 * How many lines are being used. Only for when the program isn't using user
	 * data
	 */
	private static int currentLen = MAX_LEN;

	/**
	 * The main array for the entire project
	 */
	public final static VisualLine[] LINES = new VisualLine[MAX_LEN];

	/**
	 * The pane the holds all the lines in the line pane</br>
	 * Isn't directly put in the center of the root </br>
	 * Rather it's encapsulated in a scroll pane and then encapsulated in a grid
	 * pane
	 */
	public final static HBox LINE_PANE = new HBox(1);

	/**
	 * Handles some major data functions such as randomizes the set, but it's
	 * main function </br>
	 * is to handle user data
	 */
	public final static DataManager DATA_MANAGER = new DataManager();

	/**
	 * Handles the collection of user data but supplying a pane that collects
	 * {@link #MAX_LEN} amount of data </br>
	 * and also returns the user data to be handled by the {@link #DATA_MANAGER}
	 */
	public final static DataCollection DATA_COLLECTION = new DataCollection();

	/**
	 * Top menu bar
	 */
	public final static TopMenuBar TOP_BAR = new TopMenuBar();

	/**
	 * Main pane for the project
	 */
	public final static BorderPane ROOT = new BorderPane();

	/**
	 * Scaling of the height of the lines
	 */
	public final static double SIZE_MULTIPIER = 10;

	/**
	 * Scaling of the width of the lines </br>
	 * After inputting random numbers, 8.41 seemed to be the best
	 */
	public final static double SIZE_WIDTH_MULTIPIER = 8.41;

	/* Do not let anyone instantiate this class */
	private Global() {}

	static {
		// instantiate array elements
		for (int i = 0; i < MAX_LEN; ++i) {
			LINES[i] = new VisualLine(i * SIZE_MULTIPIER);
			LINES[i].setHeight(( MAX_LEN - i ) * SIZE_MULTIPIER);
			// after inputting random numbers, 8.41 seemed to be the best
			LINES[i].setStrokeWidth(SIZE_WIDTH_MULTIPIER); // 8.41
		}
		// reverseLines();
		// getting root ready
		ROOT.setTop(TOP_BAR);
		/* Getting info from user-pref file */
		int fileLen = FileUtility.getFileLen();
		if (Utility.constrainInt(fileLen, 0, MAX_LEN) == fileLen) {
			setCurrentLen(fileLen);
		}
		DATA_COLLECTION.setTo(FileUtility.getFileUserData());

		FileUtility.openDataFile();

		// init the message box that will warn a crash
		initMessageBox();
		initStyleClasses();
	}

	private static void initStyleClasses() {
		LINE_PANE.setId("line-pane");
	}

	private static void initMessageBox() {
		Platform.runLater(() -> {
			MessageBox messageBox = new MessageBox("Something went wrong!\nShutting down program...", Color.RED);
			messageBox.initModality(Modality.APPLICATION_MODAL);
			messageBox.setTitle("OOPS ERROR");
			Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
				e.printStackTrace();
				System.out.println(e.getMessage());
				System.err.println("Error");
				messageBox.show();
				Utility.stopTime(3000);
				System.exit(-1);
			});
		});
	}

	/**
	 * Sets the action of the mode menu item in the {@link #TOP_BAR}
	 * 
	 * @param e
	 *            event handler
	 */
	public static void setModeItemOnAction(EventHandler<ActionEvent> e) {
		TOP_BAR.getOptionsMenu().setModeItemOnAction(e);
	}

	/**
	 * Sets the lines to be completely unsorted</br>
	 * Also blacken the lines
	 */
	public static void reverseLines() {
		if (!START.get()) {
			// new edit
			DATA_MANAGER.notUsingUserData();
			for (int i = 0; i < currentLen; ++i) {
				LINES[i].setHeight(( currentLen - i ) * SIZE_MULTIPIER);
				LINES[i].setStroke(Color.BLACK);
			}
		}
	}

	/**
	 * Sets the lines to be sorted without using an algorithm </br>
	 * Also blackens the lines
	 */
	public static void sortLines() {
		if (!START.get()) {
			// new edit
			DATA_MANAGER.notUsingUserData();
			for (int i = 0; i < currentLen; ++i) {
				LINES[i].setHeight(( i + 1 ) * SIZE_MULTIPIER);
				LINES[i].setStroke(Color.BLACK);
			}
		}
	}

	/**
	 * @return the <tt>safe</tt> length of the array being used,
	 */
	public static int len() {
		return DATA_MANAGER.isUsingUserData() ? DATA_MANAGER.getLen() : currentLen;
	}

	/**
	 * Sets the length of the array being used. Will filter out new lengths that
	 * don't make sense. </br>
	 * After setting the new length, the array will be normalized
	 * 
	 * @param currentLen
	 *            new length
	 */
	public static void setCurrentLen(int currentLen) {
		// are we using user data? if false, then if the new currentLen is the
		// same as the old currentLen, then disregard it,
		// if true, then check to see if it's the same as the userDataLen. If
		// it's the same, then disregard it
		// all we're really doing is seeing if the passed in len is the same as
		// the current len. to do that we need to see what type of data we're
		// dealing with
		if (currentLen != Global.currentLen && !DATA_MANAGER.isUsingUserData()
				|| DATA_MANAGER.isUsingUserData() && currentLen != DATA_MANAGER.getLen()) {
			// DATA_MANAGER.notUsingUserData();
			Global.currentLen = Utility.constrainInt(currentLen, 0, MAX_LEN);
			// if we're not using user data, then we need to normalize the array
			if (!DATA_MANAGER.isUsingUserData()) {
				draw();
				// reverse lines to normalize the array
				reverseLines();
				// sortLines();
			}
		}
	}

	/**
	 * Renders the {@link #LINE_PANE} </br>
	 * Call this method when changing the values of the lines
	 */
	public static void draw() {
		Platform.runLater(() -> {
			int len = len();
			LINE_PANE.getChildren().clear();
			if (len == MAX_LEN) {
				LINE_PANE.getChildren().addAll(LINES);
			} else {
				for (int i = 0; i < len; ++i) {
					LINE_PANE.getChildren().add(LINES[i]);
				}
			}
		});
	}

	/**
	 * Sets all lines in a random order that will have it perfectly sorted</br>
	 * 
	 * @see {@link RandomNumberGetter}
	 */
	public static void randomize() {
		Platform.runLater(() -> {
			DATA_MANAGER.notUsingUserData();
			int len = len();
			RandomNumberGetter getter = new RandomNumberGetter(1, len + 1);
			for (int i = 0; i < len; ++i) {
				LINES[i].setHeight(getter.next() * Global.SIZE_MULTIPIER);
				LINES[i].setStroke(Color.BLACK);
			}
			// lines[0].setStroke(Color.CHOCOLATE);
			// lines[lines.length - 1].setStroke(Color.CHOCOLATE);
		});
		draw();
	}

	/**
	 * Adds nodes to the {@link #LINE_PANE}
	 * 
	 * @param nodes
	 *            nodes <tt>(var-args)</tt> to be added to the
	 *            {@link #LINE_PANE}
	 */
	public static void addToLinePane(Node... nodes) {
		LINE_PANE.getChildren().clear();
		LINE_PANE.getChildren().addAll(nodes);
	}

	/**
	 * Adds a single node to the {@link #LINE_PANE}
	 * 
	 * @param node
	 *            node to be added
	 */
	public static void addToLinePane(Node node) {
		LINE_PANE.getChildren().add(node);
	}

	/**
	 * @return root of the project, down-casted
	 * @see {@link #ROOT}
	 */
	public static Pane root() {
		return ROOT;
	}

	/**
	 * Sets the center of the {@link #ROOT}
	 * 
	 * @param center
	 *            node to be set as the center of the {@link #ROOT}
	 */
	public static void setCenter(Node center) {
		ROOT.setCenter(center);
	}

	/**
	 * Sets the bottom of the {@link #ROOT}
	 * 
	 * @param bottom
	 *            node to be set as the bottom of the {@link #ROOT}
	 */
	public static void setBottom(Node bottom) {
		ROOT.setBottom(bottom);
	}

	/**
	 * Sets the left of the {@link #ROOT}
	 * 
	 * @param left
	 *            node to be set as the left of the {@link #ROOT}
	 */
	public static void setLeft(Node left) {
		ROOT.setLeft(left);
	}

	/**
	 * Sets the right of the {@link #ROOT}
	 * 
	 * @param right
	 *            node to be set as the right of the {@link #ROOT}
	 */
	public static void setRight(Node right) {
		ROOT.setRight(right);
	}

	/**
	 * Sets the top of the {@link #ROOT}
	 * 
	 * @param top
	 *            node to be set as the top of the {@link #ROOT}
	 */
	public static void setTop(Node top) {
		ROOT.setTop(top);
	}

	/**
	 * Clears the {@link #LINE_PANE}
	 */
	public static void clearLinePane() {
		LINE_PANE.getChildren().clear();
	}

}