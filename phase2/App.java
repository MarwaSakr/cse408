import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import magick.*;
import magick.util.*;

public class App
{
    public static YUVSignal signalYUV;
    
    public static void main( String[] args )
    {
        System.setProperty ("jmagick.systemclassloader" , "no");
        MagickImage current_image = null;
        int selected_option = -1;
        int option = -1;
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

                        // --- Create the Y, U, V arrays of the current image
                        signalYUV = PredictiveCodingOdd.createSignal(current_image);
                        break;
                    case Menu.PREDICTIVE_CODING_OPTIONS:
                    {
                        Menu.printSubMenu(Menu.PREDICTIVE_CODING_OPTIONS);

                        System.out.print(">");
                        try{
                            option = scan.nextInt();
                        } catch(InputMismatchException ex){
                            scan.next();
                            option = -1;
                        }

                        switch(option)
                        {
                            case (1):
                                signalYUV.encodingFlag = 1;
                                try {
                                    PredictiveCodingOdd.noPC(signalYUV);
                                } catch (MagickException ex) {
                                    System.out.println(ex.toString());
                                }
                                break;
                            case (2): // DPC fn = fn-1
                                signalYUV.encodingFlag = 2;
                                //PP Code
                                break;
                            case (3): // DPC fn = floor( (fn-1 + fn-2)/2 )
                                signalYUV.encodingFlag = 3;
                                try {
                                    PredictiveCodingOdd.differentialPC3(signalYUV);
                                } catch (MagickException ex) {
                                    System.out.println(ex.toString());
                                }
                                break;
                            case (4): // DPC fn = floor( (2*fn-1 + fn-2)/3 )
                                signalYUV.encodingFlag = 4;
                                //PP Code
                                break;
                            case (5): // DPC fn = floor( (fn-1 + 2*fn-2)/3 )
                                signalYUV.encodingFlag = 5;
                                // PP Code
                                break;
                            case (6): // DPC fn = floor( (fn-1 + fn-2 + ... + fn-10)/10 )
                                signalYUV.encodingFlag = 6;
                                // PP Code
                                break;
                            case (0): // Back to Main Menu
                                Menu.printMenu();
                                break;
                            default:
                                break;
                        }
                        Menu.printMenu();
                        break;
                    }
                    case Menu.QUANTIZATION_OPTIONS:
                    {
                        Menu.printSubMenu(Menu.QUANTIZATION_OPTIONS);
                        System.out.print(">");
                        try{
                            option = scan.nextInt();
                        } catch(InputMismatchException ex){
                            scan.next();
                            option = -1;
                        }
                        // Switch statement
                        switch(option)
                        {
                            case (1): // No Quantization
                                signalYUV.quantizationFlag = 1;
                                //Do Nothing
                                break;
                            case (2): // Uniform Quantization
                                signalYUV.quantizationFlag = 2;
                                int yBuck = QuantizeScheme.prompt(QuantizeScheme.Y);
                                int uBuck = QuantizeScheme.prompt(QuantizeScheme.U);
                                int VBuck = QuantizeScheme.prompt(QuantizeScheme.V); 
                                QuantizeScheme scheme = new QuantizeScheme(yBuck,uBuck,vBuck);
                                for(int x = 0; x++; x<signalYUV.Y.length){
                                    signalYUV.Y[x] = scheme.quantize(signalYUV.Y[x],QuantizeScheme.Y);
                                    signalYUV.U[x] = scheme.quantize(signalYUV.U[x],QuantizeScheme.U);
                                    signalYUV.V[x] = scheme.quantize(signalYUV.V[x],QuantizeScheme.V);
                                }
                                break;
                            case (0): // Back to Main Menu
                                Menu.printMenu();
                            default:
                                break;
                        }
                        Menu.printMenu();
                        break;
                    }
                    case Menu.ENCODING_OPTIONS:
                    {
                        Menu.printSubMenu(Menu.QUANTIZATION_OPTIONS);
                        System.out.print(">");
                        try{
                            option = scan.nextInt();
                        } catch(InputMismatchException ex){
                            scan.next();
                            option = -1;
                        }
                        switch(option)
                        {
                            case (1): // No Encoding
                                break;
                            case (2): // Run-length Encoding
                                break;
                            case (3): // Shannon-Fano coding
                                break;
                            case (4): // Huffman coding
                                break;
                            case (0): // Back to Main Menu
                                Menu.printMenu();
                            default:
                                break;
                        }
                        Menu.printMenu();
                        break;
                    }
                    case Menu.SAVE_KEY: // This is now saving the YUV file via YUVencoding
                        if (current_image == null) {
                            System.out.println("Please Select an image");
                        } else {
                            try {
                                YUVencoding.encodeSignal(signalYUV, null); // HashTable if exists
                            } catch (FileNotFoundException ex) {
                                System.out.println(ex.toString());
                            } catch (IOException ex) {
                                System.out.println(ex.toString());
                            }
                        }
                        Menu.printMenu();
                        break;
                    case Menu.LOAD_KEY: // This is now loading the YUV file via YUVencoding to the current_image
                        try {
                            signalYUV = YUVencoding.decodeSignal();
                            current_image = PredictiveCodingOdd.constructImage(signalYUV);
                        } catch (FileNotFoundException ex) {
                            System.out.println(ex.toString());
                        } catch (IOException ex) {
                            System.out.println(ex.toString());
                        } catch (ClassNotFoundException ex) {
                            System.out.println(ex.toString());
                        } catch (MagickException ex) {
                            System.out.println(ex.toString());
                        }
                        Menu.printMenu();
                        break;
                    case Menu.DISPLAY_IMAGE_KEY: // may need to check if signalYUV has been turned into image
                        if (current_image == null){
                            System.out.println("Please Select an image");
                        } else {
                            ImageUtil.display_image(current_image);
                        }
                        break;
                    case Menu.PRINT_MENU_KEY:
                        Menu.printMenu();
                        break;
                case Menu.PRINT_DATA_KEY: // Prints all necessary data
                {
                    try {
                        //This will act as a debug key. I am using it to test if my
                        // predictive coding is working or not.
                        current_image = PredictiveCodingOdd.constructImage(signalYUV);
                    } catch (MagickException ex) {
                        System.err.println(ex.toString());
                    }
                    if (current_image == null){
                                System.out.println("Please Select an image");
                    } else {
                        ImageUtil.display_image(current_image);
                    }

                    //PredictiveCodingOdd.printSignal(signalYUV);
                    break;
                }
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
