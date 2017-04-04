package main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Screen object is an object representing the analysis window of a bot : it
 * can retrieve parts of the screen, provide cell size and perform actions using
 * theses cells coordinates.
 */
public class Screen {

	private final int width;
	private final int height;

	private final int width_offset;
	private final int height_offset;

	private final int number_column;
	private final int number_lines;

	private static Robot robot;

	private final static Screen DEFAULT_SCREEN = Screen.createDefaultScreen();

	/**
	 * Standard constructor to create a new Screen. It is private (factory
	 * method is provided, and a Singleton is used. Use getDefaultScreen to get
	 * the default Screen you can use).
	 * 
	 * @param width
	 *            the screen width
	 * @param height
	 *            the screen height
	 * @param width_offset
	 *            the width offset of the analysis window
	 * @param height_offset
	 *            the height offset of the analysis window
	 * @param cellSize
	 *            the cellSize of the analysis window
	 */
	private Screen(int width, int height, int width_offset, int height_offset, int cellSize) {
		this.width = width;
		this.height = height;
		this.width_offset = width_offset;
		this.height_offset = height_offset;
		this.number_column = width / 128;
		this.number_lines = height / 128;
	}

	/**
	 * Static factory method to create the Singleton
	 * 
	 * @return
	 */
	private static Screen createDefaultScreen() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (AWTException e) {
				System.err.println("Couldn't create system Robot");
				System.exit(-1);
			}
		}
		int cellSize = 128;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width1 = screenSize.getWidth(); // Should be integer numbers
		double height1 = screenSize.getHeight(); // Should be integer numbers
		int width = (int) ((width1 * 0.90) / cellSize) * cellSize; // 80% screen
																	// size,
		// dividing by cellSize
		// and
		// multiplicating by
		// 128
		int height = (int) ((height1 * 0.90) / cellSize) * cellSize; // 80%
																		// screen
																		// size,
		// dividing by cellSize
		// and
		// multiplicating by
		// 128
		int height_offset = (int) (height1 - height) / 2; // Getting offsets
															// from
															// the
															// top left corner
															// of
															// the
															// screen
		int width_offset = (int) (width1 - width) / 2;

		return new Screen(width, height, width_offset, height_offset, cellSize);
	}

	/**
	 * Static method to get the default {@link Screen}
	 * 
	 * @return the default {@link Screen} object
	 */
	public static Screen getDefaultScreen() {
		return DEFAULT_SCREEN;
	}

	/**
	 * Returns a 32*32 grayscale {@link BufferedImage} of the [column, line]
	 * screen cell
	 * 
	 * @param column
	 *            the cell column
	 * @param line
	 *            the line column
	 * @return the new {@link BufferedImage} thumbnail
	 */
	public BufferedImage getAnalysableCell(int column, int line) {
		BufferedImage bufferedimage = getScreenCell(column, line);

		bufferedimage = ImageUtils.convertColorspace(ImageUtils.rescale(bufferedimage, 32, 32),
				BufferedImage.TYPE_BYTE_GRAY);
		return bufferedimage;
	}

	/**
	 * Returns a 128*128 {@link BufferedImage} of the [column, line] screen cell
	 * 
	 * @param column
	 *            the cell column
	 * @param line
	 *            the line column
	 * @return the new {@link BufferedImage} of the current screen cell value
	 */
	public BufferedImage getScreenCell(int column, int line) {
		if (column > number_column || column < 0 || line > number_lines || line < 0) {
			throw new IllegalArgumentException("Screen coordinate " + column + " " + line + " is invalid");
		}
		/*System.out.println(column + " " + line + " " + (width_offset + column * 128) + " "
				+ (height_offset + line * 128) + " (" + width_offset + "; " + height_offset + ")");*/
		Rectangle rectangle = new Rectangle(width_offset + column * 128, height_offset + line * 128, 128, 128);
		return robot.createScreenCapture(rectangle);
	}

	public BufferedImage getEntireScreen() {
		return robot.createScreenCapture(new Rectangle(width_offset, height_offset, number_column*128, number_lines*128));
	}

	/**
	 * Sets the mouse position to the given coordinates
	 * 
	 * @param column
	 * @param line
	 */
	public void setMousePosition(int column, int line) {
		// TODO MOVE OR REMOVE
		robot.mouseMove(width_offset + column * 128 + 64, height_offset + line * 128 + 64);
	}

	public void cutScreen() {
		// TODO RENAME
		try {
			for (int i = 0; i < number_column; i++) {
				for (int j = 0; j < number_lines; j++) {

					BufferedImage bf = getAnalysableCell(i, j);
					BufferedImage grayscaleImage = ImageUtils.convertColorspace(bf, BufferedImage.TYPE_BYTE_GRAY);
					File file = new File(
							"img" + File.separator + Integer.toString(i) + "_" + Integer.toString(j) + "." + "png");

					ImageIO.write(grayscaleImage, "png", file); // ignore
																// returned
																// boolean
				}
			}
		} catch (IOException e) {
			System.out.println("Write error: " + e.getMessage());
		}
	}
}
