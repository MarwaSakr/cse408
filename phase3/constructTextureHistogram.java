import java.util.Scanner;
import java.util.InputMismatchException;
import magick.*;
import magick.util.*;
import java.lang.Math;

public class constructTextureHistogram
{
	public static int w,h,width,height;
	double D,phi;
	double[][] DX = {{-1.,0.,1},{-2.,0.,2},{-1.,0.,1}};
	double[][] DY = {{1.,2.,1},{0.,0.,0},{-1.,-2.,-1}};
	Matrix dx = new Matrix(DX);
	Matrix dy = new Matrix(DY);



	public static Histogram createTextureHistogram(GridLayout layout, MagickImage image,int xCoord, int yCoord) throws MagickException
	{
		Histogram texture = new Histogram(1,1,1);
		w = layout.width;
		h = layout.height;
		width = image.getDimension().width;
		height = image.getDimension().height;
		MagickImage copy = image.cloneImage(width,height,true);

		for (int widthCount = xCoord*width/w; widthCount < (xCoord+1)*width/w; widthCount++)
		{
			for (int heightCount = yCoord*height/h; heightCount <(yCoord+1)*height/h; heightCount++)
			{
				// Code will go here soon
			}
		}
		return texture;
	}

}