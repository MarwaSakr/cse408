public class Menu {
    
    public static final int SELECT_IMAGE_KEY = 1;
    public static final int PREDICTIVE_CODING_OPTIONS = 2;
    public static final int QUANTIZATION_OPTIONS = 3;
    public static final int ENCODING_OPTIONS = 4;
    public static final int SAVE_KEY = 5;
    public static final int LOAD_KEY = 6;
    public static final int DISPLAY_IMAGE_KEY = 7;
    public static final int PRINT_MENU_KEY = 8;
    public static final int PRINT_DATA_KEY = 9;
    public static final int EXIT_KEY = 0;

    public static void printMenu(){
        System.out.println(SELECT_IMAGE_KEY + ": Select an image");
        System.out.println(PREDICTIVE_CODING_OPTIONS + ": Select type of Predictive Coding to use");
        System.out.println(QUANTIZATION_OPTIONS + ": Select Quantiziation options");
        System.out.println(ENCODING_OPTIONS + ": Select type of encoding to use");
        System.out.println(SAVE_KEY + ": Save image");
        System.out.println(LOAD_KEY + ": Load YUV image");
        System.out.println(DISPLAY_IMAGE_KEY + ": Display Image");
        System.out.println(PRINT_MENU_KEY + ": Print the Menu Again");
        System.out.println(PRINT_DATA_KEY + ": Print Data");
        System.out.println(EXIT_KEY + ": Exit");
    }

    public static void printSubMenu(int parent)
    {
        switch(parent)
        {
            case (PREDICTIVE_CODING_OPTIONS):
            {
                System.out.println("Please select a PC option.");
                System.out.println("1: No Predictive Coding");
                System.out.println("2: PC Option 2");
                System.out.println("3: PC Option 3");
                System.out.println("4: PC Option 4");
                System.out.println("5: PC Option 5");
                System.out.println("6: PC Option 6");
                System.out.println("0: Back to main menu");
                break;
            }
            case (QUANTIZATION_OPTIONS):
            {
                System.out.println("Please select a quantization option.");
                System.out.println("1: No quantization");
                System.out.println("2: Uniform quantization");
                System.out.println("0: Back to main menu");
                break;
            }
            case (ENCODING_OPTIONS):
            {
                System.out.println("Please select an encoding option.");
                System.out.println("1: No encoding");
                System.out.println("2: Run-length encoding");
                System.out.println("3: Shannon-Fano coding");
                System.out.println("4: Huffman coding");
                System.out.println("0: Back to main menu");
                break;
            }
            default:
                System.out.println("Invalid Menu Choice");
                Menu.printSubMenu(parent);
                break;
        }
    }

}
