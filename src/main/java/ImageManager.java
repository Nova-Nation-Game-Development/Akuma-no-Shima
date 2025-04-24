

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {

	public static Image loadImage(String fileName)
	{
		try { return ImageIO.read(ImageManager.class.getResource(fileName)); }
		catch (IOException | IllegalArgumentException e) { return null; }
	}
}
