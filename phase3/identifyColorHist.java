/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import magick.*;
import magick.util.*;
/**
 *
 * @author jvmilazz
 */
public class identifyColorHist {

    //Input: set of images (inputImages[]), colorSpace (string), histogram size (numbins)
    //Output: color-instance boundaries for each bin into file (files)
    protected static Histogram[] hist;


    public static void setup(MagickImage[] inputImages, String colorSpace, int numBins) throws MagickException
    {
        int numPixels = 0;


        //hist = new Histogram(numBins, minVal, maxVal);

        // --- Convert every image to YUV colorspace --- //
        if(colorSpace.equalsIgnoreCase("YUV"))
        {
            for (int i = 0; i < inputImages.length; i++)
            {
                inputImages[i].transformRgbImage(11);
                numPixels += (int) (inputImages[i].getXResolution() * inputImages[i].getYResolution());
            }
        }

        System.out.println("Num of pixels in set: " + numPixels);
        
        // --- Place every pixel in set into an array
        PixelPacket[] pixels = new PixelPacket[numPixels];
        int pixelCounter = 0;
        int imageCounter = 0;

        for(MagickImage image : inputImages)
        {
            for(int i = 0; i < image.getYResolution(); i++)
            {
                for(int j = 0; j < image.getXResolution(); j++)
                {
                    pixels[pixelCounter] = inputImages[imageCounter].getOnePixel(i, j);
                    pixelCounter++;
                }
            }
            imageCounter++;
        }

        /*for(int i = 0; i < 10; i++)
        {
            System.out.println(pixels[i].toString());
        }*/

        // --- Sort each pixel by Y, U, and V values.
        int min = 0;
        int max = pixels.length;
        int split;

        int R = 0, G = 1, B = 2;
        int[] minVal = new int[3];
        int[] maxVal = new int[3];
        PixelPacket[] temp;
        /*for(int i = 0; i < numBins; i++)
        {
            Redsort.sort(pixels);
            split = (int) Math.ceil(pixels.length/2);
        }*/

        split = (int) Math.ceil(pixels.length/2);
        temp = new PixelPacket[split];
        Redsort.sort(pixels);
            minVal[R] = pixels[0].getRed();
            maxVal[R] = pixels[split].getRed();

        // copy pixels into temp from minVal to maxVal
        split = (int) Math.ceil(pixels.length/2);
        Greensort.sort(temp);







        //Greensort.sort(pixels);
        //Bluesort.sort(pixels);

        /*for(int i = 0; i < 10; i++)
        {
            System.out.println(pixels[i].toString());
        }*/

        // --- Quantize to numBins bins
        //hist = new Histogram(numBins, minVal, maxVal);

        Quantize quantizizor = new Quantize(numBins);

        for(int i = 0; i < pixels.length; i++)
        {
            quantizizor.quantize(pixels[i].getRed(), Quantize.Y);
            quantizizor.quantize(pixels[i].getGreen(), Quantize.U);
            quantizizor.quantize(pixels[i].getBlue(), Quantize.V);
        }


        // --- Create an array of histograms for each bin --- //
        
        int[] minVals = quantizizor.getMinVal();
        int[] maxVals = quantizizor.getMaxVal();

        System.out.println(minVals.length);
        System.out.println(maxVals.length);

        hist = new Histogram[minVals.length];

        for(int i = 0; i < minVals.length; i++)
        {
            hist[i] = new Histogram(i, minVals[i], maxVals[i]);
        }




        
    }

    public static void printInfo()
    {
        for(int i = 0; i < hist.length; i++)
        {
            hist[i].printDescription();
        }
    }

    public void convertColorspace(MagickImage[] inputImages, String colorSpace) throws MagickException
    {
        if(colorSpace.equalsIgnoreCase("YUV"))
        {
            //get current colorspace, then call app function
            //debug:
            for (int i = 0; i < inputImages.length; i++)
            {
                inputImages[i].transformRgbImage(ColorspaceType.YUVColorspace);
            }
        }
    }


}
