import java.awt.FileDialog;
import java.awt.*;
import java.io.FilenameFilter;
import magick.*;
import magick.util.MagickViewer;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class ImageUtil {
    private static Frame frame = new Frame("CSE408 - Project 2");
    private static MagickViewer window = null;

    public static MagickImage load_image(String filename){
        System.setProperty ("jmagick.systemclassloader" , "no");
        ImageInfo current_image_info = null;
        MagickImage current_image = null;
        try {
            System.out.println("loading file: "+filename);
            current_image_info = new ImageInfo(filename);
            current_image = new MagickImage(current_image_info);
        } catch (MagickException ex) {
            System.out.println("Error loading file");
            System.err.println(ex.toString());
        } finally {
            if (current_image != null){
                System.out.println("File successfully Loaded");
            } else {
                System.out.println("Null MagickImage");
            }
            return current_image;
        }
    }

     /*
     * @Description Loads a directory of file names into an array
     *              of strings. Then calls ImageUtil.load_image() to
     *              load the images into an MagickImage array of size
     *              N-1 (-1 due to not using thumbs.db file).
     *
     * @Paramater String root
     * @Return ArrayList(MagickImage)
     *
     * @See ImageUtil.load_image()
     */
    public static ArrayList load_directory(String root) throws MagickException
    {
        System.setProperty("jmagick.systemclassloader", "no");
        ArrayList images = new ArrayList();
        File directory = new File(root);

        // --- Do checks to test if dierectory exists and we have read priv --- //
        if(directory.isDirectory() && directory.exists())
        {
            System.out.println("Searching " + root + " for images...");
        }
        else
        {
            System.out.println("Directory Does Not Exists!");
            return (null);
        }

        // get all file names
        File[] files = directory.listFiles();

        // create a MagickImage for each file and add to array.
        // don't add Thumbs.db.
        for(int i = 0; i < files.length; i++)
        {
            if (!files[i].getName().equals("Thumbs.db"))
            {
                images.add(i, ImageUtil.load_image(root + files[i].getName()));
            }
        }

        return (images);
    }


    /* Don't know if this works. Probably shouldn't use it. */
    public static String dialogPrompt(String structType, String ioType) throws IOException
    {
        String filePath = "";
        if(structType.equals("DIRECTORY"))
        {
            if (ioType.equals("LOAD"))
            {
                JFileChooser fd = new JFileChooser();
                fd.addChoosableFileFilter(new ImageFilter());
                fd.setAcceptAllFileFilterUsed(false);

                int fdShown = fd.showOpenDialog(new Frame());

                filePath = "...";

            }
            else if (ioType.equals("SAVE"))
            {
                JFileChooser fd = new JFileChooser();
                filePath = "...";
            }
        }
        else if (structType.equals("FILE"))
        {
            if (ioType.equals("LOAD"))
            {
                FileDialog fd = new FileDialog( new Frame(),
                "Open File...", FileDialog.LOAD );
                fd.setModal(true);
                fd.show();
                System.out.println("The Dialog has been opened.");
                filePath = new String(fd.getDirectory());

            }
            else if (ioType.equals("SAVE"))
            {
                FileDialog fd = new FileDialog( new Frame(),
                "Save File...", FileDialog.SAVE );
                fd.setModal(true);
                fd.setFilenameFilter((FilenameFilter) new ImageFilter());
                fd.requestFocus();
                //fd.show();
                System.out.println("The Dialog has been opened.");
                filePath = new String(fd.getDirectory());

            }
        }

        return (filePath);
    }

    public static void display_image(MagickImage image){
        if (window != null) {
            frame.removeNotify();
            window.setVisible(false);
            frame = new Frame("CSE408 - Project 1");
        }
        window = new MagickViewer();
        try{
            window.setImage(image);
            window.setSize(image.getDimension());
            frame.setSize(image.getDimension());
        } catch (MagickException ex) {
            System.out.println("not displayed");
            System.err.println(ex.toString());
        } finally {
            window.setVisible(true);
            frame.add(window);
            frame.setVisible(true);
        }
    }

    public static void save_image(String filename, MagickImage image){
        ImageInfo image_info;
        boolean saved = false;
        try{
            image_info = new ImageInfo(filename);
            image.setFileName(filename);
            saved = image.writeImage(image_info);
        } catch (MagickException ex) {
            saved = false;
        } finally {
            System.out.println("Image Saved Correctly: " + saved);
        }
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


}
