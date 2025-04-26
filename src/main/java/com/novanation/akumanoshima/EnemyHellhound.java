package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class EnemyHellhound implements Entity {

    private final Health health;

    // Movement variables
    private int dy;
    private int dx;
    private int worldX;

    private double vy;

    // Location Variables
    private double xPos;
    private double yPos;

    // Design Variables
    private final int width;
    private final int height;

    private Chunk currentChunk;
    private boolean onGround;

    private Rectangle2D.Double entityBounds;

    private String enemyID;
    private final Image hellhoundImage;

    public EnemyHellhound(int width, int height, int xPos, int yPos, String enemyID)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.enemyID = enemyID;

        this.worldX = xPos;

        health = new Health();
        // TODO: Get Hellhound Image
        hellhoundImage = ImageManager.loadImage("/gfx/characters/char_hellhound.png");
    }

    @Override
    public void update()
    {
        int tileLength = WorldGeneration.getTileLength();
        currentChunk = WorldGeneration.getChunk((((int) xPos) / tileLength) * tileLength);

        entityBounds = new Rectangle2D.Double(xPos, yPos - 2, width, height);

        Chunk newChunk = WorldGeneration.getChunk((((int) xPos + tileLength) / tileLength) * tileLength);
        determineChunkTile(newChunk);
    }

    private void determineChunkTile(Chunk newChunk)
    {
        if (newChunk != null && newChunk.getTileType() == TileType.TERTIARY)
        {
            // TODO: Update enemy behaviour
            switch (newChunk.getWorldType())
            {
                case FOREST -> {
                }
                case VOLCANIC -> {
                }
                case BLIZZARD -> {
                }
                case END -> {}
            }
        }
    }

    public String getEnemyID() { return enemyID; }
    public void setEnemyID(String enemyID) { this.enemyID = enemyID; }

    @Override
    public int getHeight() { return height; }
    @Override
    public double getX() { return xPos; }
    @Override
    public double getY() { return yPos; }
    @Override
    public Health getHealth() { return health; }

    @Override
    public void setWorldPos(int xPos) { worldX += xPos; }

    @Override
    public void draw(Graphics2D g2) { g2.drawImage(hellhoundImage, (int) xPos, (int) yPos, width, height, null); }

    // Movement
    @Override
    public void move(int direction) { xPos += direction; }
    @Override
    public void moveY(double dx) { yPos += dx; }
    @Override
    public double getDY() { return dy; }
    @Override
    public void setY(double newY) { yPos = newY; }
    @Override
    public double getVelocityY() { return vy; }
    @Override
    public void setVelocityY(double vy) { this.vy = vy; }

    @Override
    public boolean isGrounded() { return onGround; }
    @Override
    public void setGrounded(boolean grounded) { onGround = grounded; }

    // Shape
    @Override
    public Rectangle2D.Double getEntityBounds() { return new Rectangle2D.Double(xPos, yPos, width, height); }
    @Override
    public Chunk getCurrentChunk() { return currentChunk; }

    // Actions
    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }
    @Override
    public void performAction() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'performAction'"); }
}
