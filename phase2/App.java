import java.util.Scanner;
import java.util.InputMismatchException;
import magick.*;
import magick.util.*;

public class App
{
    public static float y = 0L, u = 0L, v = 0L;
    
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
