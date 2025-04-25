package com.novanation.akumanoshima;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class Tile {

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
            case FOREST -> setupForestData();
            case VOLCANIC -> setupVolcanicData();
            case BLIZZARD -> setupBlizzardData();
            case END -> setupEndData();
        }    
    }

    private void setupForestData()
    {
        switch (tileType)
        {
            case PRIMARY -> tileImage = ImageManager.loadImage("/gfx/tiles/grass_tile.png");
            case SECONDARY -> tileImage = ImageManager.loadImage("/gfx/tiles/dirt_tile.png");
            case TERTIARY -> tileImage = ImageManager.loadImage("/gfx/tiles/water/water_tile_1.png"); // Use animated water in future
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

    public void move(int direction)
    {
        dx = direction;
        x += dx;
    }

    public void jump() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(tileImage, x, y, width, width, null);
    }

    // Remove this and create a new interface following interface segregation
    public void performAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performAction'");
    }

    public Rectangle2D.Double getEntityBounds() { return null; }

    public Chunk getCurrentChunk() { return null; }

    public void moveY(double dx) { }

    public void onGround(boolean onGround) { }
}
