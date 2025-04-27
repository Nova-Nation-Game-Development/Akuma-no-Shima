package com.novanation.akumanoshima;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class EnemyMaou implements Entity {

    private final Health health;

    // Movement variables
    private int dy;
    private int dx;
    private int worldX;

    private String id;
    private boolean isDead;

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

    // Design variables
    private String enemyID;
    private final Image maouImage;
    private final Image explosionGif;

    public EnemyMaou(int width, int height, int xPos, int yPos, String enemyID, GamePanel panel)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.enemyID = enemyID;

        this.worldX = xPos;

        isDead = false;

        health = new Health(false);
        health.setMaxHealth(10);
        health.setCurrentHealth(health.getMaxHealth());

        maouImage = ImageManager.loadImage("/gfx/characters/char_maou.png");
        explosionGif = (ImageManager.loadGif("/gfx/animations/maou/death/explosion.gif")).getImage();
    }

    @Override
    public void setID(String id) { this.id = id; }
    @Override
    public String getID() { return id; }

    @Override
    public void update()
    {
        int tileLength = WorldGeneration.getTileLength();
        currentChunk = WorldGeneration.getChunk((((int) xPos) / tileLength) * tileLength);

        if ("DESTROYED".equals(enemyID))
            entityBounds = null;
        else
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
    public void draw(Graphics2D g2)
    {
        if (isDead)
            g2.drawImage(explosionGif, (int) xPos, (int) yPos, width, height, null);
        else
        {
            g2.drawImage(maouImage, (int) xPos, (int) yPos, width, height, null);
            drawHealthBar(g2);
        }
    }

    public void drawHealthBar(Graphics2D g2) {
        int healthBarWidth = 150;
        int healthBarHeight = 10;
        double healthBarX = xPos - (healthBarWidth / 2) + 30;
        double healthBarY = yPos - 10;
        
        // Draw background (red)
        g2.setColor(Color.RED);
        g2.fillRect((int) healthBarX, (int) healthBarY, healthBarWidth, healthBarHeight);
        
        // Draw remaining health (green)
        g2.setColor(Color.GREEN);
        int currentHealthWidth = (int)((health.getCurrentHealth() / (float) health.getMaxHealth()) * healthBarWidth);
        g2.fillRect((int) healthBarX, (int) healthBarY, currentHealthWidth, healthBarHeight);

        g2.setColor(Color.WHITE);
        String healthText = (health.getCurrentHealth()) + "/" + (health.getMaxHealth());
        g2.drawString(healthText, (int)healthBarX, (int)healthBarY - 2);
    }

    // This isn't needed for this particular enemy
    @Override
    public Chunk getNextChunk() { return null; }
    @Override
    public Chunk getPreviousChunk() { return null; }

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
    public Rectangle2D.Double getEntityBounds() { return entityBounds; }
    @Override
    public Chunk getCurrentChunk() { return currentChunk; }

    // Actions
    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }
    @Override
    public void performAction() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'performAction'"); }

    private void actionSummon() {}
    private void actionCharge() {}
    private void actionShoot() {}
    private void actionSlam() {}

    public void setDefeated(boolean isDead) { this.isDead = isDead; }
}
