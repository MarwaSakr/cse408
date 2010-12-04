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
    //private static cHistogram hist;


    public static void setup(MagickImage[] inputImages, String colorSpace, int numbins) throws MagickException
    {
        int minVal = 0;
        int maxVal = 0;
        int numPixels = 0;
        PixelPacket[] pixels;
        int pixelCounter = 0;
        int imageCounter = 0;

        //hist = new cHistogram(numbins, colorSpace);
        int[] splitValues = new int[(numbins/2)-1];

        // -- Convert all images into proper colorspace
        if(colorSpace.equalsIgnoreCase("YUV"))
        {
            for (int i = 0; i < inputImages.length; i++)
            {
                inputImages[i].transformRgbImage(11);
                numPixels += (int) (inputImages[i].getXResolution() * inputImages[i].getYResolution());
            }
        }

        // -- Create an array of pixels from every image
        pixels = new PixelPacket[numPixels];

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


        // So when determining how many times to partition a color comp,
        // use 2^1. i is the number of times a color comp get's partitioned.
        // ex) numBins = 2: partition R 1, G/B 0.
        //numBins = 4: R/G 1, G 0.
        // numBins = 8: RGB twice.
        // 16: R 4, G/B 2
        // 32: R/G 4, B 2

        // DEBUG: 2 Bins
        // partition 1st color component into two. none for 2nd/3rd
        minVal = 0;
        maxVal = numPixels;
        int binCounter = 1;
        int redCounter = 0;
        int greenCounter = 0;
        int blueCounter = 0;
        int i;
        //debug:
        int split;
        int[] mins = new int[3]; // [color comp]
        int[] maxs = new int[3]; // [color comp]'s max

        i = (int) ((int) Math.log((double) numbins) / Math.log((double) 2));

        // sort the pixels so that you can easily partion the space
        colorSort.sort(pixels, 0);
        colorSort.sort(pixels, 1);
        colorSort.sort(pixels, 2);

        while(binCounter <= numbins)
        {
            /*if(redCounter < i) // Partition red
            {
                // find median point and split array
                splitValues[i] = (int) Math.ceil(maxVal/2); // max is the last split value.
                maxVal = splitValues[i];
            }

            if(greenCounter < (numbins - redCounter))
            {
                //
            }*/

            // we have 3 channels. if i = 1, then we part 1st. if i =2, then we
            // part 1st AND 2nd. if i = 3, then we part 1st, 2nd, AND 3rd. If i = 4,
            // then we part like previous but add another part to 1st...and so on.

            // How to do this? Red = 1, Green = 2, Blue = 3. loop: if i <= Blue, part B.
            // if i <= Green, part G. if i == Red, part R. else if i > i, part Red. (Do this backwards)

            /* Recursion might be the way to go here.
             * partition(min, max, sum)?
             *
             */
            int sum = 0;
            int c = 0;
            int[] temp = new int[3]; // This stores temp information about mins or maxes.
            splitValues[0] = numPixels; //when we use splitValues, we will start at index 1.
            while (sum < numbins)
            {
                System.out.println(i + " : sum[" + sum + "]  " + c + "/" + numbins);

                if(i >= 1 && sum < numbins) // Red Channel -
                {
                    if(i == 1) // Only Part Red Channel - 2 bins // if( (sum+2) == numbins)
                    {
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2);

                        // Add bin for lower partition
                        mins[0] = pixels[0].getRed();
                        maxs[0] = pixels[splitValues[c]].getRed();
                        mins[1] = pixels[0].getGreen();
                        maxs[1] = pixels[pixels.length].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);
                        
                        // Add bin for higher partition
                        mins[0] = pixels[splitValues[c]].getRed();
                        maxs[0] = pixels[pixels.length].getRed();
                        mins[1] = pixels[0].getGreen();
                        maxs[1] = pixels[pixels.length].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        sum += 2; //this is a break condition. This will exit the loop
                    } 
                    else // If we need to partition Red then move on.
                    {
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2);
                        mins[0] = pixels[0].getRed();
                        maxs[0] = pixels[splitValues[c]].getRed();
                        sum += 2;
                        c++;
                    } // Red portion complete. Waiting for Green/Blue.
                }
                if(i >= 2 && sum < numbins) // Green Channel - 4 bins
                {
                    if(i == 2)
                    {
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2); // need to get split of G values.

                        // Add bin for green's lower partition (green) [RLGLB]
                        mins[1] = pixels[0].getGreen();
                        maxs[1] = pixels[splitValues[c]].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for green's higher partition [RLGHB]
                        mins[1] = pixels[splitValues[c]].getGreen();
                        maxs[1] = pixels[0].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for red's higher, green's lower partition [RHGLB]
                        mins[0] = maxs[0]; // previous split value
                        maxs[0] = pixels[pixels.length].getRed();
                        mins[1] = pixels[0].getGreen();
                        maxs[1] = pixels[splitValues[c]].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for red's higher, green's higher partition [RHGHB]
                        mins[1] = pixels[splitValues[c]].getGreen();
                        maxs[1] = pixels[0].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        sum += 2;
                    } 
                    else // partition green then continue.
                    {
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2);
                        mins[1] = pixels[0].getGreen();
                        maxs[1] = pixels[splitValues[c]].getGreen();
                        
                        sum += 2;
                        c++;
                    }
                }
                if(i >= 3 && sum < numbins) // Blue Channel - 8 bins
                {
                    if(i == 3 || (sum+2) == numbins) // Only Blue Channel OR if this will
                    {
                        // Add bin for red's lower, green lower's, blue's lower partition [RLGLBL]
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2);
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[splitValues[c]].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for red's lower, green lower's, blue's higher partition [RLGLBH]
                        mins[2] = pixels[splitValues[c]].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // - Switch to green higher
                        temp[1] = mins[1]; // temp[G] = min[G]
                        mins[1] = maxs[1];
                        maxs[1] = pixels[pixels.length].getGreen();

                        // Add bin for green's higher, blue's lower partition [RLGHBL]
                        mins[1] = maxs[1];
                        maxs[1] = pixels[splitValues[c]].getGreen();
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[splitValues[c]].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for green's higher, blue's higher partition [RLGHBH]
                        mins[2] = pixels[splitValues[c]].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // - Switch to green's lower
                        maxs[1] = mins[1];
                        mins[1] = temp[1];

                        // - Switch to red's higher
                        mins[0] = maxs[0];
                        maxs[0] = pixels[splitValues[c]].getRed();

                        // Add bin for red's higher, green's lower, blue's lower partition [RHGLBL]
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[splitValues[c]].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for red's higher, green's lower, blue's higher partition [RHGLBH]
                        mins[2] = pixels[splitValues[c]].getBlue();
                        maxs[2] = pixels[pixels.length].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // - Switch to green's higher
                        mins[1] = temp[1];
                        //maxs[1] =
                        
                        // Add bin for red's higher, green's higher, blue's lower partition [RHGHBL]


                        // Add bin for red's higher, green's higher, blue's higher partition [RHGHBH]

                        sum += 2;
                    } else // Partition blue channel (l, h)
                    {
                        // Add bin for lower partition for prev R and G.
                        splitValues[c] = (int) Math.ceil(splitValues[c-1]/2);
                        mins[2] = pixels[0].getBlue();
                        maxs[2] = pixels[splitValues[c]].getBlue();
                        cHistogram.addBin(mins, maxs);

                        // Add bin for R_L G_

                        sum += 2;
                        c++;
                    }
                }

                // Since a loop has been fully completed, we will add a bin for
                // the "other" partitions.

                // Add bin for higher partition
                mins[2] = pixels[splitValues[c]].getBlue();
                maxs[2] = pixels[pixels.length].getBlue();
                cHistogram.addBin(mins, maxs);


                c++;
            }
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

    public void printInfo(int numbins)
    {
        for(int counter = 0; counter < numbins; counter++)
        {
            cHistogram.printBin(counter);
        }
    }


}
