package src;

import java.awt.Graphics2D;
import java.awt.Image;

public class Player implements Entity {
    
    // Location
    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;

    // Shape
    private int width;
    private int height;
    private Image playerImage;

    // Game Panel
    private GamePanel panel;

    public Player(GamePanel panel, int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;

        this.panel = panel;

        playerImage = ImageManager.loadImage("/gfx/characters/frames/char_noroi_idle.png");
    }

    // Directional mutators
    public void setDX(int newDX) { dx = newDX; }
    public void setDY(int newDY) { dy = newDY; }
    // Directional accessors
    public int getDX() { return dx; }
    public int getDY() { return dy; }

    // Locational Accessors
    public int getX() { return x; }
    public int getY() { return y; }

    // Shape Mutators
    public void setWidth(int newWidth) { width = newWidth; }
    public void setHeight(int newHeight) { height = newHeight; }
    // Shape Accessors
    public int getHeight() { return height; }

    @Override
    public void move(int direction)
    {
        dx = direction;

        if (x + dx > 0 && x + dx < panel.getWidth() - width)
            x += dx;
    }

    public void stopMoving() { dx = 0; }

    @Override
    public void jump() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'jump'");
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(playerImage, x, y, width, height, null);
    }

    @Override
    public void performAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performAction'");
    }
}
