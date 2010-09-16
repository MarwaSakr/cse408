import magick.ImageInfo;
import magick.MagickException;

public class ImageUtil {

    public static ImageInfo load_image(String filename){
        ImageInfo current_image_info = null;
        try {
            current_image_info = new ImageInfo(filename);
        } catch (MagickException ex) {
            System.err.println(ex.toString());
        } finally {
            return current_image_info;
        }
    }
}
