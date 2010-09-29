import java.util.Scanner;
import java.util.InputMismatchException;
import magick.*;
import magick.util.*;

public class App
{

    public static void main( String[] args )
    {
        System.setProperty ("jmagick.systemclassloader" , "no");
        MagickImage current_image = null;
        int selected_option = -1;
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
                        break;
                    case Menu.COLOR_INFO_KEY:
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
                    		try{
                    		String fileName=current_image.getFileName();
                    		current_image= HueRotate.rotateHue(current_image.getFileName(), rotation);}
                    		catch(MagickException ex) {
								System.out.println("Error loading file");
            					System.err.println(ex.toString());
							}
						}
                        break;

                    case Menu.ADJUST_LIGHT_KEY:
                        break;
                    case Menu.REDUCE_COLOR_INSTANCES_KEY:
                        break;
                    case Menu.DISPLAY_IMAGE_KEY:
                        if (current_image != null){
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
