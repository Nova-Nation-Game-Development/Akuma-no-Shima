package com.novanation.akumanoshima;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Player implements Entity {
    
    // Location
    private double x;
    private double y;
    private int worldX;

    private int dx = 0;
    private int dy = 0;

    //private double health;

    // Physics
    private double timeElapsed = 0;
    private double startY;
    private boolean isJumping;
    private boolean canJump;
    private boolean onGround;

    private double vy;

    private Health health;
   
    // Shape and Collisions
    private int width;
    private int height;

    private Chunk currentChunk;
    private Chunk previousChunk;
    private Chunk nextChunk;
    
    private Rectangle2D.Double chunk;
    Rectangle2D.Double playerBounds;
    private final Image playerImage;

    // Game Panel
    private final GamePanel panel;

    //Perks
    private List<Perk> activePerks = new ArrayList<>();
    private double moveSpeedMultiplier = 1.0;
    private Weapon currentWeapon;

    public Player(GamePanel panel, int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;

        this.panel = panel;

        isJumping = false;
        playerImage = ImageManager.loadImage("/gfx/characters/char_noroi.png");
        health = new Health();
    }

    // Directional mutators
    public void setDX(int newDX) { dx = newDX; }
    public void setDY(int newDY) { dy = newDY; }
    // Directional accessors
    public int getDX() { return dx; }
    @Override
    public double getDY() { return dy; }

    // Locational Accessors
    @Override
    public double getX() { return x; }
    @Override
    public double getY() { return y; }
    // Locational Mutators
    public void setWorldPos(int xPos) { worldX = xPos; }

    // Shape Mutators
    public void setWidth(int newWidth) { width = newWidth; }
    public void setHeight(int newHeight) { height = newHeight; }
    // Shape Accessors
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }

    // Physics Accessors
    public boolean isJumping() { return !isGrounded(); }
    public boolean canJump() { return canJump; }

    @Override
    public Health getHealth() { return health; }

    // Panel Dimension Accessors
    public Dimension getPanelDimensions() { return panel.getSize(); }

    //Perk Accessors
    public Weapon getWeapon() { return currentWeapon; }
    public void setCurrentWeapon(Weapon weapon) { currentWeapon = weapon; }

    public void applyPerk(Perk perk) {
        if (!activePerks.contains(perk)) {
            activePerks.add(perk);
            perk.applyEffect(this);
        }
    }

    public void removePerk(Perk perk) {
        if (activePerks.contains(perk)) {
            activePerks.remove(perk);
            perk.removeEffect(this);
        }
    }

    public void setMoveSpeedMultiplier(double multiplier) {
        this.moveSpeedMultiplier = multiplier;
    }

    public double getMoveSpeedMultiplier() {
        return moveSpeedMultiplier;
    }

    public int getWorldX() { return worldX; }

    @Override
    public Rectangle2D.Double getEntityBounds() { return playerBounds; }

    @Override
    public void moveY(double dx) { y += dx; }

    @Override
    public void setY(double newY) { y = newY; }

    @Override
    public boolean isGrounded() { return onGround; }
    @Override
    public void setGrounded(boolean grounded) { onGround = grounded; }

    @Override
    public double getVelocityY() { return vy; }
    @Override
    public void setVelocityY(double vy) { this.vy = vy; }

    public void update()
    {
        int tileLength = WorldGeneration.getTileLength();
        currentChunk = WorldGeneration.getChunk((((int) worldX) / tileLength) * tileLength);

        playerBounds = new Rectangle2D.Double(x, y - 2, width, height);

        Chunk newChunk = WorldGeneration.getChunk((((int) worldX + tileLength) / tileLength) * tileLength);
        determineChunkTile(newChunk);
    }

    @Override
    public Chunk getCurrentChunk() { return currentChunk; }

    public void stopMoving() { dx = 0; }

    @Override
    public void move(int direction)
    {
        dx = (int)(direction * moveSpeedMultiplier);

        if (x + dx > 0 && x + dx < panel.getWidth() - width)
            x += dx;
    }
    
    public boolean isColliding(int dx)
    {
        int tileLength = WorldGeneration.getTileLength();

        if (dx > 0)
        {
            nextChunk = WorldGeneration.getChunk((((int) worldX + tileLength) / tileLength) * tileLength);
            if (nextChunk != null) // Next chunk is not air and exists
                return getCollision(nextChunk);
        }
        else if (dx < 0)
        {
            previousChunk = WorldGeneration.getChunk((((int) worldX - tileLength) / tileLength) * tileLength);
            if (previousChunk != null) // Previous chunk is not air and exists
                return getCollision(previousChunk);
        }

        return false;
    }

    private void determineChunkTile(Chunk newChunk)
    {
        if (newChunk != null && newChunk.getTileType() == TileType.TERTIARY)
        {
            // TODO: Reduce player speed, damage player or increase player speed
            switch (newChunk.getWorldType())
            {
                case FOREST -> {
                    System.out.println("In Water!");
                }
                case VOLCANIC -> {
                    System.out.println("In Lava!");
                }
                case BLIZZARD -> {
                    System.out.println("On Ice!");
                }
                case END -> {}
            }
        }
    }

    private boolean getCollision(Chunk newChunk)
    {
        // Check collision
        chunk = newChunk.getChunkBounds();
        return playerBounds.intersects(chunk) || chunk.contains(playerBounds);
    }

    public int getCollisionDirection()
    {
        if (playerBounds.getX() < chunk.getX()) // Player is on the left
            return 0;
        return 1; // Otherwise, player is on the right
    }

    @Override
    public void jump()
    {
        if (onGround)
        {
            setVelocityY(-16);
            onGround = false;
        }
    }

    @Override
    public void performAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performAction'");
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(playerImage, (int) x, (int) y, width, height, null);
    }  
}
