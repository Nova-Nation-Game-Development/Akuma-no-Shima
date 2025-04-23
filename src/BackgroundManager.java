package src;

import java.awt.*;

public class BackgroundManager {

    private String bgImages[]
            = {
                "/gfx/images/forest/forest_night_1.png",
                "/gfx/images/forest/forest_night_2.png",
                "/gfx/images/forest/forest_night_3.png",
                "/gfx/images/forest/forest_night_4.png",
                "/gfx/images/forest/forest_night_5.png"
            };

    // private String bgImages[] =
    // {
    // 	"/gfx/images/test_images/layer1-sky.png",
    // 	"/gfx/images/test_images/layer2-mountain.png",
    // 	"/gfx/images/test_images/layer3-ground.png"
    // };
    // a move amount of 0 would make a background stationary
    private int moveAmount[] = {1, 2, 3, 4, 5};  // applied to moveSize // 2, 6, 24

    private Background[] backgrounds;
    private int numBackgrounds;

    private GamePanel panel;
    private final GameWindow window;

    public BackgroundManager(GamePanel panel, GameWindow window) {
        this.panel = panel;
        this.window = window;

        numBackgrounds = bgImages.length;
        backgrounds = new Background[numBackgrounds];

        for (int i = 0; i < numBackgrounds; i++) {
            backgrounds[i] = new Background(panel, bgImages[i], moveAmount[i]);
        }
    }

    public void move(int direction) {
        for (int i = 0; i < numBackgrounds; i++) {
            backgrounds[i].move(direction);
        }
    }

    // The draw method draws the backgrounds on the screen. The
    // backgrounds are drawn from the back to the front.
    public void draw(Graphics2D g2) {
        for (int i = 0; i < numBackgrounds; i++) {
            backgrounds[i].draw(g2, window);
        }
    }
}
