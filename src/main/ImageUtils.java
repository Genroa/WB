package main;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class ImageUtils {
	/**
	 * Converts a {@link BufferedImage} to a new {@link BufferedImage} in the
	 * given colorspace type (as defined by {@link BufferedImage} type
	 * constants). This can be used to convert images to grayscale, for
	 * instance.
	 * 
	 * @param image
	 *            the {@link BufferedImage} to convert
	 * @param newType
	 *            the new type
	 * @return a newly created {@link BufferedImage} copy of the given image,
	 *         converted in the given type
	 */
	final public static BufferedImage convertColorspace(BufferedImage image, int newType) {
		BufferedImage raw_image = image;
		image = new BufferedImage(raw_image.getWidth(), raw_image.getHeight(), newType);
		ColorConvertOp xformOp = new ColorConvertOp(null);
		xformOp.filter(raw_image, image);

		return image;
	}

	/**
	 * Provides a new {@link BufferedImage} with the given dimensions, from the
	 * given source {@link BufferedImage}
	 * @param source the original {@link BufferedImage}
	 * @param ratio the ratio between source image dimensions and new image dimensions
	 * @return  the newly created {@link BufferedImage} image
	 */
	final public static BufferedImage rescale(BufferedImage source, double ratio) {
		int w = (int) (source.getWidth() * ratio);
		int h = (int) (source.getHeight() * ratio);
		return rescale(source, w, h);
	}
	
	/**
	 * Provides a new {@link BufferedImage} with the given dimensions, from the
	 * given source {@link BufferedImage}
	 * 
	 * @param source
	 *            the original {@link BufferedImage}
	 * @param width
	 *            the thumbnail width
	 * @param height
	 *            the thumbnail height
	 * @return the newly created {@link BufferedImage} image
	 */
	final public static BufferedImage rescale(BufferedImage source, int width, int height) {
		BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bf.createGraphics();
		double xScale = ((double) width) / ((double) source.getWidth());
		double yScale = ((double) height) / ((double) source.getHeight());
		AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
		g2d.drawRenderedImage(source, at);
		g2d.dispose();
		return bf;
	}
}
