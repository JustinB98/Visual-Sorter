package utility;

import javafx.scene.input.KeyCode;

/**
 * Contains static fields for any keyboard actions needed throughout the program
 */
public final class KeyboardActions {

	/**
	 * Used to pause a sort
	 */
	public final static KeyCode PAUSE_KEY = KeyCode.P;

	/**
	 * Stops the current sort
	 */
	public final static KeyCode STOP_KEY = KeyCode.BACK_SPACE;

	/**
	 * Starts a new sort as long as there is not a sort currently ongoing
	 */
	public final static KeyCode START_KEY = KeyCode.ENTER;

	/**
	 * Clears any user data in the text fields
	 */
	public final static KeyCode CLEAR_DATA_KEY = KeyCode.C;

	/**
	 * Sends a signal to speed up the sort
	 */
	public final static KeyCode FAST_SORT_KEY = KeyCode.F;

	/**
	 * Randomizes the array
	 */
	public final static KeyCode RANDOMIZE_KEY = KeyCode.R;

	/**
	 * Sets user data
	 */
	public final static KeyCode SET_DATA_KEY = KeyCode.S;

	private KeyboardActions() {}

}
