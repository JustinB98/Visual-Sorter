package model;

import javafx.scene.shape.Line;
import utility.Global;

public class VisualLine extends Line implements Comparable<VisualLine> {

	// not sure why it's .1 should be 0
	public final static double LINE_WIDTH = .1;
	private double height;

	public VisualLine(double height) {
		super(0, height, LINE_WIDTH, Global.WINDOW_HEIGHT);
		this.height = Global.WINDOW_HEIGHT - height;
	}

	public VisualLine deepCopy() {
		return new VisualLine(Global.WINDOW_HEIGHT - height);
	}

	@Override
	public int compareTo(VisualLine o) {
		return Double.compare(height, o.height);
	}

	public int compareTo(double o) {
		return Double.compare(height, o);
	}

	public void setHeight(double height) {
		setStartY(Global.WINDOW_HEIGHT - height);
		this.height = height;
	}

	public int getIntRealHeight() {
		return (int) ( height / Global.SIZE_MULTIPIER );
	}

	public double getDoubleRealHeight() {
		return height / Global.SIZE_MULTIPIER;
	}

	public double getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "height: " + height;
	}

}
