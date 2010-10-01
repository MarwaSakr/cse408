import java.awt.Color;
import java.awt.Dimension;
import java.awt.Color;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;

public class Saturation {

	MagickImage current_image = null;
	ColorConversion conversion = new ColorConversion();
	double HSL[] = null;
	double HSV[] = null;
	float HSB[] = null;
	float RGB[] = null;

	public MagickImage saturate(int value, MagickImage image)
			throws MagickException {

		ImageInfo info = new ImageInfo("1.png");
		MagickImage imageInfo = new MagickImage(info);
		PixelPacket pixel = null;
		PixelPacket newPixel = null;
		Dimension imageDimension = image.getDimension();
		final int changeValue = 65536;

		int red = 0;
		int blue = 0;
		int green = 0;

		int imageWidth = (int) imageDimension.getWidth();
		int imageHeight = (int) imageDimension.getHeight();
		int[] pixels = new int[imageWidth * imageHeight * 3];
		int pixelCount = 0;

		int pixelWidthCount = 0;
		int pixelHeightCount = 0;

		pixel = image.getOnePixel(pixelWidthCount, pixelHeightCount);
		red = pixel.getRed();
		green = pixel.getGreen();
		blue = pixel.getBlue();

		for (pixelHeightCount = 0; pixelHeightCount < imageWidth; pixelHeightCount++) {
			for (pixelWidthCount = 0; pixelWidthCount < imageWidth; pixelWidthCount++) {

				pixel = image.getOnePixel(pixelWidthCount, pixelHeightCount);
				newPixel = changeSaturation(pixel, value);

				red = newPixel.getRed();
				green = newPixel.getGreen();
				blue = newPixel.getBlue();

				pixels[pixelCount++] = red * changeValue;
				pixels[pixelCount++] = green * changeValue;
				pixels[pixelCount++] = blue * changeValue;
			}
		}

	    image.constituteImage(pixelWidthCount, pixelHeightCount, "RGB", pixels);
	    return image;
	}

	private static PixelPacket changeSaturation(PixelPacket pixel, int value)	throws MagickException {
		int red = pixel.getRed() / 256;
		int green = pixel.getGreen() / 256;
		int blue = pixel.getBlue() / 256;

		float[] hsb = Color.RGBtoHSB(red, green, blue, null);
		float hue = hsb[0];
		float saturation = hsb[1];
		float brightness = hsb[2];
		float fDegree = (float) value;

		saturation = saturation + (saturation * (fDegree / 100f));
		if (saturation > 1) {
			saturation = 1;
		}

		int RGB = Color.HSBtoRGB(hue, saturation, brightness);

		Color c = new Color(RGB);

		pixel.setRed(c.getRed() * 256);
		pixel.setGreen(c.getGreen() * 256);
		pixel.setBlue(c.getBlue() * 256);

		return pixel;
	}
}