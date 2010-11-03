import java.io.*;
import java.awt.*;

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

    public static void encodeSignal(YUVSignal signal, HashTable hash) throws FileNotFoundException
    {

            FileDialog fd = new FileDialog( new Frame(),
            "Save As...", FileDialog.SAVE );
            fd.show();
            filePath = new String( fd.getDirectory() + fd.getFile()+".YUV" );

            try {
                // Ryan's Code
                ObjectOutputStream output = new ObjectOutputStream( new FileOutputStream( filePath ) );
                // My Code
                //OutputStream fos = new FileOutputStream (filePath);
                //file =new File(filePath); //is this needed?
                //OutputStream outStream = new BufferedOutputStream( fos );
                //ObjectOutput output = new ObjectOutputStream(outStream);
                try {
                    output.writeObject(signal);

                    if (hash != null) {
                        output.writeObject(hash);
                    }
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

        System.out.println(filePath);

        // My Code
        //InputStream fis = new FileInputStream(filePath);
        //InputStream inStream = new BufferedInputStream(fis);
        //ObjectInput input = new ObjectInputStream(inStream);
        //retrievedSignal = (YUVSignal)input.readObject();

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

        // --



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
