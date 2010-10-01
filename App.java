import java.util.Scanner;
import java.util.InputMismatchException;
import magick.*;
import magick.util.*;

public class App
{
    public static int red = 0, blue = 0, green = 0;
    public static ReduceColorIns reduceColor;
    
    public static void main( String[] args )
    {
        System.setProperty ("jmagick.systemclassloader" , "no");
        MagickImage current_image = null;
        int selected_option = -1;
        int xVal = 0, yVal = 0;
        Scanner scan = new Scanner(System.in);
        Menu.printMenu();

        do{
            System.out.print(">");
            try{
                selected_option = scan.nextInt();
            } catch(InputMismatchException ex){
                scan.next();
                selected_option = -1;
            }
            switch(selected_option){
                    case Menu.SELECT_IMAGE_KEY:
                        System.out.println("Please enter the filename of your image:");
                        String filename = scan.next();
                        current_image = ImageUtil.load_image(filename);
                        try{
                            reduceColor = new ReduceColorIns(filename);
                        } catch (MagickException ex) {
                            System.out.println("Reduce Color image not loaded");
                        }
                        break;
                    case Menu.COLOR_INFO_KEY:
                        if (current_image == null)
                        {
                            System.out.println("Please Select an image");
                        }
                        else
                        {
                            System.out.println("Enter the cooresponding x and y values of the pixel information you want: ");
                            xVal = scan.nextInt();
                            yVal = scan.nextInt();
                            System.out.println("xVal = " + xVal + "   yVal = " + yVal + "\n");

                            try {

                                //imageWidth = current_image.getDimension().width;
                                //imageHeight = current_image.getDimension().height;

                                //pixelMatrix = new PixelPacket[imageWidth][imageHeight];
                                PixelPacket pp = new PixelPacket(0,0,0,0);

                                pp = current_image.getOnePixel(xVal, yVal);

                                red = pp.getRed()/256;
                                blue = pp.getBlue()/256;
                                green = pp.getGreen()/256;

                                // -- Set RGB values in ColorConversion
                                ColorConversion.setRed(red);
                                ColorConversion.setGreen(green);
                                ColorConversion.setBlue(blue);

                                // --- Convert to XYZ

                                ColorConversion.RGBtoXYZ(red, green, blue);


                                // -- Convert to Lab

                                ColorConversion.XYZtoLAB(red, green, blue);

                                // -- Convert to YUV

                                ColorConversion.RGBtoYUV(red, green, blue);

                                // -- Convert to YIQ

                                ColorConversion.RGBtoYIQ();

                                // -- Convert to YCrCb

                                ColorConversion.RGBtoYCbCr();

                                // -- Convert to HSL
                                
                                ColorConversion.RGBtoHSL();
                                
                                

                                
                                // -- Display Information

                                System.out.println("RGB: [" + red + ", " + green + ", " + blue + "].");


                            } catch (MagickException ex)
                            {
                                System.out.println("cannot obtain [" + xVal + ", " + yVal + "].");
                                System.err.println(ex.toString());
                            }
                        }

                        break;
                    case Menu.REDUCE_COLOR_INSTANCES_KEY:
                        int reduceOption;
                        
                        try{
                            reduceColor.copyPixels();
                            reduceColor.printMenu();
                            reduceOption = scan.nextInt();
                            reduceColor.convertColorSpace(reduceOption);
                        } catch (MagickException ex) {
                            System.out.println("Reduce Colors did not work.");
                        }
                        break;
                    case Menu.ADJUST_SATURATION_KEY:
                        break;

                    case Menu.SHIFT_HUE_KEY:
                    	if (current_image == null)
                    	System.out.println("Please select an image");
                    	else
                    	{
							System.out.println("Please enter the angle to rotate the hue (in degrees):");
                    		int rotation=Integer.parseInt(scan.next());
                            current_image = HueRotate.rotateHue(current_image,rotation);
                            System.out.println("Hue Rotated");

						}
                        break;

                    case Menu.ADJUST_LIGHT_KEY:
                        current_image = contrast.changeLight(current_image);
                        break;
                    case Menu.SAVE_KEY:
                        if (current_image == null) {
                            System.out.println("Please Select an image");
                        } else {
                            System.out.println("Please enter the filename of your image:");
                            filename = scan.next();
                            ImageUtil.save_image(filename, current_image);
                        }
                        break;
                    case Menu.DISPLAY_IMAGE_KEY:
                        if (current_image == null){
                            System.out.println("Please Select an image");
                        } else {
                            ImageUtil.display_image(current_image);
                        }
                        break;
                    case Menu.PRINT_MENU_KEY:
                        Menu.printMenu();
                        break;
                    case Menu.EXIT_KEY:
                        break;
                    default:
                        System.out.println("Invalid Menu Choice");
                        Menu.printMenu();
                        break;
	 			}
        } while (selected_option != Menu.EXIT_KEY);
        System.exit(0);


    }
}
