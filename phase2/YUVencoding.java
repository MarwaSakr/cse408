import java.io.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author jvmilazz
 */
public class YUVencoding {

    private static String filePath;
    private static YUVSignal retrievedSignal;
    public static HashTable table;
    public static double yDistortion;
    public static double uDistortion;
    public static double vDistortion;

    public static void encodeSignal(YUVSignal signal) throws FileNotFoundException
    {

            FileDialog fd = new FileDialog( new Frame(),
            "Save As...", FileDialog.SAVE );
            fd.show();
            filePath = new String( fd.getDirectory() + fd.getFile()+".YUV" );

            try {
                ObjectOutputStream output = new ObjectOutputStream( new FileOutputStream( filePath ) );

                try {
                    output.writeObject(signal);
                } finally {
                    if (output != null)
                    output.close();
                }
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }


    } // end of encodeSignal

    public static YUVSignal decodeSignal () throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream input;

        // --  New Code
        FileDialog fd = new FileDialog( new Frame(),
            "Open File...", FileDialog.LOAD );
            fd.show();
            filePath = new String( fd.getDirectory() + fd.getFile());

        //System.out.println(filePath);


        // Ryan's Code
        try {
            input = new ObjectInputStream(new FileInputStream (filePath));
            retrievedSignal = (YUVSignal)input.readObject();

            if(retrievedSignal.encodingFlag == 4)
                table = (HashTable) input.readObject();

            input.close();
        } catch (IOException ex)
        {
            System.err.println(ex.toString());
        }

        // -- Check type of encoding to rebuild YUVSignal

        if(retrievedSignal.encodingFlag == 2)
        {
			retrievedSignal.Ynew = RunlengthEncoding.decode(retrievedSignal.Ynew, retrievedSignal.Yorg.length); // Is this right?
			retrievedSignal.Unew = RunlengthEncoding.decode(retrievedSignal.Unew, retrievedSignal.Uorg.length);
			retrievedSignal.Vnew = RunlengthEncoding.decode(retrievedSignal.Vnew, retrievedSignal.Vorg.length);
		}
		else if (retrievedSignal.encodingFlag == 3)
		{
			retrievedSignal.Ynew = ShannonFanoEncoding.decode(retrievedSignal.Ynewencoded, retrievedSignal.YHash);
			retrievedSignal.Unew = ShannonFanoEncoding.decode(retrievedSignal.Unewencoded, retrievedSignal.UHash);
			retrievedSignal.Vnew = ShannonFanoEncoding.decode(retrievedSignal.Vnewencoded, retrievedSignal.VHash);
		}
		else if (retrievedSignal.encodingFlag == 4)
		{
			retrievedSignal.Ynew = HuffmanEncoding.decode(retrievedSignal.Ynewencoded, retrievedSignal.YHash);
			retrievedSignal.Unew = HuffmanEncoding.decode(retrievedSignal.Unewencoded, retrievedSignal.UHash);
			retrievedSignal.Vnew = HuffmanEncoding.decode(retrievedSignal.Vnewencoded, retrievedSignal.VHash);
		}

		// -- Signal has been reconstructed. Find error and return signal


         int resolution=retrievedSignal.height*retrievedSignal.width;
         int width = retrievedSignal.width;
         int height = retrievedSignal.height;

         for (int i=0; i<height; i++)
         {
             for (int j=0; j<width; j++)
             {
                 yDistortion+=(double)((retrievedSignal.Yorg[i*width+j]-retrievedSignal.Ynew[i*width+j])*(retrievedSignal.Yorg[i*width+j]-retrievedSignal.Ynew[i*width+j]))/(double)resolution;
                 uDistortion+=(double)((retrievedSignal.Uorg[i*width+j]-retrievedSignal.Unew[i*width+j])*(retrievedSignal.Uorg[i*width+j]-retrievedSignal.Unew[i*width+j]))/(double)resolution;
                 vDistortion+=(double)((retrievedSignal.Vorg[i*width+j]-retrievedSignal.Vnew[i*width+j])*(retrievedSignal.Vorg[i*width+j]-retrievedSignal.Vnew[i*width+j]))/(double)resolution;
             }
         } // end of for loop

         return retrievedSignal;

    } // end of decodeSignal
}
