
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;


/**
 *
 * @author jvmilazz
 */
public class PredictiveCodingOdd {

    public static YUVSignal createSignal(MagickImage image)
    {

        // Read in pixels row-by-row, convert from RGB to YUV, store in YUVSingal
        YUVSignal signal = null;
        PixelPacket temp = new PixelPacket(0,0,0,0);
        int count = 0;

        try {
            //System.out.println(image.getDimension().width + " X " + image.getDimension().height);

            signal = new YUVSignal(image.getDimension().width, image.getDimension().height);

            // do conversions by hand via ColorConversion class.
            for(int i = 0; i < signal.height; i++)
            {
                for(int j = 0; j < signal.width; j++)
                {
                    temp = image.getOnePixel(j, i);
                    ColorConversion.RGBtoYUV((int) Math.floor(temp.getRed()/256), (int) Math.floor(temp.getGreen()/256), (int) Math.floor(temp.getBlue()/256));

                    signal.Yorg[count] = (int) ColorConversion.YUV_Y;
                    signal.Uorg[count] = (int) ColorConversion.YUV_U;
                    signal.Vorg[count] = (int) ColorConversion.YUV_V;
                    
                    signal.Ynew[count] = signal.Yorg[count];
                    signal.Unew[count] = signal.Uorg[count];
                    signal.Vnew[count] = signal.Vorg[count];
                    count++;

                }
            }

        } catch (MagickException ex) {
            System.err.println(ex.toString());
        }

        return(signal);
    }

    // Need to convert back to RGB before creating
    public static MagickImage constructImage(YUVSignal signal) throws MagickException
    {
        MagickImage newImage = new MagickImage();
        byte pixels[] = new byte[signal.height*signal.width*3]; // no alpha channel
        
        if (signal.predictiveCodingFlag != 1)
        {
            for (int i = 1; i < signal.Ynew.length; i++)
            {
                signal.Ynew[i] += signal.Yerr[i];
            }
        }

        // for each Y,U, V value, convert back to RGB, and put them in pixels[]
        for(int i = 0; i < signal.Ynew.length; i++)
        {
            ColorConversion.YUVtoRGB(signal.Ynew[i], signal.Unew[i], signal.Vnew[i]);
            pixels[i*3] = (byte)ColorConversion.YUV_R;
            pixels[i*3 + 1] = (byte) ColorConversion.YUV_G;
            pixels[i*3 + 2] = (byte) ColorConversion.YUV_B;
        }

        newImage.constituteImage(signal.width, signal.height, "RGB", pixels);
        return (newImage);
    }


    public static void printSignal(YUVSignal signal)
    {
        System.out.println("\t\tOld YUV Signal");
        System.out.println("Y\t\tU\t\tV");
        for (int i = 0; i < signal.Yorg.length; i++)
        {
            System.out.println(signal.Yorg[i] + "\t\t" + signal.Uorg[i] + "\t\t" + signal.Vorg[i]);
        }

        System.out.println("\t\t____________________________________");
        System.out.println("\t\tNew YUV Signal");
        System.out.println("Y\t\tU\t\tV");
        for (int i = 0; i < signal.Yorg.length; i++)
        {
            System.out.println(signal.Yorg[i] + "\t\t" + signal.Uorg[i] + "\t\t" + signal.Vorg[i]);
        }

        System.out.println("\t\t____________________________________");
        System.out.println("\t\tYUV Error Signal");
        System.out.println("Y\t\tU\t\tV");
        for (int i = 0; i < signal.Yorg.length; i++)
        {
            System.out.println(signal.Yerr[i] + "\t\t" + signal.Uerr[i] + "\t\t" + signal.Verr[i]);
        }

    }


    public static void noPC(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                signal.Ynew[i*width +j] = signal.Yorg[i*width +j];
                signal.Unew[i*width +j] = signal.Uorg[i*width +j];
                signal.Vnew[i*width +j] = signal.Vorg[i*width +j];
                
                signal.Yquant[i*width + j] = (float) signal.Ynew[i*width + j];
                signal.Uquant[i*width + j] = (float) signal.Unew[i*width + j];
                signal.Vquant[i*width + j] = (float) signal.Vnew[i*width + j];
            }
        }

    }
     /*
	 * Encode with predictors
	 * Fn = floor (fn-1)
	 * En = fn - Fn
     */
    public static void differentialPC2(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

        for (int i=0; i<1; i++)
        {
			signal.Ynew[i]=signal.Yorg[i];
			signal.Unew[i]=signal.Uorg[i];
			signal.Vnew[i]=signal.Vorg[i];
			
			signal.Yquant[i] = (float) signal.Yorg[i];
			signal.Uquant[i] = (float) signal.Uorg[i];
			signal.Vquant[i] = (float) signal.Vorg[i];
		}

        for (int j = 1; j < height*width; j++) //j < width
        {

            // Do computation for Y bin first
            //signal.Ynew[j] = (int) Math.floor(signal.Yorg[j-1]);
            signal.Yquant[j] = (float) Math.floor(signal.Yorg[j-1]);
            signal.Ynew[j] = (int) signal.Ynew[j];
            signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

            // Do computation for U bin
            //signal.Unew[j] = (int) Math.floor(signal.Uorg[j-1]);
            signal.Uquant[j] = (float) Math.floor(signal.Uorg[j-1]);
            signal.Unew[j] = (int) signal.Unew[j];
            signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

            // Do computation for V bin
            //signal.Vnew[j] = (int) Math.floor(signal.Vorg[j-1]);
            signal.Vquant[j] = (float) Math.floor(signal.Vorg[j-1]);
            signal.Vnew[j] = (int) signal.Vnew[j];
            signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
        }


    }

    /*
     * Encode with predictors
     * Fn = floor ( (fn-1 + fn-2)/2 )
     * En = fn - Fn
    */ 
    public static void differentialPC3(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

        for (int i=0; i<2; i++)
        {
			signal.Ynew[i]=signal.Yorg[i];
			signal.Unew[i]=signal.Uorg[i];
			signal.Vnew[i]=signal.Vorg[i];
			
			signal.Yquant[i] = (float) signal.Yorg[i];
			signal.Uquant[i] = (float) signal.Uorg[i];
			signal.Vquant[i] = (float) signal.Vorg[i];
		}

        for (int j = 2; j < height*width; j++) //j < width
        {
            // Do computation for Y bin first
            //signal.Ynew[j] = (int) Math.floor((signal.Yorg[j-1] + signal.Yorg[j-2])/2);
            signal.Yquant[j] = (float) Math.floor((signal.Yorg[j-1] + signal.Yorg[j-2])/2);
            signal.Ynew[j] = (int) signal.Yquant[j];
            signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

            // Do computation for U bin
            //signal.Unew[j] = (int) Math.floor((signal.Uorg[j-1] + signal.Uorg[j-2])/2);
            signal.Uquant[j] = (float) Math.floor((signal.Uorg[j-1] + signal.Uorg[j-2])/2);
            signal.Unew[j] = (int) signal.Uquant[j];
            signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

            // Do computation for V bin
            //signal.Vnew[j] = (int) Math.floor((signal.Vorg[j-1] + signal.Vorg[j-2])/2);
            signal.Vquant[j] = (float) Math.floor((signal.Vorg[j-1] + signal.Vorg[j-2])/2);
            signal.Vnew[j] = (int) signal.Vquant[j];
            signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
        }
    }
    
 	/*
     * Encode with predictors
     * Fn = floor ( (2*fn-1 + fn-2)/3 )
     * En = fn - Fn
     */
    public static void differentialPC4(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

		for (int i=0; i<2; i++)
        {
			signal.Ynew[i]=signal.Yorg[i];
			signal.Unew[i]=signal.Uorg[i];
			signal.Vnew[i]=signal.Vorg[i];
			
			signal.Yquant[i] = (float) signal.Yorg[i];
			signal.Uquant[i] = (float) signal.Uorg[i];
			signal.Vquant[i] = (float) signal.Vorg[i];
		}

        for (int j = 2; j < height*width; j++) //j < width
        {

            // Do computation for Y bin first
            signal.Yquant[j] = (float) Math.floor(2*signal.Yorg[j-1] + signal.Yorg[j-2])/3;
            signal.Ynew[j] = (int) signal.Yquant[j];
            signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

            // Do computation for U bin
            signal.Uquant[j] = (float) Math.floor(2*signal.Uorg[j-1] + signal.Uorg[j-2])/3;
            signal.Unew[j] = (int) signal.Uquant[j];
            signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

            // Do computation for V bin
            signal.Vquant[j] = (float) Math.floor(2*signal.Vorg[j-1] + signal.Vorg[j-2])/3;
            signal.Vnew[j] = (int) signal.Vquant[j];
            signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
        }


    }
 	/*
     * Encode with predictors
     * Fn = floor ( (fn-1 + 2*fn-2)/3 )
     * En = fn - Fn
     */
    public static void differentialPC5(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

        for (int i=0; i<2; i++)
        {
			signal.Ynew[i]=signal.Yorg[i];
			signal.Unew[i]=signal.Uorg[i];
			signal.Vnew[i]=signal.Vorg[i];
			
			signal.Yquant[i] = (float) signal.Yorg[i];
			signal.Uquant[i] = (float) signal.Uorg[i];
			signal.Vquant[i] = (float) signal.Vorg[i];
		}


        for (int j = 2; j < height*width; j++) //j < width
        {

            // Do computation for Y bin first
            signal.Yquant[j] = (float) Math.floor((signal.Yorg[j-1] + (2*signal.Yorg[j-2]))/3);
            signal.Ynew[j] = (int) signal.Yquant[j];
            signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

            // Do computation for U bin
            signal.Uquant[j] = (float) Math.floor((signal.Uorg[j-1] + (2*signal.Uorg[j-2]))/3);
            signal.Unew[j] = (int) signal.Uquant[j];
            signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

            // Do computation for V bin
            signal.Vquant[j] = (float) Math.floor((signal.Vorg[j-1] + (2*signal.Vorg[j-2]))/3);
            signal.Vnew[j] = (int) signal.Vquant[j];
            signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
        }


    }

    /*
     * Encode with predictors
     * Fn = floor ( (fn-1 + fn-2 + ... + fn-10)/10 )
     * En = fn - Fn
     */
    public static void differentialPC6(YUVSignal signal) throws MagickException
    {
        int height = signal.height;
        int width = signal.width;

        for (int i=0; i<9; i++)
        {
			signal.Ynew[i] = signal.Yorg[i];
			signal.Unew[i] = signal.Uorg[i];
			signal.Vnew[i] = signal.Vorg[i];
			
			signal.Yquant[i] = (float) signal.Yorg[i];
			signal.Uquant[i] = (float) signal.Uorg[i];
			signal.Vquant[i] = (float) signal.Vorg[i];
		}

        for (int j = 10; j < height*width; j++) //j < width
        {

			// Do computation for Y bin first
            signal.Yquant[j] = (float) Math.floor((signal.Yorg[j-1] + signal.Yorg[j-2]
            + signal.Yorg[j-3] + signal.Yorg[j-4] + signal.Yorg[j-5] + signal.Yorg[j-6]
            + signal.Yorg[j-7] + signal.Yorg[j-8]+signal.Yorg[j-9] + signal.Yorg[j-10])/10);
            signal.Ynew[j] = (int) signal.Yquant[j];
            signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

            // Do computation for U bin
            signal.Uquant[j] = (float) Math.floor((signal.Uorg[j-1] + signal.Uorg[j-2]
			+ signal.Uorg[j-3] + signal.Uorg[j-4] + signal.Uorg[j-5] + signal.Uorg[j-6]
			+ signal.Uorg[j-7] + signal.Uorg[j-8]+ signal.Uorg[j-9] + signal.Uorg[j-10])/10);
			signal.Unew[j] = (int) signal.Uquant[j];
            signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

            // Do computation for V bin
            signal.Vquant[j] = (float) Math.floor((signal.Vorg[j-1] + signal.Vorg[j-2]
			+ signal.Vorg[j-3] + signal.Vorg[j-4] + signal.Vorg[j-5] + signal.Vorg[j-6]
			+ signal.Vorg[j-7] + signal.Vorg[j-8]+ signal.Vorg[j-9] + signal.Vorg[j-10])/10);
			signal.Vnew[j] = (int) signal.Vquant[j];
            signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
        }
    }

}
