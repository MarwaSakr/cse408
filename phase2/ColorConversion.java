import magick.*;
import magick.util.*;

public class ColorConversion
{
    public static double R=0.0, G=0.0, B=0.0;
    public static double XYZ_R=0.0, XYZ_G=0.0, XYZ_B=0.0;
    public static double XYZ_X=-1.0, XYZ_Y=-1.0, XYZ_Z=-1.0;

    public static double LAB_L=0.0, LAB_a=0.0, LAB_b=0.0;
    public static double LAB_X=0.0, LAB_Y=0.0, LAB_Z=0.0;

    public static double YIQ_Y = 0.0, YIQ_I = 0.0, YIQ_Q = 0.0;
    public static double YIQ_R = 0.0, YIQ_G = 0.0, YIQ_B = 0.0;

    public static double YUV_Y=0.0, YUV_U=0.0, YUV_V=0.0;
    public static double YUV_R = 0.0, YUV_G = 0.0, YUV_B = 0.0;

    public static double YCbCr_Y = 0.0, YCbCr_Cb = 0.0, YCbCr_Cr = 0.0;
    public static double YCbCr_R = 0.0, YCbCr_G = 0.0, YCbCr_B = 0.0;

    // Debug Info
    ///public static double xOld, yOld, zOld;

    // Works 100%
    public static void RGBtoXYZ(int red, int green, int blue)
    {
        XYZ_X = Math.floor(R * 0.4124 + G * 0.3576 + B * 0.1805);
        XYZ_Y = Math.floor(R * 0.2127 + G * 0.7152 + B * 0.0722); //0.2126
        XYZ_Z = Math.floor(R * 0.0193 + G * 0.1192 + B * 0.9505)+1.0; // added b/c of small err


        //System.out.println("RGB->XYZ: [" + XYZ_X + ", " + XYZ_Y + ", " + XYZ_Z + "].");
    }

    // Works
    public static void RGBtoYUV(int red, int green, int blue)
    {
        /*
         * Y = 0.299R + 0.587G + 0.114B
         * U'= (B-Y)*0.565
         * V'= (R-Y)*0.713
         */

        //YUV_Y = 0.299*red + 0.587*green + 0.144*blue;
        //YUV_U = (blue - YUV_Y) * 0.565;
        //YUV_V = (red - YUV_Y) * 0.713;

        YUV_Y = Math.floor(0.299 * red + 0.587 * green + 0.114 * blue);
        YUV_U = Math.floor(-0.299 * red - 0.587 * green + 0.886 * blue);
        YUV_V = Math.floor(0.701 * red - 0.587 * green - 0.114 * blue);

        //System.out.println("RGB->YUV: [" + YUV_Y + ", " + YUV_U + ", " + YUV_V + "].");
    }

    public static void RGBtoYIQ()
    {
        double Rt = R%256.0f - 0.5;
        double Gt = G%256.0f - 0.5;
        double Bt = B%256.0f - 0.5;

        YIQ_Y = Math.floor(0.299 * R + 0.587 * G + 0.114 * B);
        YIQ_I = Math.floor(0.595 * R - 0.274 * G - 0.322 * B);
        YIQ_Q = Math.floor(0.211 * R - 0.523 * G + 0.312 * B);

        //System.out.println("RGB->YIQ: [" + YIQ_Y + ", " + YIQ_I + ", " + YIQ_Q + "].");
    }

    // Conversion to full-range YCbCr
    public static void RGBtoYCbCr()
    {

        YCbCr_Y = Math.floor(0.299 * R + 0.587 * G + 0.114 * B);
        YCbCr_Cb = Math.floor(((-0.169)* R + (-0.331)*G + (0.500)*B)+128);
        YCbCr_Cr = Math.floor(((0.500)*R + (-0.419)*G + (-0.081)*B)+128);

        //System.out.println("RGB->YCbCr: [" + YCbCr_Y + ", " + YCbCr_Cb + ", " + YCbCr_Cr + "].");

    }

    public static void RGBtoHSL()
    {
        double H = 0, S = 0, L = 0, M, m, diff;
        double Rt = R/256;
        double Gt = G/256;
        double Bt = B/256;
        M = Math.max( Math.max(Rt, Gt), Bt);
        m = Math.min( Math.min(Rt ,Gt ), Bt);
        diff = M - m;

        L = (M + m )/2;

        if (diff == 0)
        {
                H = 0;
                S = 0;
        }

        else
        {
                if (L  < .5)
                {
                        S = diff / (M + m);
                }
                else
                {
                        S = diff / (2 - M - m);
                }

                double tempR = ( ( ( M - Rt ) / 6) + ( diff / 2) ) / diff;
                double tempG = ( ( ( M - Gt ) / 6) + ( diff / 2) ) / diff;
                double tempB = ( ( ( M - Bt ) / 6) + ( diff / 2) ) / diff;

                if (Rt == M) {H = tempB-tempG;}
                else if (Gt==M) {H = .3333 + tempR - tempB;}
                else if (Bt==M) {H = .6666 + tempG - tempR;}

                if(H < 0)
                        H +=1;
                if(H > 1)
                        H -=1;

                //H -= 1;

                H *=255;
                S *=255;
                L *=255;

                //System.out.println("RGB->HSL: [" + H + ", " + S + ", " + L + "].");

        }

    }

    // ------------------------------------------------------------------------

    // from book pg. 97 section 4.18
    public static void XYZtoRGB()
    {
        /*
         *  R’ = 3.240479 * X - 1.53715 * Y - 0.498535 * Z
            G’ = -0.969256 * X + 1.875991 * Y + 0.041556 * Z
            B’ = 0.055648 * X - 0.204043 * Y + 1.057311 * Z
         */

        XYZ_R = Math.floor(3.2405 * XYZ_X - 1.5372 * XYZ_Y - 0.4985 * XYZ_Z);
        XYZ_G = Math.floor(-0.9693 * XYZ_X + 1.876 * XYZ_Y + 0.0416 * XYZ_Z)+1.0; //added b/c of small err
        XYZ_B = Math.floor(0.0556 * XYZ_X - 0.2040 * XYZ_Y + 1.0573 * XYZ_Z);

        //System.out.println("XYZ->RGB: [" + XYZ_R + ", " + XYZ_G + ", " + XYZ_B + "].");


    }

    public static void XYZtoLAB(int red, int green, int blue)
    {

        final double Xn = 0.9504559271; //95.047; // Observer = 2, Illuminant = D65
        final double Yn = 1.00000; //100.000;
        final double Zn = 1.0890577508; //108.883;


        double x, y, z;

        x = XYZ_X/Xn;
        y = XYZ_Y/Yn;
        z = XYZ_Z/Zn;



        if (x > 0.008856)
            x = Math.pow(x, 0.333);
        else
            x = (7.787 * x) + (0.1379);

        if (y > 0.008856)
        {
            y = Math.pow(y, 0.333);
            //LAB_L = Math.floor((116 * y) - 16);
        }
        else
        {
            y = (7.787 * y) + (0.1379);
            //LAB_L = Math.floor(903.3*y);
        }

        if (z > 0.008856)
            z = Math.pow(z, 0.333);
        else
            z = (7.787 * z) + (0.1379);

        LAB_L = Math.floor(0.5*((1.160*y)-0.160+1.0));
        LAB_a = Math.floor(0.5*(5.000*(x-y)+1.0));
        LAB_b = Math.floor(0.5*(2.000*(y-z)+1.0));



        //System.out.println("XYZ->LAB: [" + LAB_L + ", " + LAB_a + ", " + LAB_b + "].");


    }

    public static void LABtoXYZ()
    {

        /*double Yn = (LAB_L + 16) / 116;
        double Xn = LAB_a / 500 + Yn;
        double Zn = yLab - LAB_b /200;

        if ( Math.pow( Yn , 3 ) > 0.008856 )
            yLab = Math.pow( Yn , 3 );
        else
            { y = ( y - 16 / 116 ) / 7.787; }
        if ( Math.pow( x , 3 ) > 0.008856 )
            { x = Math.pow( x , 3 ); }
        else
            { x = ( x - 16 / 116 ) / 7.787; }
        if ( Math.pow( z , 3 ) > 0.008856 )
            { z = Math.pow( z , 3 ); }
        else
            { z = ( z - 16 / 116 ) / 7.787; }*/

    }

    // Works
    public static void YUVtoRGB(int y, int u, int v)
    {
        /*
         * R = Y + 1.403V'
         * G = Y - 0.344U' - 0.714V'
         * B = Y + 1.770U'
         */

        YUV_R = Math.floor((double)y + 1.403*(double)v);
        YUV_G = Math.floor((double)y - 0.395*(double)u - 0.581*(double)v);
        YUV_B = Math.floor((double)y + 2.032*(double)u);

        //System.out.println("YUV->RGB: [" + YUV_R + ", " + YUV_G + ", " + YUV_B + "].");
    }

    public static void YIQtoRGB()
    {
        YIQ_R = Math.floor(1.0 * YIQ_Y + 0.956 * YIQ_I + 0.621 * YIQ_Q);
        YIQ_G = Math.floor(1.0 * YIQ_Y + -0.272 * YIQ_I + -0.647 * YIQ_Q);
        YIQ_B = Math.floor(1.0 * YIQ_Y + -0.523 * YIQ_I + 1.702 * YIQ_Q);

        //System.out.println("YIQ->RGB: [" + YIQ_R + ", " + YIQ_G + ", " + YIQ_B + "].");
    }

    // Conversion to full-range YCbCr
    public static void YCbCrtoRGB()
    {
        YCbCr_R = Math.floor((YCbCr_Y + 1.400*(YCbCr_Cr)+128));
        YCbCr_G = Math.floor((YCbCr_Y + (-0.343* (YCbCr_Cb)) + (-0.711*YCbCr_Cr)+128));
        YCbCr_B = Math.floor((YCbCr_Y + 1.765*(YCbCr_Cr))+128);

        //System.out.println("YCbCb->RGB: [" + YCbCr_R + ", " + YCbCr_G + ", " + YCbCr_B + "].");

    }




    // --- Set methods for red, green, and blue
    public static void setRed(int red)
    {
        R = red;
    }

    public static void setGreen(int green)
    {
        G = green;
    }

    public static void setBlue(int blue)
    {
        B = blue;
    }
}
