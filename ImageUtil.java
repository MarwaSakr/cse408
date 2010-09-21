import magick.*;
import magick.util.MagickWindow;

public class ImageUtil {

    public static MagickImage load_image(String filename){
        System.setProperty ("jmagick.systemclassloader" , "no");
        ImageInfo current_image_info = null;
        MagickImage current_image = null;
        try {
            System.out.println("loading file: "+filename);
            current_image_info = new ImageInfo(filename);
            current_image = new MagickImage(current_image_info);
        } catch (MagickException ex) {
            System.out.println("ERROR");
            System.err.println(ex.toString());
        } finally {
            return current_image;
        }
    }
    
    public static void display_image(MagickImage image){
        MagickWindow window = new MagickWindow(image);
        window.setVisible(true);
    }
}
