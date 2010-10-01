
import java.awt.Dimension;
import java.util.Scanner;
import magick.MagickImage;
import magick.MagickException;
import magick.MagickApiException;
import magick.util.MagickWindow;
import magick.ColorspaceType;
import magick.ImageInfo;


import magick.PixelPacket;

public class contrast
{
	public static int applyContrast(int pixel,int c)
	{
		pixel = (pixel - 128)*c;
		pixel = pixel + 128;
		if(pixel> 255)
			pixel= 255;
		else if( pixel< 0)
			pixel = 0;
		return pixel;
	}

   public static MagickImage changeLight(MagickImage image)
   {
	   
	   try
	   {
		   int contrast_value = 1;
		   int red,blue,green = 0;
		   Dimension dimensions = image.getDimension();							// Image Dimensions
		   byte[] newPixels = new byte[dimensions.width * dimensions.height * 3];  // Create  pixel arrays
		   Scanner input = new Scanner(System.in);
		   System.out.println("Enter the light value (default:1 ; range -256 to 256) ");
		   contrast_value = input.nextInt();
		   int x = 0;
			for(int i = 0; i < dimensions.height; i++)								// loop through the image to pick pixel one by one
			{
				for(int j = 0; j < dimensions.width; j++)
				{
					PixelPacket pixel = image.getOnePixel(j,  i);
					red = pixel.getRed();										// get red pixel
					green= pixel.getGreen();									// get green pixel
					blue= pixel.getBlue();   									// get blue pixel

					// Apply contrast
					red = applyContrast(red,contrast_value);
					green= applyContrast(green,contrast_value);
					blue= applyContrast(blue,contrast_value);

					//set new pixels
					newPixels[x] = (byte)red;
					newPixels[x+1] = (byte)green;
					newPixels[x+2] = (byte)blue;
					x = x+3;
				}
			}

			image = new MagickImage();
			image.constituteImage(dimensions.width, dimensions.height, "RGB", newPixels);

		}
	   // Error Exception
		catch (MagickApiException ex)
		{

			System.out.println("MagickApiException: Catched ");
		}
		catch (MagickException ex)
		{

			System.out.println("MagickException: Catched ");
       	}

		return image;
	}
}