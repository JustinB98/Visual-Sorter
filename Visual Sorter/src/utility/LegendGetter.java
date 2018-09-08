package utility;

import javafx.scene.paint.Color;
import model.LegendItem;
import model.Legend;

public final class LegendGetter {

	public final static Legend bubbleSortLegend, selectionSortLegend, insertionSortLegend, mergeSortLegend,
			shellSortLegend, quickSortLegend;

	private LegendGetter() {}

	static {
		bubbleSortLegend = new Legend();
		selectionSortLegend = new Legend();
		insertionSortLegend = new Legend();
		mergeSortLegend = new Legend();
		shellSortLegend = new Legend();
		quickSortLegend = new Legend();
		initLegends();
	}

	private static void initLegends() {
		initBubbleSortLegend();
		initSelectionSortLegend();
		initInsertionSortLegend();
		initMergeSortLegend();
		initShellSortLegend();
		initQuickSortLegend();
	}

	private static void initBubbleSortLegend() {
		LegendItem parserItem = new LegendItem("Parser", Color.RED,
				"Runs through the array and brings\nthe highest element to the top of the array");
		LegendItem smallestItem = new LegendItem("Smaller", Color.GREEN,
				"When the two items are being compared,\nthe green one is the smaller of the two");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE, "Any element that is already sorted will be blue");
		bubbleSortLegend.addChildren(parserItem, smallestItem, sortedItem);
	}

	private static void initMergeSortLegend() {
		LegendItem parserItem = new LegendItem("Parser", Color.RED,
				"Makes a pass through the array\nto find the smallest element");
		LegendItem minimumItem = new LegendItem("Minimum", Color.GREEN, "The minimum element of the array");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE, "Any element that is arleady sorted will be blue");
		selectionSortLegend.addChildren(parserItem, minimumItem, sortedItem);
	}

	private static void initInsertionSortLegend() {
		LegendItem parserItem = new LegendItem("Parser", Color.RED,
				"Goes through the sorted part of\nthe array to place the new element");
		LegendItem divisorItem = new LegendItem("Divisor", Color.GREEN,
				"The divide of the array.\nLeft will be the sorted\nRight will be unsorted");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE,
				"The elements that are sorted.\n(Will happen at the end of the sort)");
		insertionSortLegend.addChildren(parserItem, divisorItem, sortedItem);
	}

	private static void initSelectionSortLegend() {
		LegendItem parserItem = new LegendItem("Parser", Color.RED,
				"Once a part of the array has been partitioned,\nThe parser scans through the new partition and will\nappear at the smaller of the two items in the array.\nAlso will replace the elements of the temp array to the original array");
		LegendItem midPointItem = new LegendItem("Midpoint", Color.GREEN, "The middle of the current partition");
		LegendItem endPointItem = new LegendItem("EndPoints", Color.CHOCOLATE, "The endpoints of the partition");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE,
				"Any element that is already sorted will be blue\n(Will happen at the end of the sort)");
		mergeSortLegend.addChildren(parserItem, midPointItem, endPointItem, sortedItem);
	}

	private static void initShellSortLegend() {
		LegendItem lowerBoundItem = new LegendItem("Lower", Color.GREEN, "The lower part of h");
		LegendItem upperBoundItem = new LegendItem("Upper", Color.CHOCOLATE, "The upper part of h");
		LegendItem parserItem = new LegendItem("Parser", Color.RED, "Swaps intervals/endpoints as needed");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE,
				"Any element that is already sorted will be blue\n(Will happen at the end of the sort)");
		shellSortLegend.addChildren(lowerBoundItem, upperBoundItem, parserItem, sortedItem);
	}

	private static void initQuickSortLegend() {
		LegendItem endPointsItem = new LegendItem("Endpoints", Color.CHOCOLATE, "Endpoints for recursion calls");
		LegendItem parserItem = new LegendItem("Parser", Color.RED,
				"Goes through the array forward and backwards\npivoting the subarray");
		LegendItem sortedItem = new LegendItem("Sorted", Color.BLUE,
				"Any element that is already sorted will be blue\n(Will happen at the end of the sort)");
		LegendItem swappedItem = new LegendItem("Swapped", Color.GREEN,
				"Any elements that are currently being swapped.\nCan be hard to see");
		quickSortLegend.addChildren(endPointsItem, parserItem, swappedItem, sortedItem);
	}

}
