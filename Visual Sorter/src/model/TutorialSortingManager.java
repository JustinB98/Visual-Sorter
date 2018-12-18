package model;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import utility.GUIService;
import utility.Global;
import utility.Utility;

/**
 * To help with advanced sorting algorithms
 */
public class TutorialSortingManager {

	private static TutorialSortingManager instance = new TutorialSortingManager();
	
	private VisualLine[] lines = Global.LINES;
	private BooleanProperty stop = Global.STOP;
	private BooleanProperty start = Global.START;
	private StringProperty textProperty;
	private boolean pause = true;

	private TutorialSortingManager() {}

	public static TutorialSortingManager getInstance() {
		return instance;
	}
	
	public void setTextProperty(StringProperty text) {
		textProperty = text;
	}

	private int levelsOfMergeRecursion;

	public void mergeSort() {
		// Global.DATA_MANAGER.setData(new double[] { 5, 6, 15, 4, 6, 14, 6, 1,
		// 8, 5, 6, 10, 17 });
		double[] workSpace = new double[Global.len()];
		recMergeSort(workSpace, 0, Global.len() - 1);
		if (!stop.get())
			textProperty.set("It's sorted!!!");
		else
			textProperty.set("The tutorial was canceled...");
		// colorLines();
		levelsOfMergeRecursion = 0;
		reset();
	}

	private void reset() {
		Platform.runLater(() -> {
			stop.set(false);
			start.set(false);
		});
		draw();
	}

	private void draw() {
		Global.draw();
	}

	private void recMergeSort(double[] workSpace, int lowerBound, int upperBound) {
		++levelsOfMergeRecursion;
		draw();
		if (lowerBound == upperBound) { // special case, only 1 element
			pauseAndSetText("This is only one value, so, we say that it's sorted.");
			--levelsOfMergeRecursion;
			return;
		} else {
			// System.out.println(pause);
			if (!stop.get()) {
				int mid = ( upperBound + lowerBound ) / 2;
				printMergeInstructions(workSpace, lowerBound, mid, upperBound);
				merge(workSpace, lowerBound, mid + 1, upperBound);
				--levelsOfMergeRecursion;
			}
		}
	}

	private void printMergeInstructions(double[] workSpace, int lowerBound, int mid, int upperBound) {
		colorLines(lowerBound, upperBound, Color.GREEN);
		if (levelsOfMergeRecursion <= 1) {
			pauseAndSetText("Look at this area in green.");
		}
		colorLines(lowerBound, upperBound, Color.BLACK);
		colorLines(lowerBound, mid, Color.GREEN);
		if (levelsOfMergeRecursion <= 1) {
			pauseAndSetText("We need to sort this area, the lower half.");
		} else {
			pauseAndSetText(
					"But in order to do that we need to sort this area, so we'll need to call the recMergeSort method again. Levels: "
							+ levelsOfMergeRecursion + ".");
		}
		recMergeSort(workSpace, lowerBound, mid);
		colorLines(lowerBound, mid, Color.BLACK);
		colorLines(mid + 1, upperBound, Color.GREEN);
		pauseAndSetText("Now we need to sort this area, the upper half.");
		recMergeSort(workSpace, mid + 1, upperBound);
		colorLines(lowerBound, mid, Color.RED);
		colorLines(mid + 1, upperBound, Color.BLUE);
		pauseAndSetText("These two areas are ready to be merged and sorted.");
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
		colorLines(lowPtr, upperBound, Color.BLACK);
		int j = 0; // workspace index;
		final int lowerBound = lowPtr;
		final int mid = highPtr - 1;
		final int n = upperBound - lowerBound + 1; // # of items

		int lowPtrTemp = 0, highPtrTemp = 0;
		while (lowPtr <= mid && highPtr <= upperBound && !stop.get()) {
			lines[lowPtr].setStroke(Color.BLUE);
			lines[highPtr].setStroke(Color.RED);
			pauseAndSetText("We are comparing these values. (" + lines[lowPtr].getIntRealHeight() + ", "
					+ lines[highPtr].getIntRealHeight() + ")");
			int smaller;
			if (lines[lowPtr].compareTo(lines[highPtr]) < 0) {
				lowPtrTemp = lowPtr;
				lines[highPtr].setStroke(Color.BLACK);
				smaller = lines[lowPtr].getIntRealHeight();
				workSpace[j++] = lines[lowPtr++].getHeight();
			} else {
				highPtrTemp = highPtr;
				smaller = lines[highPtr].getIntRealHeight();
				lines[lowPtr].setStroke(Color.BLACK);
				workSpace[j++] = lines[highPtr++].getHeight();
			}
			pauseAndSetText("This value (" + smaller + ") is smaller, so we'll put it in the temp array.");
			lines[lowPtrTemp].setStroke(Color.BLACK);
			lines[highPtrTemp].setStroke(Color.BLACK);
		}

		int notInserted = 0;
		while (lowPtr <= mid && !stop.get()) {
			// Utility.stopTime(100);
			lines[lowPtr].setStroke(Color.BLUE);
			workSpace[j++] = lines[lowPtr++].getHeight();
			++notInserted;
		}

		while (highPtr <= upperBound && !stop.get()) {
			// Utility.stopTime(100);
			lines[highPtr].setStroke(Color.BLUE);
			workSpace[j++] = lines[highPtr++].getHeight();
			++notInserted;
		}
		// try to get text singular or plural
		String beginningInsertedText = notInserted == 1 ? "This value was" : "These values were";
		String middleInsertedText = notInserted == 1 ? "it is" : "they are";
		pauseAndSetText(beginningInsertedText + " not inserted into the temp array yet," + "\nBut we can say "
				+ middleInsertedText + " larger than any values in the array.");

		colorLines(lowerBound, upperBound, Color.BLACK);
		pauseAndSetText("Now, we'll move our values from our temp array to the original array.");
		if (!stop.get()) {
			fillArrayFromWorkSpace(workSpace, lowerBound, n);
		}
		colorLines(lowerBound, upperBound, Color.BLUE);
		// doesn't execute on the last recursive call
		if (levelsOfMergeRecursion != 1)
			pauseAndSetText("This area is now sorted. We are " + levelsOfMergeRecursion + " levels of recursion in.");
	}

	private void fillArrayFromWorkSpace(double[] workSpace, int lowerBound, int n) {
		for (int j = 0; j < n; ++j) {
			// lines[lowerBound + j] = workSpace[j];
			lines[lowerBound + j].setHeight(workSpace[j]);
			lines[lowerBound + j].setStroke(Color.RED);
			Utility.stopTime(10);
			lines[lowerBound + j].setStroke(Color.BLACK);
			draw();
		}
	}

	public void shellSort() {
		int inner, outer;
		double temp;
		int len = Global.len();
		int h = 1; // by default
		while (h <= len / 3) {
			h = h * 3 + 1;
		}
		pauseAndSetText("Our starting h is " + h);
		while (h > 0) { // decrement h, until h = 1
			for (outer = h; outer < len && !stop.get(); ++outer) {
				// boolean changed = false;
				temp = lines[outer].getHeight();
				lines[outer].setStroke(Color.RED);
				pauseAndSetText("We will declare a variable called temp,\nwhich will hold this value ("
						+ lines[outer].getIntRealHeight() + ")");
				lines[outer].setStroke(Color.BLACK);
				inner = outer;
				lines[outer].setStroke(Color.CHOCOLATE);
				int innerIndex = inner - h;
				lines[inner - h].setStroke(Color.GREEN);
				pauseAndSetText("We are comparing these points. (" + lines[inner - h].getIntRealHeight() + ", "
						+ lines[outer].getIntRealHeight() + ")");
				pauseAndSetText(lines[inner - h].compareTo(temp) > 0 && inner > h - 1 ? "We need to swap these"
						: "We don't need to do anything because they are in order");
				int whileLoops = 0;
				while (inner > h - 1 && lines[inner - h].compareTo(temp) > 0 && !stop.get()) {
					if (whileLoops > 0) {
						// lines[outer].setStroke(Color.BLACK);
						lines[inner].setStroke(Color.GREEN);
						lines[inner - h].setStroke(Color.RED);
						pauseAndSetText("Since inner > h - 1 (" + inner + " > " + ( h - 1 )
								+ ") and the red value is larger than the temp (" + (int) temp / Global.SIZE_MULTIPIER
								+ "),\nwe continue to swap");
					}
					++whileLoops;
					lines[inner].setStroke(Color.RED);
					lines[inner - h].setStroke(Color.BLACK);
					pauseAndSetText("We need to change change this value...");
					lines[inner].setStroke(Color.BLACK);
					lines[inner - h].setStroke(Color.RED);
					pauseAndSetText("To this value");
					lines[inner].setHeight(lines[inner - h].getHeight());
					lines[inner].setStroke(Color.BLACK);
					lines[inner - h].setStroke(Color.BLACK);
					inner -= h;
				} // end while
				if (whileLoops == 0) {
					lines[innerIndex].setStroke(Color.BLACK);
				}
				lines[inner].setStroke(Color.RED);
				lines[outer].setStroke(Color.BLACK);
				if (whileLoops >= 1) {
					pauseAndSetText("Now let's change our inner value to our temp ("
							+ (int) temp / Global.SIZE_MULTIPIER + ")");
				}
				lines[inner].setHeight(temp);
				lines[inner].setStroke(Color.BLACK);
				draw();
			} // end for
			String previousH = "(" + h + " - 1) / 3";
			h = ( h - 1 ) / 3; // decrease h
			pauseAndSetText(
					"We've reached the end of the outer loop, so let's change h " + previousH + "\nh is now " + h);
			if (h == 1)
				pauseAndSetText("Since h is 1, this is basically an insertion sort");

			draw();
		} // end while
		if (!stop.get()) {
			colorLines(0, len, Color.BLUE);
			textProperty.set("h is now less than or equal to zero, so, it's sorted!!");
		} else {
			textProperty.set("The tutorial was canceled...");
		}
		reset();
	} // end
		// shell
		// sort

	private int levelsOfQuickRecursion;

	public void quickSort() {
		recQuickSort(0, Global.len() - 1);
		if (!stop.get()) {
			colorLines(0, Global.len() - 1, Color.BLUE);
			textProperty.set("Sorted!");
		} else {
			textProperty.set("Tutorial was canceled...");
		}
		levelsOfQuickRecursion = 0;
		reset();
	}

	public void recQuickSort(int left, int right) {
		if (stop.get()) {
			return;
		}
		++levelsOfQuickRecursion;
		if (right - left <= 0) { // left >= right?
			pauseAndSetText("The left endpoint is greater than the right endpoint, so we're done here");
			--levelsOfQuickRecursion;
			colorLines(left, right, Color.BLACK);
			return; // left is greater than right, so we're done
		} else {
			colorLines(left, right, Color.GREEN);
			pauseAndSetText("We are looking at this area here. Recursion Levels: " + levelsOfQuickRecursion);
			colorLines(left, right, Color.BLACK);
			// lines[left].setStroke(Color.CHOCOLATE);
			// lines[right].setStroke(Color.CHOCOLATE);
			double pivot = lines[right].getHeight(); // pivot is the end element
			int partition = partitionIt(left, right, pivot);
			colorLines(left, partition - 1, Color.GREEN);
			pauseAndSetText("We've now partitioned the array, and found this to be our partitioning point\n"
					+ "We need to sort this area");
			recQuickSort(left, partition - 1); // partition is the new right
			colorLines(partition + 1, right, Color.GREEN);
			pauseAndSetText("Now that the lower part has been partitioned,\nWe can focus on the upper half");
			recQuickSort(partition + 1, right); // partition is the new left
			colorLines(left, right, Color.GREEN);
			pauseAndSetText("We've now partitioned and sorted this area. Recursion Levels: " + levelsOfQuickRecursion);
			colorLines(left, right, Color.BLACK);
			--levelsOfQuickRecursion;
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
				// innerLoopStop(MERGE_DELAY);
				lines[leftPtr].setStroke(Color.BLACK);
				lines[left].setStroke(Color.CHOCOLATE);
				lines[right].setStroke(Color.CHOCOLATE);
				draw();
			}
			while (rightPtr > left && lines[--rightPtr].compareTo(pivot) >= 0 && !stop.get()) {
				lines[rightPtr].setStroke(Color.RED);
				// innerLoopStop(MERGE_DELAY);
				lines[rightPtr].setStroke(Color.BLACK);
				lines[left].setStroke(Color.CHOCOLATE);
				lines[right].setStroke(Color.CHOCOLATE);
				draw();
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
		// innerLoopStop(MERGE_DELAY / 2);
		swap(leftPtr, indexToSwitch);
		// innerLoopStop(MERGE_DELAY / 2);
		lines[leftPtr].setStroke(Color.BLACK);
	}

	private void swap(int a, int b) {
		Utility.swap(lines[a], lines[b]);
	}

	@SuppressWarnings("unused")
	private void colorLineTemp(final int index, Paint color) {
		new GUIService(() -> {
			lines[index].setStroke(color);
			Utility.stopTime(10);
			lines[index].setStroke(Color.BLACK);
		}).start();
	}

	// should be called from a different thread than the current one sorting
	public void unpause() {
		pause = false;
	}

	private void pauseAndSetText(String text) {
		// textProperty.setValue("");
		textProperty.set("");
		textProperty.set(text);
		System.out.println(text);
		while (pause && !stop.get()) {
			// pause will change soon and we don't want pause to be set and used
			// at the same time, so a pause in the thread should help that
			Utility.stopTime(1);
		}
		pause = true;
	}

	// probably a useless method because stop can be accessed from the Global
	// class
	public void stop() {
		stop.set(true);
	}

	private void colorLines(int start, int end, Paint color) {
		for (int i = start; i <= end; ++i) {
			lines[i].setStroke(color);
		}
	}

}
