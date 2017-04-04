package main;

import java.awt.AWTException;

public class Main {
	public static void main(String[] args) throws AWTException {
		Screen screen = Screen.getDefaultScreen();
		screen.cutScreen(); // Fill "img" folder
		//screen.setMousePosition(7, 1);

		/*try (Bot b = Bot.create()) {
			// If you reach this line, you can do the bot behave and all,
			// everything went well. Resources will be closed when you reach the
			// end of this try-with-resources block.
		}*/
	}
}
