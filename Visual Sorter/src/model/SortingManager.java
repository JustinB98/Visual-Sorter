package model;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import utility.Global;
import utility.Utility;

/**
 * Terrible code lies here, please don't look
 */
public class SortingManager {

	private static SortingManager instance = new SortingManager();
	
	private VisualLine[] lines = Global.LINES;
	private BooleanProperty fastSort, stop, start;
	private boolean pause;

	private final int MERGE_DELAY = 100;

	private int lastIterations;
	private int expectedIterations;

	private SortingManager() {
		this.start = Global.START;
		this.stop = Global.STOP;
	}

	public void setFastSort(BooleanProperty fastSort) {
		this.fastSort = fastSort;
	}
	
	public static SortingManager getInstance() {
		return instance;
	}
	
	public int getLastIterations() {
		return lastIterations;
	}
	
	public int getExpectedIterations() {
		return expectedIterations;
	}
	
	public void selectionSort() {
		lastIterations = 0;
		int min; // index where the minimum value is found
		int len = Global.len();
		for (int out = 0; out < len - 1 && !stop.get(); out++) {
			min = out;
			outerLoopStop();
			for (int in = out + 1; in < len && !stop.get(); in++) {
				if (lines[in].compareTo(lines[min]) < 0) {
					min = in;
				}
				afterSelectionLoop(min, in);
				++lastIterations;
			}
			swap(out, min);
			lines[out].setStroke(Color.BLUE);
			draw();
		}
		lines[len - 1].setStroke(Color.BLUE);
		reset();
		// Platform.runLater(this::setHeights);
		expectedIterations = Global.len() * Global.len();
	}

	private void afterSelectionLoop(int min, int in) {
		lines[min].setStroke(Color.GREEN);
		lines[in].setStroke(Color.RED);
		innerLoopStop();
		// pauseIfNeeded();
		lines[in].setStroke(Color.BLACK);
		lines[min].setStroke(Color.BLACK);
	}

	public void bubbleSort() {
		lastIterations = 0;
		int len = Global.len();
		for (int out = len - 1; out >= 1 && !stop.get(); out--) {
			outerLoopStop();
			for (int in = 0; in < out && !stop.get(); in++) {
				lines[in].setStroke(Color.GREEN);
				lines[in + 1].setStroke(Color.RED);
				if (lines[in].compareTo(lines[in + 1]) > 0) {
					swap(in, in + 1);
				}
				// pauseIfNeeded();
				innerLoopStop();
				lines[in].setStroke(Color.BLACK);
				lines[in + 1].setStroke(Color.BLACK);
				++lastIterations;
			}
			lines[out].setStroke(Color.BLUE);
			draw();
		}
		lines[0].setStroke(Color.BLUE);
		reset();
		expectedIterations = Global.len() * Global.len();
		// Platform.runLater(this::setHeights);
	}

	public void insertionSort() {
		lastIterations = 0;
		int in;
		// int out; // first item not sorted, to the right of the divide
		final int len = Global.len();
		for (int out = 1; out < len && !stop.get(); out++) {
			double temp = lines[out].getHeight();
			boolean changed = false;
			in = out;
			lines[out].setStroke(Color.GREEN);
			outerLoopStop(1);
			// tried to check if lines[in - 1] was greater than or equal to temp
			// made the sorting slower because there are cases where the whole
			// set is just the same number
			while (in > 0 && lines[in - 1].compareTo(temp) > 0 && !stop.get()) {
				changed = true;
				in = innerInsertionSortWhileLoop(in, out);
				++lastIterations;
			}
			blackenLines(out); // without this, if most are in order, all the
								// lines would be green
			if (!changed) {
				++lastIterations;
				innerLoopStop();
			}
			lines[in].setHeight(temp);
			draw();
		}
		colorLines();
		reset();
		expectedIterations = Global.len() * Global.len();
		// Platform.runLater(this::setHeights);
	}

	/* Returns in */
	private int innerInsertionSortWhileLoop(int in, int out) {
		lines[in].setHeight(lines[in - 1].getHeight());
		blackenLines(out);
		lines[in - 1].setStroke(Color.RED);
		in--;
		innerLoopStop();
		// pauseIfNeeded();
		lines[in].setStroke(Color.BLACK);
		return in;
	}

	public void mergeSort() {
		int len = Global.len();
		lastIterations = 0;
		double[] workSpace = new double[Global.len()];
		recMergeSort(workSpace, 0, Global.len() - 1); // range that will be
														// sorted
		colorLines();
		reset();
		// https://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers
		expectedIterations = (int) (len * (Math.log(len) / Math.log(2)));
		System.out.println(expectedIterations);
		// System.out.println("Merge Sort: " + mergeSortN);
	}

	private void recMergeSort(double[] workSpace, int lowerBound, int upperBound) {
		if (lowerBound == upperBound) { // special case, only 1 element
			++lastIterations;
			return;
		} else {
			if (!stop.get()) {
				int mid = ( upperBound + lowerBound ) / 2;
				// lines[lowerBound].setStroke(Color.SIENNA);
				// lines[upperBound].setStroke(Color.SIENNA);
				recMergeSort(workSpace, lowerBound, mid); // sort lower half
				recMergeSort(workSpace, mid + 1, upperBound); // sort
																// right/higher
																// half
				lines[mid].setStroke(Color.GREEN);
				lines[lowerBound].setStroke(Color.CHOCOLATE);
				lines[upperBound].setStroke(Color.CHOCOLATE);
				// if (!stop.get()) {
				merge(workSpace, lowerBound, mid + 1, upperBound);
				// }
				lines[lowerBound].setStroke(Color.BLACK);
				lines[mid].setStroke(Color.BLACK);
				lines[upperBound].setStroke(Color.BLACK);
			}
		}
	}

	/**
	 * 
	 * @param workSpace
	 * @param lowPtr
	 *            Starting point of the lower half sub array
	 * @param highPtr
	 *            Starting point of the higher half of the sub array
	 * @param upperBound
	 *            The upper bound of the high half sub array
	 */
	private void merge(double[] workSpace, int lowPtr, int highPtr, int upperBound) {
		int j = 0; // workspace index;
		final int lowerBound = lowPtr;
		final int mid = highPtr - 1;
		final int n = upperBound - lowerBound + 1; // # of items

		while (lowPtr <= mid && highPtr <= upperBound && !stop.get()) {
			++lastIterations;
			// Utility.stopTime(100);
			if (lines[lowPtr].compareTo(lines[highPtr]) < 0) {
				lines[lowPtr].setStroke(Color.RED);
				innerLoopStop();
				// would be neater just to always set the lowerbound to
				// chocolate color all the time,
				// but that is pointless since it only needs to be done once
				if (lowerBound == lowPtr) {
					lines[lowPtr].setStroke(Color.CHOCOLATE);
				} else {
					lines[lowPtr].setStroke(Color.BLACK);
				}
				workSpace[j++] = lines[lowPtr++].getHeight();
			} else {
				lines[highPtr].setStroke(Color.RED);
				innerLoopStop();
				lines[highPtr].setStroke(Color.BLACK);
				workSpace[j++] = lines[highPtr++].getHeight();
			}
		}

		while (lowPtr <= mid && !stop.get()) {
			// Utility.stopTime(100);
			++lastIterations;
			workSpace[j++] = lines[lowPtr++].getHeight();
		}

		while (highPtr <= upperBound && !stop.get()) {
			// Utility.stopTime(100);
			++lastIterations;
			workSpace[j++] = lines[highPtr++].getHeight();
		}
		outerLoopStop();
		// if the user stopped at any time during this method,
		// some of the heights could have been changed
		// thus, losing some line heights altogether
		lines[upperBound].setStroke(Color.CHOCOLATE);
		if (!stop.get()) {
			fillInArrayFromWorkSpace(workSpace, lowerBound, n);
		}
	}

	private void fillInArrayFromWorkSpace(double[] workSpace, int lowerBound, int n) {
		for (int j = 0; j < n; ++j) {
			++lastIterations;
			// lines[lowerBound + j] = workSpace[j];
			lines[lowerBound + j].setHeight(workSpace[j]);
			// lines[lowerBound + j].setStroke(Color.PURPLE);
			lines[lowerBound + j].setStroke(Color.RED);
			innerLoopStop();
			if (j == 0) {
				lines[lowerBound].setStroke(Color.CHOCOLATE);
			} else {
				lines[lowerBound + j].setStroke(Color.BLACK);
			}
			draw();
		}
	}

	public void shellSort() {
		lastIterations = 0;
		int inner, outer;
		double temp;
		final int len = Global.len();
		int h = 1; // by default
		while (h <= len / 3) {
			h = h * 3 + 1;
		}
		while (h > 0) { // decrement h, until h = 1
			// System.out.println("H: " + h);
			for (outer = h; outer < len && !stop.get(); ++outer) {
				boolean changed = false;
				temp = lines[outer].getHeight();
				outerLoopStop(5);
				inner = outer;
				lines[outer].setStroke(Color.CHOCOLATE);
				int innerIndex = inner - h;
				lines[inner - h].setStroke(Color.GREEN);
				while (inner > h - 1 && lines[inner - h].compareTo(temp) > 0 && !stop.get()) {
					// if (changed && h != 1) break;
					changed = true;
					innerShellWhileLoop(inner, outer, h);
					inner -= h;
					++lastIterations;
				} // end while
					// if the shell sort didn't go through the while loop at
					// least once
					// do an innerLoopStop
				if (!changed) {
					innerLoopStop();
					lines[innerIndex].setStroke(Color.BLACK);
					++lastIterations;
				}
				lines[outer].setStroke(Color.BLACK);
				lines[inner].setHeight(temp);
				draw();
			} // end for
			h = ( h - 1 ) / 3; // decrease h
			draw();
		} // end while
		colorLines();
		reset();
		// System.out.println("Shell Sort O(" + shellSortN + ")");
		expectedIterations = (int) (len * (Math.pow(Math.log(len) / Math.log(2), 2)));
	} // end shell sort

	private void innerShellWhileLoop(int inner, int outer, int h) {
		lines[inner].setStroke(Color.RED);
		lines[outer].setStroke(Color.CHOCOLATE);
		lines[inner - h].setStroke(Color.GREEN);
		lines[inner].setHeight(lines[inner - h].getHeight());
		innerLoopStop();
		lines[inner].setStroke(Color.BLACK);
		lines[inner - h].setStroke(Color.BLACK);
	}

	public void quickSort() {
		final int len = Global.len();
		lastIterations = 0;
		recQuickSort(0, len - 1);
		// System.out.println("Quick Sort O(" + quickSortN + ")");
		colorLines();
		reset();
		expectedIterations = (int) (len * (Math.log(len) / Math.log(2)));
	}

	public void recQuickSort(int left, int right) {
		if (stop.get()) {
			++lastIterations;
			return;
		}
		if (right - left <= 0) { // left >= right?
			return; // left is greater than right, so we're done
		} else {
			// lines[left].setStroke(Color.CHOCOLATE);
			// lines[right].setStroke(Color.CHOCOLATE);
			double pivot = lines[right].getHeight(); // pivot is the end element
			int partition = partitionIt(left, right, pivot);
			outerLoopStop(10);
			recQuickSort(left, partition - 1); // partition is the new right
			innerLoopStop(20);
			recQuickSort(partition + 1, right); // partition is the new left
			innerLoopStop(20);
			lines[left].setStroke(Color.BLACK);
			lines[right].setStroke(Color.BLACK);
			++lastIterations;
		}
	}

	public int partitionIt(int left, int right, double pivot) {
		int leftPtr = left - 1;
		int rightPtr = right;
		// lines[left].setStroke(Color.CHOCOLATE);
		// lines[right].setStroke(Color.CHOCOLATE);
		while (true) {
			while (leftPtr < right && lines[++leftPtr].compareTo(pivot) < 0 && !stop.get()) {
				lines[leftPtr].setStroke(Color.RED);
				innerLoopStop(MERGE_DELAY);
				lines[leftPtr].setStroke(Color.BLACK);
				lines[left].setStroke(Color.CHOCOLATE);
				lines[right].setStroke(Color.CHOCOLATE);
				draw();
				++lastIterations;
			}
			while (rightPtr > left && lines[--rightPtr].compareTo(pivot) >= 0 && !stop.get()) {
				lines[rightPtr].setStroke(Color.RED);
				innerLoopStop(MERGE_DELAY);
				lines[rightPtr].setStroke(Color.BLACK);
				lines[left].setStroke(Color.CHOCOLATE);
				lines[right].setStroke(Color.CHOCOLATE);
				draw();
				++lastIterations;
			}
			if (leftPtr >= rightPtr) {
				break;
			} else {
				if (!stop.get()) {
					partitionSwap(leftPtr, right, left, rightPtr);
					draw();
				}
			}
		}
		// don't want a swap that will mess up the array
		if (!stop.get()) {
			partitionSwap(leftPtr, right, left, right);
		}
		draw();
		lines[right].setStroke(Color.BLACK);
		lines[left].setStroke(Color.BLACK);
		return leftPtr;
	}

	private void partitionSwap(int leftPtr, int right, int left, int indexToSwitch) {
		lines[leftPtr].setStroke(Color.GREEN);
		lines[right].setStroke(Color.GREEN);
		innerLoopStop(MERGE_DELAY / 2);
		swap(leftPtr, indexToSwitch);
		innerLoopStop(MERGE_DELAY / 2);
		lines[leftPtr].setStroke(Color.BLACK);
	}

	private void blackenLines(int divisorIndex) {
		for (int i = 0; i < divisorIndex; ++i) {
			lines[i].setStroke(Color.BLACK);
		}
	}

	private void colorLines() {
		int len = Global.len();
		for (int i = 0; i < len && !stop.get(); ++i) {
			lines[i].setStroke(Color.BLUE);
			Utility.stopTime(10);
		}
	}

	private void reset() {
		Platform.runLater(() -> {
			pause = true;
			stop.set(false);
			start.set(false);
		});
		draw();
	}

	// why not?
	private void draw() {
		Global.draw();
	}

	private void swap(int a, int b) {
		Utility.swap(lines[a], lines[b]);
	}

	private void outerLoopStop() {
		outerLoopStop(Utility.DEFAULT_STOP_TIME);
	}

	private void outerLoopStop(long millis) {
		// slows down the fast sort just a little
		if (fastSort.get()) {
			Utility.stopTime(millis);
		}
	}

	private void innerLoopStop(long millis) {
		if (!fastSort.get()) {
			Utility.stopTime(millis);
		} else {
			Utility.stopTime(1);
		}
		pauseIfNeeded();
	}

	private void innerLoopStop() {
		innerLoopStop(Utility.DEFAULT_STOP_TIME);
	}

	public void togglePause() {
		pause = !pause;
	}

	public void resumeNoMatterWhat() {
		pause = false;
	}

	public boolean isPaused() {
		return pause;
	}

	private void pauseIfNeeded() {
		while (pause) {
			// pause will change soon and we don't want pause to be set and used
			// at the same time, so a brief stop in the thread should help that
			Utility.stopTime(1);
		}
	}
}