package menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import utility.Global;

public class DisplaySubMenu extends Menu {

	private MenuItem ascendingItem, decendingItem;

	public DisplaySubMenu() {
		super("Display");
	}

	{
		ascendingItem = new MenuItem("Ascending");
		ascendingItem.setOnAction(e -> Global.sortLines());
		decendingItem = new MenuItem("Decending");
		decendingItem.setOnAction(e -> Global.reverseLines());
		getItems().addAll(ascendingItem, decendingItem);
	}

}
