package src;

import java.awt.Graphics2D;

public abstract class Tile implements Entity {

    // Tile data
    private int x;
    private int y;
    private String tileData;

    // Directional speed
    private int dx;
    private int dy;

    // Tile Dimension
    private final int width; // Everything should be a square (typically, 64 x 64)

    // Game Panel
    private final GamePanel panel;
    
    public Tile(GamePanel panel, int x, int y, int width, String tileData)
    {
        this.panel = panel;
        this.width = width;

        this.x = x;
        this.y = y;

        this.tileData = tileData;
    }

    public int getX() { return x; }
    public String getTileData() { return tileData; }

    @Override
    public void move(int direction)
    {
        dx = direction;

        if (x + dx > 0 && x + dx < panel.getWidth() - width)
            x += dx;
    }

    @Override
    public void jump() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
