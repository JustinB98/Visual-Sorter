package model;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utility.FileUtility;

public class Legend extends Pane {

	private HBox legend;

	// working on proper constructor
	public Legend(LegendItem... items) {
		// addChildren(items);
	}

	public void addChildren(LegendItem... items) {
		legend.getChildren().clear();
		legend.getChildren().addAll(items);
		legend.setId("legend");
		TitledBorderPane titledBorderPane = new TitledBorderPane("Legend", legend);
		titledBorderPane.setTranslateY(8);
		// titledBorderPane.setPadding(new Insets(26,10,10,10));
		getChildren().add(titledBorderPane);
	}

	{
		legend = new HBox(10);
		// legend.setPadding(new Insets(26, 10, 10, 10));
		// legend.setAlignment(Pos.CENTER);
		// to be aligned with the control pane, 5, is a good value
		if (FileUtility.STYLE_FILE_EXISTS)
			legend.setTranslateY(5);
	}

}
