import magick.*;
import magick.util.MagickViewer;
import java.awt.Frame;



public class HueRotate {

	static	double rOut,gOut,bOut;
	static double rIn,gIn,bIn;
	static double h,s,v;

public static MagickImage rotateHue(MagickImage output_Image, int rotation){
        System.setProperty ("jmagick.systemclassloader" , "no");    
        try {
            for(int i=0; i<(int)output_Image.getXResolution(); i++)
            {
				for (int j=0; j<(int)output_Image.getYResolution();j++)
				{
					rIn=output_Image.getOnePixel(i,j).getRed();
					gIn=output_Image.getOnePixel(i,j).getGreen();
					bIn=output_Image.getOnePixel(i,j).getBlue();
					rgbToHSV(rIn,gIn,bIn,rotation);
					output_Image.getOnePixel(i,j).setRed((int)rOut);
					output_Image.getOnePixel(i,j).setGreen((int)gOut);
					output_Image.getOnePixel(i,j).setBlue((int)bOut);
				}
			} // end of nested for loop
        } // end of try statement
		catch (MagickException ex){}
        return output_Image;
    } // end of rotateHue

    // beginning of calcHSV
    private static void rgbToHSV(double Rin,double Gin, double Bin, int rotation)
    {
		double diff;
		h=0.0;
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
			h=-1.0;
		}
		h*=60;
		h+=rotation;
		h=h%360;

	}
	// end of rgbToHSV

	// Beginning of hsvToRGB
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
