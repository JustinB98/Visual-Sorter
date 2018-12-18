package panes;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.SortingManager;
import model.TitledBorderPane;
import utility.Utility;

public class IterationPane extends Pane {

	private Text iterationText;

	{
		iterationText = new Text();
		refreshText(); // puts 0 
	}

	public IterationPane() {
		// adding a wrapper makes the text easier to control
		// if just adding the text by itself, then it gets too close to the
		// borders and there's no way to pad a single node unless putting it in a 
		// wrapper pane (at least, not that I know of)
		HBox textWrapper = new HBox(iterationText);
		TitledBorderPane sub = new TitledBorderPane("Iterations", textWrapper);
		getChildren().add(sub);
		initTooltip();
	}

	private void initTooltip() {
		Utility.installTooltip(this,
				"May be a little off.\nIt's should be mentioned that when we use O notation,"
				+ "\nwe generally overestimate.\nFor example, "
				+ "bubble/selection sort don't actually run for "
				+ "O(N\u00B2),\nbut it's easier to say "
				+ "than O(N + (N - 1) + (N - 2) + ...)");
	}

	public void refreshText() {
		String actualText = format("Actual", SortingManager.getInstance().getLastIterations());
		String exceptedText = format("Expected", SortingManager.getInstance().getExpectedIterations());
		iterationText.setText(exceptedText + "\n" + actualText);
	}

	private String format(String text, int i) {
		return text + " O(" + i + ")";
	}

}
