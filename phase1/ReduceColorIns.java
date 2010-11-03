/*********************************************************************
 * Sebum Lee
 * 1000672044
 * CSE408 Phase 1
 * Reduce the number of color instances in the image to 8 using 
 * a 3-bit color table created in the color space of the user's choice.
 **********************************************************************/


import magick.*;
import magick.util.MagickWindow;

public class ReduceColorIns {
	
	MagickImage current_image = null;
	ImageInfo info;
	PixelPacket[] image_pixels;
	PixelPacket[] new_image_pixels;
	byte[] pixels;
	int[][] colorTable = new int[8][3];
	int width, height;
	int size;
	static int med_Red;
	static int med_Green;
	static int med_Blue;
	static int low_Red;
	static int low_Green;
	static int low_Blue;
	static int high_Red;
	static int high_Green;
	static int high_Blue;
	
	public ReduceColorIns(String image) throws MagickException{
		info = new ImageInfo(image);
		current_image = new MagickImage(info);
		width = current_image.getDimension().width;
		height = current_image.getDimension().height;
		size = width * height;
		image_pixels = new PixelPacket[size];
		new_image_pixels = new PixelPacket[size];				
	}
	
	public void copyPixels() throws MagickException {
		int count = 0;
		
		for(int i=0; i< height; i++) {
			for(int j=0; j<width; j++) {
				image_pixels[count] = current_image.getOnePixel(j,i);
				new_image_pixels[count] = current_image.getOnePixel(j,i);				
				count++;				
			}			
		}		
	}
	
	public void convertColorSpace(int option) throws MagickException {
		int R, G, B;
		int XYZ_X, XYZ_Y, XYZ_Z;
		int XYZ_R, XYZ_G, XYZ_B;
		int LAB_L, LAB_a, LAB_b;
		int YUV_Y, YUV_U, YUV_V;
		int YUV_R, YUV_G, YUV_B;
		int YCbCr_Y, YCbCr_Cb, YCbCr_Cr;
		int YCbCr_R, YCbCr_G, YCbCr_B;
		int YIQ_Y, YIQ_I, YIQ_Q;
		int YIQ_R, YIQ_G, YIQ_B;
		
		switch(option) {
			case 0: //RGB
				sortRed();
		        createColorTable();
		        
		        createNewImage(0);
		        break;
			case 1: //XYZ
				for(int i =0; i < image_pixels.length; i++) {
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
					
					XYZ_X = (int) Math.floor(R * 0.4124 + G * 0.3576 + B * 0.1805);
			        XYZ_Y = (int) Math.floor(R * 0.2127 + G * 0.7152 + B * 0.0722); //0.2126
			        XYZ_Z = (int) (Math.floor(R * 0.0193 + G * 0.1192 + B * 0.9505)+1.0); // added b/c of small err
			        
			        new_image_pixels[i].setRed(XYZ_X);
			        new_image_pixels[i].setGreen(XYZ_Y);
			        new_image_pixels[i].setBlue(XYZ_Z);
			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	XYZ_X = new_image_pixels[i].getRed();
		        	XYZ_Y = new_image_pixels[i].getGreen();
		        	XYZ_Z = new_image_pixels[i].getBlue();
		        	
		        	XYZ_R = (int) Math.floor(3.2405 * XYZ_X - 1.5372 * XYZ_Y - 0.4985 * XYZ_Z);
		            XYZ_G = (int) (Math.floor(-0.9693 * XYZ_X + 1.876 * XYZ_Y + 0.0416 * XYZ_Z)+1.0); //added b/c of small err
		            XYZ_B = (int) Math.floor(0.0556 * XYZ_X - 0.2040 * XYZ_Y + 1.0573 * XYZ_Z);
		            
		            new_image_pixels[i].setRed(XYZ_R);
		            new_image_pixels[i].setGreen(XYZ_G);
		            new_image_pixels[i].setBlue(XYZ_B);
		        }
		        
		        createNewImage(1);
				break;
			case 2: //Lab
				for(int i =0; i < image_pixels.length; i++) {
					final double Xn = 0.9504559271; //95.047; // Observer = 2, Illuminant = D65
			        final double Yn = 1.00000; //100.000;
			        final double Zn = 1.0890577508; //108.883;
			        
			        double x, y, z;
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
					
					XYZ_X = (int) Math.floor(R * 0.4124 + G * 0.3576 + B * 0.1805);
			        XYZ_Y = (int) Math.floor(R * 0.2127 + G * 0.7152 + B * 0.0722); //0.2126
			        XYZ_Z = (int) (Math.floor(R * 0.0193 + G * 0.1192 + B * 0.9505)+1.0); // added b/c of small err
			        
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
			        
			        LAB_L = (int) Math.floor(0.5*((1.160*y)-0.160+1.0));
			        LAB_a = (int) Math.floor(0.5*(5.000*(x-y)+1.0));
			        LAB_b = (int) Math.floor(0.5*(2.000*(y-z)+1.0));
			        
			        new_image_pixels[i].setRed(LAB_L);
			        new_image_pixels[i].setGreen(LAB_a);
			        new_image_pixels[i].setBlue(LAB_b);
			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	
		        	XYZ_X = new_image_pixels[i].getRed();
		        	XYZ_Y = new_image_pixels[i].getGreen();
		        	XYZ_Z = new_image_pixels[i].getBlue();
		        	
		        	XYZ_R = (int) Math.floor(3.2405 * XYZ_X - 1.5372 * XYZ_Y - 0.4985 * XYZ_Z);
		            XYZ_G = (int) (Math.floor(-0.9693 * XYZ_X + 1.876 * XYZ_Y + 0.0416 * XYZ_Z)+1.0); //added b/c of small err
		            XYZ_B = (int) Math.floor(0.0556 * XYZ_X - 0.2040 * XYZ_Y + 1.0573 * XYZ_Z);
		            
		            new_image_pixels[i].setRed(XYZ_R);
		            new_image_pixels[i].setGreen(XYZ_G);
		            new_image_pixels[i].setBlue(XYZ_B);
		        }
		        
		        createNewImage(2);
				break;
			case 3: // YUV
				for(int i =0; i < image_pixels.length; i++) {
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
					
					YUV_Y = (int) Math.floor(0.299 * R + 0.587 * G + 0.114 * B);
			        YUV_U = (int) Math.floor(-0.299 * R - 0.587 * G + 0.886 * B);
			        YUV_V = (int) Math.floor(0.701 * R - 0.587 * G - 0.114 * B);
			        
			        new_image_pixels[i].setRed(YUV_Y);
			        new_image_pixels[i].setGreen(YUV_U);
			        new_image_pixels[i].setBlue(YUV_V);			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	
		        	YUV_Y = new_image_pixels[i].getRed();
		        	YUV_U = new_image_pixels[i].getGreen();
		        	YUV_V = new_image_pixels[i].getBlue();
		        	
		        	YUV_R = (int) Math.floor(YUV_Y + 1.403*YUV_V);
		            YUV_G = (int) Math.floor(YUV_Y - 0.395*YUV_U - 0.581*YUV_V);
		            YUV_B = (int) Math.floor(YUV_Y + 2.032*YUV_U);
		            
		            new_image_pixels[i].setRed(YUV_R);
		            new_image_pixels[i].setGreen(YUV_G);
		            new_image_pixels[i].setBlue(YUV_B);
		        }
		        
		        createNewImage(3);
				break;
			case 4: //YCbCr
				for(int i =0; i < image_pixels.length; i++) {
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
					
					YCbCr_Y = (int) Math.floor(0.299 * R + 0.587 * G + 0.114 * B);
			        YCbCr_Cb = (int) Math.floor(((-0.169)* R + (-0.331)*G + (0.500)*B)+128);
			        YCbCr_Cr = (int) Math.floor(((0.500)*R + (-0.419)*G + (-0.081)*B)+128);
			        
			        new_image_pixels[i].setRed(YCbCr_Y);
			        new_image_pixels[i].setGreen(YCbCr_Cb);
			        new_image_pixels[i].setBlue(YCbCr_Cr);			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	YCbCr_Y = new_image_pixels[i].getRed();
		        	YCbCr_Cb = new_image_pixels[i].getGreen();
		        	YCbCr_Cr = new_image_pixels[i].getBlue();
		        	
		        	YCbCr_R = (int) Math.floor((YCbCr_Y + 1.400*(YCbCr_Cr)+128));
		            YCbCr_G = (int) Math.floor((YCbCr_Y + (-0.343* (YCbCr_Cb)) + (-0.711*YCbCr_Cr)+128));
		            YCbCr_B = (int) Math.floor((YCbCr_Y + 1.765*(YCbCr_Cr))+128);
		            
		            new_image_pixels[i].setRed(YCbCr_R);
		            new_image_pixels[i].setGreen(YCbCr_G);
		            new_image_pixels[i].setBlue(YCbCr_B);
		        }
		        
		        createNewImage(4);
				
				break;
			case 5: //YIQ
				for(int i =0; i < image_pixels.length; i++) {
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
					
					double Rt = R%256.0f - 0.5;
					double Gt = G%256.0f - 0.5;
			        double Bt = B%256.0f - 0.5;
			        
			        YIQ_Y = (int) Math.floor(0.299 * R + 0.587 * G + 0.114 * B);
			        YIQ_I = (int) Math.floor(0.595 * R - 0.274 * G - 0.322 * B);
			        YIQ_Q = (int) Math.floor(0.211 * R - 0.523 * G + 0.312 * B);
			        
			        new_image_pixels[i].setRed(YIQ_Y);
			        new_image_pixels[i].setGreen(YIQ_I);
			        new_image_pixels[i].setBlue(YIQ_Q);			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	YIQ_Y = new_image_pixels[i].getRed();
		        	YIQ_I = new_image_pixels[i].getGreen();
		        	YIQ_Q = new_image_pixels[i].getBlue();
		        	
		        	YIQ_R = (int) Math.floor(1.0 * YIQ_Y + 0.956 * YIQ_I + 0.621 * YIQ_Q);
		            YIQ_G = (int) Math.floor(1.0 * YIQ_Y + -0.272 * YIQ_I + -0.647 * YIQ_Q);
		            YIQ_B = (int) Math.floor(1.0 * YIQ_Y + -0.523 * YIQ_I + 1.702 * YIQ_Q);
		            
		            new_image_pixels[i].setRed(YIQ_R);
		            new_image_pixels[i].setGreen(YIQ_G);
		            new_image_pixels[i].setBlue(YIQ_B);
		        }
		        //sbPrint();
		        createNewImage(5);
				break;
				
			case 6:  //HSL
				for(int i =0; i < image_pixels.length; i++) {
					double H = 0, S = 0, L = 0, M, m, diff;
					R = image_pixels[i].getRed();
					G = image_pixels[i].getGreen();
					B = image_pixels[i].getBlue();
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

			        }
			        
			        new_image_pixels[i].setRed((int) H);
			        new_image_pixels[i].setGreen((int) S);
			        new_image_pixels[i].setBlue((int) L);			        
				}
				sortRed();
		        createColorTable();
		        for(int i =0; i < new_image_pixels.length; i++) {
		        	YIQ_Y = new_image_pixels[i].getRed();
		        	YIQ_I = new_image_pixels[i].getGreen();
		        	YIQ_Q = new_image_pixels[i].getBlue();
		        	
		        	YIQ_R = (int) Math.floor(1.0 * YIQ_Y + 0.956 * YIQ_I + 0.621 * YIQ_Q);
		            YIQ_G = (int) Math.floor(1.0 * YIQ_Y + -0.272 * YIQ_I + -0.647 * YIQ_Q);
		            YIQ_B = (int) Math.floor(1.0 * YIQ_Y + -0.523 * YIQ_I + 1.702 * YIQ_Q);
		            
		            new_image_pixels[i].setRed(YIQ_R);
		            new_image_pixels[i].setGreen(YIQ_G);
		            new_image_pixels[i].setBlue(YIQ_B);
		        }
		        //sbPrint();
		        createNewImage(6);
				break;				
		}
		
	}
	public void sbPrint() throws MagickException {
		System.out.println(new_image_pixels[0]);
		System.out.println(image_pixels[0]);
		System.out.println(current_image.getOnePixel(0, 0));
	}
	
	// Create a color table for corresponding color space after median cut
	public void createColorTable() {
		colorTable[0][0] = low_Red;
		colorTable[0][1] = low_Green;
		colorTable[0][2] = low_Blue;
		
		colorTable[1][0] = low_Red; 
		colorTable[1][1] = low_Green;
		colorTable[1][2] = high_Blue;
		
		colorTable[2][0] = low_Red;
		colorTable[2][1] = high_Green;
		colorTable[2][2] = low_Blue;
		
		colorTable[3][0] = low_Red;
		colorTable[3][1] = high_Green;
		colorTable[3][2] = high_Blue;
		
		colorTable[4][0] = high_Red;
		colorTable[4][1] = low_Green;
		colorTable[4][2] = low_Blue;
		
		colorTable[5][0] = high_Red;
		colorTable[5][1] = low_Green;
		colorTable[5][2] = high_Blue;
		
		colorTable[6][0] = high_Red;
		colorTable[6][1] = high_Green;
		colorTable[6][2] = low_Blue;
		
		colorTable[7][0] = high_Red;
		colorTable[7][1] = high_Green;
		colorTable[7][2] = high_Blue;
		
		// Print out the color table to the console
		for(int r =0; r < colorTable.length; r++) {
			System.out.print("Color " + r + ":" );
			for(int c = 0; c < colorTable[r].length; c++){
				System.out.print(" " + colorTable[r][c]);
			}
			System.out.println("");
		}
		
	}
	
	public void createNewImage(int option) throws MagickException {
		MagickImage newImage = new MagickImage();
		pixels = new byte[width * height * 4];
		for(int i =0; i < size; i++) {
			
				int red = new_image_pixels[i].getRed() / 256;
				int green = new_image_pixels[i].getGreen() /256;
				int blue = new_image_pixels[i].getBlue() /256;
				
				//System.out.println(red + " " + green + " " + blue);
				
				if(red < med_Red) {
					if(green < med_Green) {
						if(blue < med_Blue) {
							pixels[4*i] = (byte)(low_Red );
							pixels[4*i +1] = (byte)(low_Green);
							pixels[4*i +2] = (byte)(low_Blue);
							pixels[4*i +3] = (byte) 255;
						} else if(blue > med_Blue) {
							pixels[4*i] = (byte)(low_Red);
							pixels[4*i +1] = (byte)(low_Green);
							pixels[4*i +2] = (byte)(high_Blue);
							pixels[4*i +3] = (byte) 255;
						}
					}else if(green > med_Green) {
						if(blue < med_Blue) {
							pixels[4*i] = (byte)(low_Red);
							pixels[4*i +1] = (byte)(high_Green);
							pixels[4*i +2] = (byte)(low_Blue);
							pixels[4*i +3] = (byte) 255;
						} else if(blue > med_Blue) {
							pixels[4*i] = (byte)(low_Red);
							pixels[4*i +1] = (byte)(high_Green);
							pixels[4*i +2] = (byte)(high_Blue);
							pixels[4*i +3] = (byte) 255;
						}
					}
				} else if(red > med_Red) {
					if(green < med_Green) {
						if(blue < med_Blue) {
							pixels[4*i] = (byte)(high_Red);
							pixels[4*i +1] = (byte)(low_Green);
							pixels[4*i +2] = (byte)(low_Blue);
							pixels[4*i +3] = (byte) 255;
						} else if(blue > med_Blue) {
							pixels[4*i] = (byte)(high_Red);
							pixels[4*i +1] = (byte)(low_Green);
							pixels[4*i +2] = (byte)(high_Blue);
							pixels[4*i +3] = (byte) 255;
						}
					} else if(green > med_Green) {
						if(blue < med_Blue){
							pixels[4*i] = (byte)(high_Red);
							pixels[4*i +1] = (byte)(high_Green);
							pixels[4*i +2] = (byte)(low_Blue);
							pixels[4*i +3] = (byte) 255;
						} else if(blue > med_Blue) {
							pixels[4*i] = (byte)(high_Red);
							pixels[4*i +1] = (byte)(high_Green);
							pixels[4*i +2] = (byte)(high_Blue);
							pixels[4*i +3] = (byte) 255;
						}
					}
				}			
		}
		
		newImage.constituteImage(width, height, "RGBA", pixels);
		MagickWindow window = new MagickWindow(newImage);
		window.setVisible(true);
		String fileNames ="";
		
		// write a color space to the file name.
		switch(option) {
			case 0:
				fileNames += "RGB" + info.getFileName();
				break;
			case 1:
				fileNames += "XYZ" + info.getFileName();
				break;
			case 2:
				fileNames += "Lab" + info.getFileName();
				break;
			case 3:
				fileNames += "YUV" + info.getFileName();
				break;
			case 4:
				fileNames += "YCbCr" + info.getFileName();
				break;
			case 5:
				fileNames += "YIQ" + info.getFileName();
				break;
			case 6:
				fileNames += "HSL" + info.getFileName();
				break;
		}				
		
		newImage.setFileName(fileNames);
		newImage.writeImage(info);		
	}
	
	
	// Print the reduce color menu
	public void printMenu() {
		System.out.println("0: Reduce to RGB");
		System.out.println("1: Reduce to XYZ");
		System.out.println("2: Reduce to Lab");
		System.out.println("3: Reduce to YUV");
		System.out.println("4: Reduce to YCbCr");
		System.out.println("5: Reduce to YIQ");
		System.out.println("6: Reduce to HSL");
		System.out.print("Option: ");		
	}
	
	// Sort Red
	public void sortRed() throws MagickException {
		PixelPacket[] sort_pixels = new PixelPacket[size];
	
		for(int i=0; i<new_image_pixels.length; i++) {
			sort_pixels[i] = new_image_pixels[i];
		}
				
		low_Red = 0;
		high_Red = 0;
		Redsort.sort(sort_pixels);
		med_Red = sort_pixels[(int) (sort_pixels.length/2)].getRed() /256;
		//System.out.println("Median is for red: " + med_Red );
		for(int i =0; i<sort_pixels.length/2; i++) {
			low_Red += sort_pixels[i].getRed() /256;
		}
		low_Red = low_Red / (sort_pixels.length /2);
		//System.out.println("Low red is " + low_Red);
		for(int i = sort_pixels.length /2; i<sort_pixels.length; i++) {
			high_Red += sort_pixels[i].getRed() /256;
		}
		high_Red = high_Red / (sort_pixels.length /2);
		//System.out.println("High red is " + high_Red);
		
		low_Green = 0;
		high_Green = 0;
		Greensort.sort(sort_pixels);
		med_Green = sort_pixels[(int) (sort_pixels.length * 0.75)].getGreen() / 256;
		//System.out.println("Median green " + med_Green);
		for(int i = sort_pixels.length/2; i < (int) (sort_pixels.length * 0.75); i++) {
			low_Green += sort_pixels[i].getGreen() / 256;
		}
		low_Green = low_Green / (sort_pixels.length / 4);
		//System.out.println("Low green is " + low_Green);
		for(int i = (int)(sort_pixels.length * 0.75); i < (int) (sort_pixels.length); i++) {
			high_Green += sort_pixels[i].getGreen() / 256;
		}
		high_Green = high_Green / (sort_pixels.length /4);
		//System.out.println("High green is " + high_Green);
		
		low_Blue = 0;
		high_Blue = 0;
		
		Bluesort.sort(sort_pixels);
		med_Blue = sort_pixels[(int)(sort_pixels.length * 7/8)].getBlue() /256;
		//System.out.println("Median blue " + med_Blue);
		
		for(int i = (int) (sort_pixels.length * 0.75); i < (int) (sort_pixels.length * 7/8); i++) {
			low_Blue += image_pixels[i].getBlue() / 256;
		}
		low_Blue = low_Blue / (sort_pixels.length / 8);
		//System.out.println("Low blue is " + low_Blue);
		for(int i = (int)(sort_pixels.length * 7/8); i < (int) (sort_pixels.length); i++) {
			high_Blue += sort_pixels[i].getBlue() / 256;
		}
		high_Blue = high_Blue / (sort_pixels.length /8);
		//System.out.println("High blue is " + high_Blue);

	}
	
}
