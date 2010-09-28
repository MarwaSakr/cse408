import magick.*;
import magick.util.MagickWindow;

public class ImageUtil {
    private static MagickWindow window = null;

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
    
    public static void display_image(MagickImage image){
        if (window != null) {
            window.setVisible(false);
        }
        window = new MagickWindow(image);
        try{
            window.setSize(image.getDimension());
        } catch (MagickException ex) {
            System.err.println(ex.toString());
        } finally {
            window.setVisible(true);
        }
    }
}
