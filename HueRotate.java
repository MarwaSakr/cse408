import magick.*;
import magick.util.MagickViewer;
import java.awt.Frame;
import magick.ColorspaceType;
import java.awt.Dimension;



public class HueRotate {

	public static double rOut,gOut,bOut;
	public static double rIn,gIn,bIn;
	public static double h,s,v;
	public static int opacity;
	public static MagickImage output_Image= new MagickImage();

public static MagickImage rotateHue(MagickImage input_Image, int rotation){

        System.setProperty ("jmagick.systemclassloader" , "no");
        try {
			Dimension dimensions = input_Image.getDimension();
			byte[] newPixels = new byte[dimensions.width * dimensions.height * 4];
            int x=0;
            for(int i=0; i<dimensions.height; i++)
            {
				for (int j=0; j<dimensions.width;j++)
				{
					PixelPacket pixelPacket= input_Image.getOnePixel(j,  i);

					// Get the current RGB values
					rIn=pixelPacket.getRed();
					gIn=pixelPacket.getGreen();
					bIn=pixelPacket.getBlue();
					opacity=pixelPacket.getOpacity();

					// Convert RGB values to HSV and add rotation to H
					rgbToHSV(rIn,gIn,bIn,rotation);
					// rgbToHSV calls hsvToRGB after changing the h value

					newPixels[3*x] = (byte)rOut;
					newPixels[3*x+1] = (byte)gOut;
					newPixels[3*x+2] = (byte)bOut;
					newPixels[3*x+3] = (byte)255;
					x+=1;

				}
			} // end of nested for loop

			output_Image = new MagickImage();
			output_Image.constituteImage(dimensions.width, dimensions.height, "RGB", newPixels);

        } // end of try statement
		catch (MagickException ex){}
		System.out.println("RGB= "+rOut+", "+gOut+", "+bOut+"\n");
        return output_Image;
    } // end of rotateHue

    // beginning of rgbToHSV
    private static void rgbToHSV(double Rin,double Gin, double Bin, int rotation)
    {
		double diff;
		v=maxRGB(Rin,Gin,Bin);
		diff=(maxRGB(Rin,Gin,Bin)-minRGB(Rin,Gin,Bin));
		if (maxRGB(Rin,Gin,Bin)!=0)
		{
			s=diff/v;
		}
		else
		{
			s=0;
		}
		if (s!=0)
		{
			if (Rin==v)
			{
				h=((Gin-Bin)/diff);
			}
			else if (Gin==v)
			{
				h=((Rin-Bin)/diff)+2;
			}
			else if (Bin==v)
			{
				h=((Rin-Gin)/diff)+4;
			}
		}
		else
		{
			h=-7.0;
		}
		h*=60;
		h+=rotation;
		if (h<0.0)
		h+=360.0;
		else if(h>360.0)
		h-=360.0;

	}
	// end of rgbToHSV

	// Beginning of hsvToRGB
	// Converts HSV back into RGB
	private static void hsvToRGB()
	{
		double c=s*v;
		double hPrime=(h/60)%6;
		double x=c*(1-Math.abs(hPrime%2-1));
		double rTemp;
		double gTemp;
		double bTemp;
		double diff;

		if (h>=0 && h<1)
		{
			rTemp=c;
			gTemp=x;
			bTemp=0.0;
		}
		else if (h>=1 && h<2)
		{
			rTemp=x;
			gTemp=c;
			bTemp=0.0;
		}
		else if (h>=2 && h<3)
		{
			rTemp=0.0;
			gTemp=c;
			bTemp=x;
		}
		else if (h>=3 && h<4)
		{
			rTemp=0.0;
			gTemp=x;
			bTemp=c;
		}
		else if (h>=4 && h<5)
		{
			rTemp=x;
			gTemp=0.0;
			bTemp=c;
		}
		else
		{
			rTemp=c;
			gTemp=0.0;
			bTemp=x;
		}

		diff=v-c;

		rOut=rTemp+diff;
		gOut=gTemp+diff;
		bOut=bTemp+diff;
	}
	// end of hsvToRGB

	// Beginning of maxRGB
	private static double maxRGB(double Rin,double Gin, double Bin)
	{
		if (Rin>Gin)
		{
			if (Bin>Rin)
			{
				return Bin;
			}
			else
			{
				return Rin;
			}
		}
		else
		{
			if (Bin>Gin)
			{
				return Bin;
			}
			else
			{
				return Gin;
			}
		}
	}
	// end of max RGB

	// Beginning of minRGB
	private static double minRGB(double Rin, double Gin, double Bin)
	{
		if (Rin<Gin)
		{
			if (Bin<Rin)
			return Bin;
			else
			return Rin;
		}
		else
		{
			if (Bin<Gin)
			return Bin;
			else
			return Gin;
		}
	}

} // end of class HueRotate
