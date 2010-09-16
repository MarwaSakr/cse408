import java.util.Scanner;
import magick.ImageInfo;
import magick.MagickException;

public class App 
{
    public static void main( String[] args )
    {
        boolean image_selected = false;
        ImageInfo markImageInfo = null;
        int selected_option;
        Scanner scan = new Scanner(System.in);
        Menu.printMenu();

        do{
            selected_option = scan.nextInt();
            System.out.println("You chose: " + selected_option);
        } while (selected_option != Menu.EXIT_KEY);
        System.exit(0);

        try {
            markImageInfo = new ImageInfo();
        } catch (MagickException ex) {
            System.err.println(ex.toString());
        }
    }
}
