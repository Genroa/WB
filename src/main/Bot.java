package main;

import java.awt.AWTException;
import java.awt.Robot;

import javax.xml.ws.WebServiceException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.sun.xml.internal.ws.Closeable;

/**
 * Cette classe représente le bot en lui-même. Les opérations de haut niveau
 * (charger un comportement par exemple) sont écrites ici.
 */
public class Bot implements Closeable {
	private final Robot robot;
	private final Screen screen;
	
	private Bot(Robot robot) throws AWTException {
		this.robot = robot;
		this.screen = Screen.getDefaultScreen();
	}
	
	
	public static Bot create() throws AWTException {
		try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
		return new Bot(new Robot());
	}


	@Override
	public void close() throws WebServiceException {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e) {}
	}
}
