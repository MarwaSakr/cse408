public class Menu {
    
    public static final int SELECT_IMAGE_KEY = 0;
    public static final int SAVE_KEY = 6;
    public static final int DISPLAY_IMAGE_KEY = 7;
    public static final int PRINT_MENU_KEY = 8;
    public static final int EXIT_KEY = 9;

    public static void printMenu(){
        System.out.println(SELECT_IMAGE_KEY + ": Select an image");
        System.out.println(SAVE_KEY + ": Save image");
        System.out.println(DISPLAY_IMAGE_KEY + ": Display Image");
        System.out.println(PRINT_MENU_KEY + ": Print the Menu Again");
        System.out.println(EXIT_KEY + ": Exit");
    }

}
