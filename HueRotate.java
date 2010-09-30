import magick.*;
import magick.util.MagickViewer;
import java.awt.Frame;



public class HueRotate {

	static	double rOut,gOut,bOut;
	static double rIn,gIn,bIn;
	static double h,s,v;
	static int opacity;
	static MagickImage output_Image= new MagickImage();

public static MagickImage rotateHue(MagickImage input_Image, int rotation){


        System.setProperty ("jmagick.systemclassloader" , "no");
        try {
			output_Image.setXResolution(input_Image.getXResolution());
			output_Image.setYResolution(input_Image.getYResolution());
            for(int i=0; i<(int)input_Image.getXResolution(); i++)
            {
				for (int j=0; j<(int)input_Image.getYResolution();j++)
				{
					// Get the current RGB values
					rIn=(double)input_Image.getOnePixel(i,j).getRed();
					gIn=(double)input_Image.getOnePixel(i,j).getGreen();
					bIn=(double)input_Image.getOnePixel(i,j).getBlue();
					opacity=input_Image.getOnePixel(i,j).getOpacity();

					// Convert RGB values to HSV and add rotation to H
					rgbToHSV(rIn,gIn,bIn,rotation);

					PixelPacket pixelPacket = new PixelPacket((int)rOut,(int)gOut,(int)bOut,opacity);
					output_Image.getOnePixel(i,j).setOpacity(opacity);
					output_Image.getOnePixel(i,j).setRed((int)rOut);
					output_Image.getOnePixel(i,j).setGreen((int)gOut);
					output_Image.getOnePixel(i,j).setBlue((int)bOut);
				}
			} // end of nested for loop
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

	}
	// end of rgbToHSV

	// Beginning of hsvToRGB
	// Converts HSV back into RGB
	private static void hsvToRGB()
	{
		double c=s*v;
		double hPrime=(h/60)%6;
		double x=c*(1-Math.abs(hPrime%2-1));
		double rTemp=0.0;
		double gTemp=0.0;
		double bTemp=0.0;
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
		else if (h>=5 && h<6)
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
