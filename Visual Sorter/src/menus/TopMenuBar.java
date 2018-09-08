package menus;

import javafx.scene.control.MenuBar;

public class TopMenuBar extends MenuBar {

	private OptionsMenu optionsMenu;
	private HelpMenu helpMenu;

	{
		optionsMenu = new OptionsMenu();
		helpMenu = new HelpMenu();
		getMenus().addAll(optionsMenu, helpMenu);
	}
	
	public OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}
	
	public void toggleModeItem() {
		optionsMenu.toggleModeItem();
	}

}