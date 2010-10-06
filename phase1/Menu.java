public class Menu {
    
    public static final int SELECT_IMAGE_KEY = 0;
    public static final int COLOR_INFO_KEY = 1;
    public static final int ADJUST_SATURATION_KEY = 2;
    public static final int SHIFT_HUE_KEY = 3;
    public static final int ADJUST_LIGHT_KEY = 4;
    public static final int REDUCE_COLOR_INSTANCES_KEY = 5;
    public static final int SAVE_KEY = 6;
    public static final int DISPLAY_IMAGE_KEY = 7;
    public static final int PRINT_MENU_KEY = 8;
    public static final int EXIT_KEY = 9;

    public static void printMenu(){
        System.out.println(SELECT_IMAGE_KEY + ": Select an image");
        System.out.println(COLOR_INFO_KEY + ": Obtain color information for a pixel");
        System.out.println(ADJUST_SATURATION_KEY + ": Adjust the saturation");
        System.out.println(SHIFT_HUE_KEY + ": Shift the hue");
        System.out.println(ADJUST_LIGHT_KEY + ": Adjust light in image");
        System.out.println(REDUCE_COLOR_INSTANCES_KEY + ": Reduce Color Instances");
        System.out.println(SAVE_KEY + ": Save image");
        System.out.println(DISPLAY_IMAGE_KEY + ": Display Image");
        System.out.println(PRINT_MENU_KEY + ": Print the Menu Again");
        System.out.println(EXIT_KEY + ": Exit");
    }

}
