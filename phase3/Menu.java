public class Menu {

    public static final int CREATE_DATABASE_KEY = 1; //Create DB
    public static final int SELECT_QUERY_KEY = 2;        //query database
    public static final int DISPLAY_RESULTS_KEY = 3;   //Display data. Submenu - output from 1,2,3,4,and display image
    public static final int PRINT_MENU_KEY = 4;       //Print Menu
    public static final int EXIT_KEY = 0;

    public static void printMenu(){
        System.out.println(CREATE_DATABASE_KEY + ": Setup (only run once)");
        System.out.println(SELECT_QUERY_KEY + ": Query database");
        System.out.println(DISPLAY_RESULTS_KEY + ": Display Data");
        System.out.println(PRINT_MENU_KEY + ": Print Main Menu");
        System.out.println(EXIT_KEY + ": Exit");
    }

    public static void printSubMenu(int parent)
    {
        switch(parent)
        {
            case (DISPLAY_RESULTS_KEY):
            {
                System.out.println("Please select the data you would wish to output.");
                System.out.println("1: color-instance boundaries");
                System.out.println("2: color historgram triplet");
                System.out.println("3: texture histogram tuples");
                System.out.println("4: line features");
                System.out.println("5: index directory?");
                System.out.println("6: 5 best matches");
                System.out.println("7: Display Result image");
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
