package utility;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import model.VisualLine;

public class Utility {

	/**
	 * 100 Milliseconds
	 */
	public final static long DEFAULT_STOP_TIME = 100;

	private Utility() {}

	public static void swap(VisualLine l1, VisualLine l2) {
		double height = l1.getHeight();
		l1.setHeight(l2.getHeight());
		l2.setHeight(height);
		// to implement this method, a check needs to be made
		// l1.setHeight(l1.getHeight() + l2.getHeight());
		// l2.setHeight(l1.getHeight() - l2.getHeight());
		// l1.setHeight(l1.getHeight() - l2.getHeight());
	}

	/**
	 * Stops time with the default time
	 * 
	 * @see {@link #DEFAULT_STOP_TIME}
	 * @see {@link Thread#sleep(long)}
	 */
	public static void stopTime() {
		stopTime(DEFAULT_STOP_TIME);
	}

	/**
	 * Stops time for a specified amount of milliseconds
	 * 
	 * @param millis
	 *            time the current thread will sleep for
	 * 
	 * @see {@link Thread#sleep(long)}
	 */
	public static void stopTime(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}

	/**
	 * Installs a tooltip with the specified node
	 * 
	 * @param node
	 *            node that tooltip will be installed upon
	 * @param toolTipText
	 *            String that will be centered and installed on the node as a
	 *            tooltip object
	 * @see {@link javafx.scene.control.Tooltip}
	 */
	public static void installTooltip(Node node, String toolTipText) {
		Tooltip t = new Tooltip(toolTipText);
		t.setTextAlignment(TextAlignment.CENTER);
		Tooltip.install(node, t);
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void resetLineColors() {
		for (int i = 0; i < Global.len(); ++i) {
			Global.LINES[i].setStroke(Color.BLACK);
		}
	}

	public static double constrainDouble(double value, double min, double max) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		} else {
			return value;
		}
	}

	public static <T extends Comparable<T>> T constrain(T value, T min, T max) {
		if (value.compareTo(min) < 0) {
			return min;
		} else if (value.compareTo(max) > 0) {
			return max;
		} else {
			return value;
		}
	}

	public static int constrainInt(int value, int min, int max) {
		return (int) constrainDouble(value, min, max);
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static double[] convertListToArrayDouble(List<Double> list) {
		double[] arr = new double[list.size()];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	public static int[] convertListToArrayInt(List<Integer> list) {
		int[] arr = new int[list.size()];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = list.get(i);
		}
		return arr;
	}

}
