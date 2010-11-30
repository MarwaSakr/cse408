import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import magick.*;


public class App
{
    /*
     * Variable initializations
     * Load all images from /pics/ into inputImages
     * Select color space
     * quantize color space
     * compute color histogram
     *
     */
    public static void main( String[] args ) throws MagickException
    {
        // --- Variable Initializations --- //
        System.setProperty ("jmagick.systemclassloader" , "no");
        MagickImage query_image = null;
        MagickImage result_image = null; // closest image to query image
        MagickImage[] inputImages = null; //images from input directory //- 52 files in pics
        ArrayList inputImagesAL = new ArrayList();
        String directoryPath = "pics/";
        int selected_option = -1;
        int option = -1; //for sub menu system
        Scanner scan = new Scanner(System.in);
        int bins = 1;


        do{
            Menu.printMenu();
            System.out.print(">");
            try {
                selected_option = scan.nextInt();
            } catch(InputMismatchException ex)
            {
                scan.next();
                selected_option = -1;
            }

            // --- Process Menu Selection --- //
            switch(selected_option)
            {
                    case Menu.CREATE_DATABASE_KEY: /* Select input directory */
                    {
                         // --- Select a color space --- //
                        System.out.println("\nPlease enter a colorspace: ");
                        String colorspace = scan.next();


                        System.out.println("\nNumber of bins:");
                        bins = scan.nextInt();

                        // --- Load all images in DirectoryPath --- //
                        inputImagesAL = ImageUtil.load_directory(directoryPath);

                        if (inputImagesAL.contains(null))
                            System.out.println("Error loading directory.");
                        else
                            System.out.println("Directory Loaded.");

                        // --- Convert ArrayList into array --- //
                        inputImagesAL.trimToSize();
                        inputImages = (MagickImage[]) inputImagesAL.toArray(new MagickImage[0]); //doesn't work.

                        //System.out.println("Size of inputImages: " + inputImages.length);
                        
                       

                        identifyColorHist.setup(inputImages, colorspace, bins);



                        break;
                    }
                    case Menu.SELECT_QUERY_KEY: /* Select the query image */
                    {
                        System.out.println("\nPlease enter the filename of your image:");
                        String filename = scan.next();
                        query_image = ImageUtil.load_image(filename);

                        break;
                    }
                    case Menu.DISPLAY_RESULTS_KEY:
                    {
                        Menu.printSubMenu(Menu.DISPLAY_RESULTS_KEY);

                        System.out.print("> ");
                        option = scan.nextInt();
                        /*try {
                            option = scan.nextInt();
                        } catch(InputMismatchException ex)
                        {
                            scan.next();
                            option = -1;
                        }*/

                        switch (option)
                        {
                            case (1):
                            {
                                // print histogram
                                System.out.println("Histogram Specification");
                                System.out.println("Number of Bins: " + bins);
                                identifyColorHist.printInfo();
                                
                                
                                break;
                            }
                            case (2):
                            {
                                // print ...
                                break;
                            }
                            case (3):
                            {
                                // print ...
                                break;
                            }
                            case (4):
                            {
                                // print ...
                                break;
                            }
                            case (5):
                            {
                                // print ...
                                break;
                            }
                            case (6):
                            {
                                // print ...
                                break;
                            }
                            case (7):
                            {
                                // Display result image
                                if (result_image == null){
                                    System.out.println("Please Select an image");
                                } else {
                                    ImageUtil.display_image(result_image);
                                }
                                break;
                            }
                            case (0):
                            {
                                option = -1;
                                Menu.printMenu();
                                break;
                            }
                            default:
                            {
                                Menu.printSubMenu(option);
                                break;
                            }
                        }

                        break;
                    }
                    case Menu.PRINT_MENU_KEY:
                    {
                        Menu.printMenu();
                        break;
                    }
                    case Menu.EXIT_KEY:
                    {
                        break;
                    }
                    default:
                    {
                        System.out.println("Invalid Menu Choice");
                        Menu.printMenu();
                        break;
                    }
            }
        } while (selected_option != Menu.EXIT_KEY);
        System.exit(0);

    }
}
