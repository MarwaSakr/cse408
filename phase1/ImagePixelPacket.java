import magick.MagickImage;


public class ImagePixelPacket {
	static int x;
	static int y;
	static int red;
	static int green;
	static int blue;
	
	public ImagePixelPacket(int r, int g, int b, int x, int y) {
		this.red = r;
		this.green = g;
		this.blue = b;
		this.x = x;
		this.y = y;
	}
}
