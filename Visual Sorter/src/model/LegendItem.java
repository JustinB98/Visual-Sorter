package model;

import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import utility.Utility;

public class LegendItem extends HBox {

	// tooltipText read-only
	private String title, tooltipText;
	private Rectangle legendSquare;
	private final static double SQUARE_SIZE = 10d;
	private final static double STROKE_WIDTH = 1.25;

	public LegendItem(String title, Paint color, String tooltipText) {
		this.title = Objects.requireNonNull(title, "Title can't be null!");
		this.tooltipText = Objects.requireNonNull(tooltipText);
		Objects.requireNonNull(color, "Color can't be null!");
		legendSquare.setFill(color);
		legendSquare.setStroke(Color.BLACK);
		legendSquare.setStrokeWidth(STROKE_WIDTH);
		initNode();
	}

	{
		legendSquare = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
		setSpacing(5);
		setAlignment(Pos.CENTER);
	}

	private void initNode() {
		Text textObj = new Text(title);
		getChildren().addAll(legendSquare, textObj);
		Utility.installTooltip(this, tooltipText);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTooltipText() {
		return tooltipText;
	}

	public Paint getColor() {
		return legendSquare.getFill();
	}

	public void setColor(Paint color) {
		legendSquare.setFill(color);
	}

}
