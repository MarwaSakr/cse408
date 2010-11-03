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
import magick.util.*;

public class App
{
    public static YUVSignal signalYUV;

    public static void main( String[] args )
    {
		int intSize=32;
		double yDistortion = 0.0;
		double uDistortion = 0.0;
		double vDistortion = 0.0;
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
                        yDistortion = 0.0;
                        uDistortion = 0.0;
                        vDistortion = 0.0;
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
                                try
                                {
									PredictiveCodingOdd.differentialPC2(signalYUV);
								} catch (MagickException ex)
								{
									System.out.println(ex.toString());
                                }
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
                                try
                                {
									PredictiveCodingOdd.differentialPC4(signalYUV);
	                            } catch (MagickException ex)
	                            {
                                    System.out.println(ex.toString());
                                }
                                break;
                            case (5): // DPC fn = floor( (fn-1 + 2*fn-2)/3 )
                                signalYUV.encodingFlag = 5;
                                try {
										PredictiveCodingOdd.differentialPC5(signalYUV);
		                            } catch (MagickException ex)
		                            {
		                                System.out.println(ex.toString());
                                }
                                break;
                            case (6): // DPC fn = floor( (fn-1 + fn-2 + ... + fn-10)/10 )
                                signalYUV.encodingFlag = 6;
                                try
                                {
	                                   PredictiveCodingOdd.differentialPC6(signalYUV);
                                } catch (MagickException ex)
                                {
                                    System.out.println(ex.toString());
                                }
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
                                int vBuck = QuantizeScheme.prompt(QuantizeScheme.V);
                                QuantizeScheme scheme = new QuantizeScheme(yBuck,uBuck,vBuck);
                                for(int x = 0;x<signalYUV.Ynew.length;x++){
                                    signalYUV.Ynew[x] = scheme.quantize(signalYUV.Ynew[x],QuantizeScheme.Y);
                                    signalYUV.Unew[x] = scheme.quantize(signalYUV.Unew[x],QuantizeScheme.U);
                                    signalYUV.Vnew[x] = scheme.quantize(signalYUV.Vnew[x],QuantizeScheme.V);
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
                        RunlengthEncoding runlength_YUV;

						                        ShannonFanoEncoding shannon;

						                        HuffmanEncoding huffman;



						                        int total_OriginalY = 0;
						                        int total_OriginalU = 0;
						                        int total_OriginalV = 0;
						                        int total_OriginalYUV = 0;

						                    	int total_Y = 0;
						                        int total_U = 0;
						                        int total_V = 0;

						                        String[] conversionY = new String[signalYUV.Ynew.length];
						                        String[] conversionU = new String[signalYUV.Unew.length];
						                        String[] conversionV = new String[signalYUV.Vnew.length];

						                        ArrayList<Record> records_Y = new ArrayList<Record>();
						                        ArrayList<Record> records_U = new ArrayList<Record>();
						                        ArrayList<Record> records_V = new ArrayList<Record>();

						                        Menu.printSubMenu(Menu.ENCODING_OPTIONS);
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
						                            	signalYUV.encodingFlag = 1;
						                                break;
						                            case (2): // Run-length Encoding

						                                Encoding encodeRunY = new Encoding();
						                                Encoding encodeRunU = new Encoding();
						                                Encoding encodeRunV = new Encoding();

						                            	signalYUV.encodingFlag = 2;
						                            	runlength_YUV = new RunlengthEncoding();

						                            	int[] RL_Y = runlength_YUV.encode(signalYUV.Ynew);
						                                int[] RL_U = runlength_YUV.encode(signalYUV.Unew);
						                                int[] RL_V = runlength_YUV.encode(signalYUV.Vnew);

						                                //System.out.println("Length of YNew:" + RL_Y.length);
						                                //System.out.println("Length of Original Array: " + signalYUV.Yorg.length);
						                                System.out.println("-----Before Encoding-----");

							                            total_OriginalY = 32 * signalYUV.Yorg.length;
							                            System.out.println("Total number of bits of Y: " + total_OriginalY);

							                            total_OriginalU = 32 * signalYUV.Uorg.length;
							                            System.out.println("Total number of bits of U: " + total_OriginalU);

							                            total_OriginalV = 32 * signalYUV.Vorg.length;
							                            System.out.println("Total number of bits of V: " + total_OriginalV);

							                            total_OriginalYUV = total_OriginalY + total_OriginalU + total_OriginalV;
							                            System.out.println("Total number of bits of YUV: " + total_OriginalYUV);

						                                signalYUV.Ynew = RL_Y;
						                                signalYUV.Unew = RL_U;
						                                signalYUV.Vnew = RL_V;

						                                System.out.println("-----After Encoding-----");
							                            //System.out.println("Length of YNew:" + signalYUV.Ynew.length);
							                            //System.out.println("Last value at Ynew: " + signalYUV.Ynew[signalYUV.Ynew.length-1]);
							                            int totalBitsY = 32 * signalYUV.Ynew.length;
							                            System.out.println("Total number of bits of Y: " + totalBitsY);

							                            int totalBitsU = 32 * signalYUV.Unew.length;
							                            System.out.println("Total number of bits of U: " + totalBitsU);

							                            int totalBitsV = 32 * signalYUV.Vnew.length;
							                            System.out.println("Total number of bits of V: " + totalBitsV);

							                            int totalBitsYUV = totalBitsY + totalBitsU + totalBitsV;
							                            System.out.println("Total number of bits of YUV: " + totalBitsYUV);


						                                break;
						                            case (3): // Shannon-Fano coding

						                                Encoding encodeSFY = new Encoding();
						                                Encoding encodeSFU = new Encoding();
						                                Encoding encodeSFV = new Encoding();

						                            	signalYUV.encodingFlag = 3;

							                            shannon = new ShannonFanoEncoding();

							                            Hashtable shannonCodeTable_Y = new Hashtable();
							                            Hashtable shannonCodeTable_U = new Hashtable();
							                            Hashtable shannonCodeTable_V = new Hashtable();

							                            for(int i = 0; i < signalYUV.Ynew.length; i++) {
						                                    conversionY[i] = String.valueOf(signalYUV.Ynew[i]);
							                            }

							                            for(int i = 0; i < signalYUV.Unew.length; i++) {
						                                    conversionU[i] = String.valueOf(signalYUV.Unew[i]);
							                            }

							                            for(int i = 0; i < signalYUV.Vnew.length; i++) {
						                                    conversionV[i] = String.valueOf(signalYUV.Vnew[i]);
							                            }

							                            //System.out.println("Y Frequency");
							                            records_Y = encodeSFY.findFrequency(conversionY);
							                            //System.out.println("U Frequency");
							                            records_U = encodeSFU.findFrequency(conversionU);
							                            //System.out.println("V Frequency");
							                            records_V = encodeSFV.findFrequency(conversionV);

							                            /*
							                            for(int i = 0; i < records_Y.size(); i++) {
							                                System.out.println(records_Y.get(i));
							                            }*/

							                            Collections.sort(records_Y);
							                            Collections.sort(records_U);
							                            Collections.sort(records_V);


							                            for(int i = 0; i < records_Y.size(); i++) {
						                                    total_Y += records_Y.get(i).getFrequency();
							                            }

							                            for(int i = 0; i < records_U.size(); i++) {
						                                    total_U += records_U.get(i).getFrequency();
							                            }

							                            for(int i = 0; i < records_V.size(); i++) {
						                                    total_V += records_V.get(i).getFrequency();
							                            }

							                            shannon.buildCode(records_Y, total_Y, 0, records_Y.size());
							                            shannon.buildCode(records_U, total_U, 0, records_U.size());
							                            shannon.buildCode(records_V, total_V, 0, records_V.size());

							                            System.out.println("-----Before Encoding-----");

							                            total_OriginalY = 32 * signalYUV.Yorg.length;
							                            System.out.println("Total number of bits of Y: " + total_OriginalY);

							                            total_OriginalU = 32 * signalYUV.Uorg.length;
							                            System.out.println("Total number of bits of U: " + total_OriginalU);

							                            total_OriginalV = 32 * signalYUV.Vorg.length;
							                            System.out.println("Total number of bits of V: " + total_OriginalV);

							                            total_OriginalYUV = total_OriginalY + total_OriginalU + total_OriginalV;
							                            System.out.println("Total number of bits of YUV: " + total_OriginalYUV);

							                            System.out.println("-----After Encoding-----");

							                            int totalBitsY1 = shannon.calculateTotalBits(records_Y);
							                            System.out.println("Total number of bits of Y: " + totalBitsY1);

							                            int totalBitsU1 = shannon.calculateTotalBits(records_U);
							                            System.out.println("Total number of bits of U: " + totalBitsU1);

							                            int totalBitsV1 = shannon.calculateTotalBits(records_V);
							                            System.out.println("Total number of bits of V: " + totalBitsV1);

							                            int totalBitsYUV1 = totalBitsY1 + totalBitsU1 + totalBitsV1;
							                            System.out.println("Total number of bits of YUV: " + totalBitsYUV1);


							                            shannonCodeTable_Y = shannon.buildCodeTable(records_Y, shannonCodeTable_Y);
							                            shannonCodeTable_U = shannon.buildCodeTable(records_U, shannonCodeTable_U);
							                            shannonCodeTable_V = shannon.buildCodeTable(records_V, shannonCodeTable_V);

							                            signalYUV.YHash = shannonCodeTable_Y;
							                            signalYUV.UHash = shannonCodeTable_U;
							                            signalYUV.VHash = shannonCodeTable_V;

							                            signalYUV.Ynewencoded = shannon.encode(signalYUV.Ynew, shannonCodeTable_Y);
							                            signalYUV.Unewencoded = shannon.encode(signalYUV.Unew, shannonCodeTable_U);
							                            signalYUV.Vnewencoded = shannon.encode(signalYUV.Vnew, shannonCodeTable_V);

						                                break;

						                            case (4): // Huffman coding

						                                Encoding encodeHCY = new Encoding();
						                                Encoding encodeHCU = new Encoding();
						                                Encoding encodeHCV = new Encoding();

						                            	signalYUV.encodingFlag = 4;

						                            	Hashtable huffmanCodeTable_Y = new Hashtable();
						                            	Hashtable huffmanCodeTable_U = new Hashtable();
						                            	Hashtable huffmanCodeTable_V = new Hashtable();

						                            	for(int i = 0; i < signalYUV.Ynew.length; i++) {
						                            		conversionY[i] = String.valueOf(signalYUV.Ynew[i]);
						                            	}

							                            for(int i = 0; i < signalYUV.Unew.length; i++) {
						                                    conversionU[i] = String.valueOf(signalYUV.Unew[i]);
							                            }

							                            for(int i = 0; i < signalYUV.Vnew.length; i++) {
						                                    conversionV[i] = String.valueOf(signalYUV.Vnew[i]);
							                            }

							                            records_Y = encodeHCY.findFrequency(conversionY);
							                            records_U = encodeHCU.findFrequency(conversionU);
							                            records_V = encodeHCV.findFrequency(conversionV);

							                            Collections.sort(records_Y);
							                            Collections.sort(records_U);
							                            Collections.sort(records_V);

							                            HuffmanEncoding huffman_Y = new HuffmanEncoding((ArrayList<Record>) records_Y.clone());
							                            HuffmanEncoding huffman_U = new HuffmanEncoding((ArrayList<Record>) records_U.clone());
							                            HuffmanEncoding huffman_V = new HuffmanEncoding((ArrayList<Record>) records_V.clone());

							                            ArrayList<Record> root_Y = huffman_Y.buildTree();
							                            ArrayList<Record> root_U = huffman_U.buildTree();
							                            ArrayList<Record> root_V = huffman_V.buildTree();

							                            signalYUV.YHash = huffman_Y.traverse(root_Y.get(0), "", huffmanCodeTable_Y);
							                            signalYUV.UHash = huffman_U.traverse(root_U.get(0), "", huffmanCodeTable_U);
							                            signalYUV.VHash = huffman_V.traverse(root_V.get(0), "", huffmanCodeTable_V);

							                            String[] encodedArray_Y = huffman_Y.encode(signalYUV.Ynew, huffmanCodeTable_Y);
							                            String[] encodedArray_U = huffman_U.encode(signalYUV.Unew, huffmanCodeTable_U);
							                            String[] encodedArray_V = huffman_V.encode(signalYUV.Vnew, huffmanCodeTable_V);

							                            signalYUV.Ynewencoded = encodedArray_Y;
							                            signalYUV.Unewencoded = encodedArray_U;
							                            signalYUV.Vnewencoded = encodedArray_V;

							                            System.out.println("-----Before Encoding-----");

							                            total_OriginalY = 32 * signalYUV.Yorg.length;
							                            System.out.println("Total number of bits of Y: " + total_OriginalY);

							                            total_OriginalU = 32 * signalYUV.Uorg.length;
							                            System.out.println("Total number of bits of U: " + total_OriginalU);

							                            total_OriginalV = 32 * signalYUV.Vorg.length;
							                            System.out.println("Total number of bits of V: " + total_OriginalV);

							                            total_OriginalYUV = total_OriginalY + total_OriginalU + total_OriginalV;
							                            System.out.println("Total number of bits of YUV: " + total_OriginalYUV);

							                            System.out.println("-----After Encoding-----");

							                            int totalBitsY11 = huffman_Y.calculateTotalBits(records_Y, huffmanCodeTable_Y);
							                            System.out.println("Total number of bits of Y: " + totalBitsY11);

							                            int totalBitsU11 = huffman_U.calculateTotalBits(records_U, huffmanCodeTable_U);
							                            System.out.println("Total number of bits of U: " + totalBitsU11);

							                            int totalBitsV11 = huffman_V.calculateTotalBits(records_V, huffmanCodeTable_V);
							                            System.out.println("Total number of bits of V: " + totalBitsV11);

							                            int totalBitsYUV11 = totalBitsY11 + totalBitsU11 + totalBitsV11;
							                            System.out.println("Total number of bits of YUV: " + totalBitsYUV11);

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
                                YUVencoding.encodeSignal(signalYUV); // HashTable if exists
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
                            try{
                                current_image = PredictiveCodingOdd.constructImage(signalYUV);
                            } catch(Exception ex){}
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
                        //current_image = PredictiveCodingOdd.constructImage(signalYUV);

                        int width = signalYUV.width;
  			            int height = signalYUV.height;
                        int resolution=height*width;
						for (int i=0; i<height; i++)
						{
							for (int j=0; j<width; j++)
						    {
								yDistortion+=(double)((signalYUV.Yorg[i*width+j]-signalYUV.Ynew[i*width+j])*(signalYUV.Yorg[i*width+j]-signalYUV.Ynew[i*width+j]))/(double)resolution;
					            uDistortion+=(double)((signalYUV.Uorg[i*width+j]-signalYUV.Unew[i*width+j])*(signalYUV.Uorg[i*width+j]-signalYUV.Unew[i*width+j]))/(double)resolution;
		 	                    vDistortion+=(double)((signalYUV.Vorg[i*width+j]-signalYUV.Vnew[i*width+j])*(signalYUV.Vorg[i*width+j]-signalYUV.Vnew[i*width+j]))/(double)resolution;
						             }
         } // end of for loop

                        System.out.println("Y distortion = "+ yDistortion+" U distortion = "+ uDistortion+ " V distortion = "+ vDistortion+" \n");
                    } catch (Exception ex) {
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
