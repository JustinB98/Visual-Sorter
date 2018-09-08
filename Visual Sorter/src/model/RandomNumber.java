package model;

public class RandomNumber {

	private final int NUMBER;
	private boolean taken;

	public RandomNumber(int num) {
		NUMBER = num;
	}

	public boolean isTaken() {
		return taken;
	}

	public int getNumber() {
		taken = true;
		return NUMBER;
	}

}
