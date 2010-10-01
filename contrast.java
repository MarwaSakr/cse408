import magick.ImageInfo;
import magick.MagickImage;
import magick.MagickException;
import magick.MagickApiException;
import magick.util.MagickWindow;
import magick.ColorspaceType;
import java.awt.Dimension;
import java.util.Scanner;

import magick.PixelPacket;

public class contrast {

    public static MagickImage changeLight(MagickImage image) {

try {
//Grab our image dimensions
Dimension dimensions = image.getDimension();
//Create our pixel arrays. One for old pixels, 1 for new
byte[] newPixels = new byte[dimensions.width * dimensions.height * 3];

System.out.println("enter contrast value (default:1 ; range 0-256) ");
Scanner input = new Scanner(System.in);
double k = input.nextDouble();

int x = 0;
for(int i = 0; i < dimensions.height; i++) {
for(int j = 0; j < dimensions.width; j++) {
PixelPacket pixel = image.getOnePixel(j,  i);
int red = pixel.getRed();
int green= pixel.getGreen();
int blue= pixel.getBlue();
red= (int)((red- 128)*k + 128);
green= (int)((green- 128)*k + 128);
blue= (int)((blue- 128)*k + 128);
if(red> 255) {
red= 255;
} else if( red< 0) {
red= 0;
}

if(green> 255) {
green= 255;
} else if(green< 0) {
green= 0;
}

if(blue> 255) {
blue= 255;
} else if(blue< 0) {
blue= 0;
}
//System.out.println("red: " + red+ " green: " + green+ " blue: " + blue);
newPixels[x] = (byte)red;
newPixels[x+1] = (byte)green;
newPixels[x+2] = (byte)blue;
x = x+3;
}
}

image = new MagickImage();
/*
* Create the new image with new pixels. Note that the
* color space isn't actually RGB, the image just needs to know
* if there are 3 or 4 components to a pixel. So RGB or RGBA (I think)
*/
image.constituteImage(dimensions.width, dimensions.height, "RGB", newPixels);

}
catch (MagickApiException ex) {
System.err.println("MagickException: " + ex + ": " + ex.getReason() + ", " + ex.getDescription());
ex.printStackTrace();
}
catch (MagickException ex) {
System.err.println("MagickException: " + ex);
ex.printStackTrace();
        }

return image;
}
}
