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
            case TERTIARY -> tileImage = ImageManager.loadImage("/gfx/tiles/animated/water_tile_1.png"); // TODO: Use animated water in future
            case VOID -> {}
        }
    }

    private void setupVolcanicData()
    {
        switch (tileType)
        {
            case PRIMARY -> tileImage = ImageManager.loadImage("/gfx/tiles/lava_stone_tile.png");
            case SECONDARY -> tileImage = ImageManager.loadImage("/gfx/tiles/stone_tile.png");
            case TERTIARY -> tileImage = ImageManager.loadImage("/gfx/tiles/animated/lava_tile.png"); // TODO: Use animated lava in future
            case VOID -> {}
        }
    }

    private void setupBlizzardData()
    {
        switch (tileType)
        {
            case PRIMARY -> tileImage = ImageManager.loadImage("/gfx/tiles/snowy_dirt_tile.png");
            case SECONDARY -> tileImage = ImageManager.loadImage("/gfx/tiles/dirt_blizzard_tile.png");
            case TERTIARY -> tileImage = ImageManager.loadImage("/gfx/tiles/animated/ice_tile.png"); // TODO: Use ice tile
            case VOID -> {}
        }
    }

    private void setupEndData()
    {
        switch (tileType)
        {
            case PRIMARY -> tileImage = ImageManager.loadImage("/gfx/tiles/grass_tile.png"); // TODO: Use brick tiles
            case SECONDARY -> tileImage = ImageManager.loadImage("/gfx/tiles/dirt_tile.png"); // TODO: Use cracked brick tiles
            case TERTIARY -> {}
            case VOID -> {}
        }
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

    public Rectangle2D.Double getEntityBounds() { return null; }

    public Chunk getCurrentChunk() { return null; }

    public void moveY(double dx) { }

    public void onGround(boolean onGround) { }
}
