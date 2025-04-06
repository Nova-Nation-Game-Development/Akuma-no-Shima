package src;

import java.awt.Graphics2D;
import java.awt.Image;

public class Tile implements Entity {

    // Tile data
    private int x;
    private int y;
    private Image tileImage;
    private TileType tileType;
    private final WorldType world;

    // Directional speed
    private int dx;
    private int dy;

    // Tile Dimension
    private final int width; // Everything should be a square (typically, 64 x 64)

    // Game Panel
    private final GamePanel panel;
    
    public Tile(GamePanel panel, int x, int y, int width, TileType tileType, WorldType world)
    {
        this.panel = panel;
        this.width = width;

        this.x = x;
        this.y = y;

        this.tileType = tileType;
        this.world = world;

        setupTileData();
    }

    private void setupTileData()
    {
        switch (world)
        {
            case WorldType.FOREST -> setupForestData();
            case WorldType.VOLCANIC -> setupVolcanicData();
            case WorldType.BLIZZARD -> setupBlizzardData();
            case WorldType.END -> setupEndData();
        }    
    }

    private void setupForestData()
    {
        switch (tileType)
        {
            case TileType.PRIMARY -> tileImage = ImageManager.loadImage("/gfx/tiles/grass_tile.png");
            case TileType.SECONDARY -> tileImage = ImageManager.loadImage("/gfx/tiles/dirt_tile.png");
            case TileType.TERTIARY -> tileImage = ImageManager.loadImage("/gfx/tiles/water/water_tile_1.png"); // Use animated water in future
        }
    }

    private void setupVolcanicData()
    {
        
    }

    private void setupBlizzardData()
    {
        
    }

    private void setupEndData()
    {

    }

    public int getX() { return x; }
    public TileType getTileData() { return tileType; }

    @Override
    public void move(int direction)
    {
        dx = direction;
        x += dx;
    }

    @Override
    public void jump() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(tileImage, x, y, width, width, null);
    }

    // Remove this and create a new interface following interface segregation
    @Override
    public void performAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performAction'");
    }
}
