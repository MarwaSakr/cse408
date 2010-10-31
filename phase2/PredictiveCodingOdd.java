
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

            //temp = image.getOnePixel(0,0);
            //System.out.println("Red: " + (int) Math.floor(temp.getRed()/256));
            //System.out.println("Green: " + (int) Math.floor(temp.getGreen()/256));
            //System.out.println("Blue: " + (int) Math.floor(temp.getBlue()/256));

            //ColorConversion.RGBtoYUV((int) Math.floor(temp.getRed()/256), (int) Math.floor(temp.getRed()/256), (int) Math.floor(temp.getRed()/256));

            //System.out.println("Y: " + (int) ColorConversion.YUV_Y);
            //System.out.println("U: " + (int) ColorConversion.YUV_U);
            //System.out.println("Y: " + (int) ColorConversion.YUV_V);

            // do conversions by hand via ColorConversion class.
            for(int i = 0; i < signal.height; i++)
            {
                for(int j = 0; j < signal.width; j++)
                {
                    temp = image.getOnePixel(i, j);
                    ColorConversion.RGBtoYUV((int) Math.floor(temp.getRed()/256), (int) Math.floor(temp.getGreen()/256), (int) Math.floor(temp.getBlue()/256));

                    signal.Yorg[count] = (int) ColorConversion.YUV_Y;
                    signal.Uorg[count] = (int) ColorConversion.YUV_U;
                    signal.Vorg[count] = (int) ColorConversion.YUV_V;
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

        ColorConversion.YUVtoRGB(signal.Ynew[0], signal.Unew[0], signal.Vnew[0]);
        System.out.println(ColorConversion.YUV_R + " " + ColorConversion.YUV_G + " " + ColorConversion.YUV_B);

        // for each Y,U, V value, convert back to RGB, and put them in pixels[]
        for(int i = 0; i < signal.Ynew.length; i++)
        {
            ColorConversion.YUVtoRGB(signal.Ynew[i], signal.Unew[i], signal.Vnew[i]);
            pixels[i*3] = (byte) ColorConversion.YUV_R;
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
                signal.Ynew[j] = signal.Yorg[j];
                signal.Unew[j] = signal.Uorg[j];
                signal.Vnew[j] = signal.Vorg[j];
            }
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

        signal.Ynew[0] = signal.Yorg[0];
        signal.Ynew[1] = signal.Yorg[1];

        // fn(i, j) = (i-j)*width + (j-1)

        //for (int i = 0; i < height; i++)
        //{
            for (int j = 0; j < height*width; j++) //j < width
            {
                if (j == 0) // && i == 0
                {
                    j = 2;
                }

                // Do computation for Y bin first
                signal.Ynew[j] = (int) Math.floor((signal.Yorg[j-1] + signal.Yorg[j-2])/2);
                signal.Yerr[j] = signal.Yorg[j] - signal.Ynew[j];

                // Do computation for U bin
                signal.Unew[j] = (int) Math.floor((signal.Uorg[j-1] + signal.Uorg[j-2])/2);
                signal.Uerr[j] = signal.Uorg[j] - signal.Unew[j];

                // Do computation for V bin
                signal.Vnew[j] = (int) Math.floor((signal.Vorg[j-1] + signal.Vorg[j-2])/2);
                signal.Verr[j] = signal.Vorg[j] - signal.Vnew[j];
            }
        //}

    }

}
