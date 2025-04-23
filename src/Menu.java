package src;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Menu extends Scene {

    private final GameWindow window;

    private final BufferedImage image;
    private final Image loadingImage;

    public Menu(GameWindow window)
    {
        this.window = window;
        setPreferredSize(new Dimension(this.window.getWidth(), this.window.getHeight()));

        image = new BufferedImage(this.window.getWidth(), this.window.getHeight(), BufferedImage.TYPE_INT_RGB);
        loadingImage = ImageManager.loadImage("/gfx/images/background/main_menu.png");
    }

    @Override
    public void run() {

    }
}
