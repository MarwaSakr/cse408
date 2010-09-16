import java.util.Scanner;
import magick.ImageInfo;
import magick.MagickException;

public class App 
{
    public static void main( String[] args )
    {
        ImageInfo current_image_info = null;
        int selected_option;
        Scanner scan = new Scanner(System.in);
        Menu.printMenu();

        do{
            selected_option = scan.nextInt();
            switch(selected_option){
					case Menu.SELECT_IMAGE_KEY:
                        System.out.println("Please enter the filename of your image:");
                        String filename = scan.next();
                        current_image_info = ImageUtil.load_image(filename);
                        if (current_image_info != null){
                            System.out.println("File successfully Loaded");
                        } else {
                            System.out.println("Invalid filename");
                        }
                        break;
                    case Menu.COLOR_INFO_KEY:
                        break;
                    case Menu.ADJUST_SATURATION_KEY:
                        break;
                    case Menu.SHIFT_HUE_KEY:
                        break;
                    case Menu.ADJUST_LIGHT_KEY:
                        break;
                    case Menu.REDUCE_COLOR_INSTANCES_KEY:
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
