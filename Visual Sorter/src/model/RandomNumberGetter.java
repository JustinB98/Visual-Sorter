package model;

/**
 * A data structure for getting random numbers in a certain range
 */
public class RandomNumberGetter {

	private RandomNumber[] ranNumbers;
	private int used;

	/**
	 * 
	 * @param min
	 * @param max
	 *            not included
	 */
	public RandomNumberGetter(int min, int max) {
		ranNumbers = new RandomNumber[max - min];
		for (int i = min; i < max; ++i) {
			ranNumbers[i - min] = new RandomNumber(i);
		}
	}

	public int next() {
		if (isDone())
			throw new IndexOutOfBoundsException("All numbers are used!!");
		int index = getNextIndex();
		++used;
		return ranNumbers[index].getNumber();
	}

	private int getNextIndex() {
		if (!isDone()) { // avoiding infinite loop
			int index;
			do {
				index = getRandomIndex();
			} while (ranNumbers[index].isTaken());
			return index;
		}
		return -1;
	}

	private int getRandomIndex() {
		return (int) ( Math.random() * ranNumbers.length );
	}

	public boolean isDone() {
		return used == ranNumbers.length;
	}

}
