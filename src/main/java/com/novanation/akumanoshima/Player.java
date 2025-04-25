package com.novanation.akumanoshima;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private Health health;
   
    // Shape and Collisions
    private int width;
    private int height;
    private Chunk currentChunk;
    private Rectangle2D.Double chunk;
    Rectangle2D.Double playerBounds;
    private final Image playerImage;

    // Temp
    private double thresholdX = 0;
    private boolean inThreshold = false;

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
    public int getDY() { return dy; }

    // Locational Accessors
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
    public int getHeight() { return height; }

    // Physics Accessors
    public boolean isJumping() { return isJumping; }
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
    public void onGround(boolean onGround) { }

    public void update()
    {
        playerBounds = new Rectangle2D.Double(x, y - 2, width, height);
    }

    @Override
    public Chunk getCurrentChunk()
    {
        int tileLength = WorldGeneration.getTileLength();
        return currentChunk = WorldGeneration.getChunk((((int) worldX) / tileLength) * tileLength);
    }

    public void stopMoving() { dx = 0; }

    @Override
    public void move(int direction)
    {
        dx = (int)(direction * moveSpeedMultiplier);

        if (x + dx > 0 && x + dx < panel.getWidth() - width)
            x += dx;
    }

    public void setThresholdX(double xVal) { thresholdX = xVal; }
    
    public boolean isColliding(int dx, boolean inThreshold)
    {
        int tileLength = WorldGeneration.getTileLength();

        this.inThreshold = inThreshold;

        if (inThreshold)
        {
            if (dx > 0)
            {
                Chunk nextChunk = WorldGeneration.getChunk((((int) thresholdX + tileLength) / tileLength) * tileLength);
                if (nextChunk != null) // Next chunk is not air and exists
                    return getCollision(nextChunk);
            }
            else if (dx < 0)
            {
                Chunk previousChunk = WorldGeneration.getChunk((((int) thresholdX - tileLength) / tileLength) * tileLength);
                if (previousChunk != null) // Previous chunk is not air and exists
                    return getCollision(previousChunk);
            }
        }
        
        // Check collision with the next chunk
        if (dx > 0)
        {
            Chunk nextChunk = WorldGeneration.getChunk((((int) x + tileLength) / tileLength) * tileLength);
            if (nextChunk != null) // Next chunk is not air and exists
                return getCollision(nextChunk);
                
        }
        else if (dx < 0)
        {
            Chunk previousChunk = WorldGeneration.getChunk((((int) x - tileLength) / tileLength) * tileLength);
            if (previousChunk != null) // Previous chunk is not air and exists
                return getCollision(previousChunk);
        }

        return false;
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
        isJumping = true;
        canJump = true;

        Timer timer = new Timer();

        // Perform jump over a period of time
        TimerTask jumpTask = new TimerTask() {
            int counter = 0;
            int countdown = 1;

            int tileHeight = WorldGeneration.getTileLength();
            int decrementCounter = 0;
            double velocity = 0;

            @Override
            public void run() {
                counter++;
                // Perform jump actions

                // First half
                if (counter < (Physics.getMaxStep() * 0.20))
                    velocity = Physics.getSpeedScale() * counter;
                
                // Second half
                if (counter >= (Physics.getMaxStep() * 0.20))
                {
                    decrementCounter++;
                    velocity = -(Physics.getGravity() * decrementCounter * 2);
                }

                // Prevent a jump loop
                if (counter + Physics.getJumpInterval() >= Physics.getMaxStep())
                    canJump = false;

                if (counter >= Physics.getMaxStep())
                {
                    isJumping = false;
                    timer.cancel();
                }

                // End jump task on ground contact
                if ((y - velocity) + height < panel.getHeight() - tileHeight)
                {
                    // Stop performing the jump and end the task
                    if (!canJump)
                    {
                        if (countdown == 0)
                        {
                            isJumping = false;
                            timer.cancel();
                            return;
                        }
                        else
                            countdown--;
                    }

                    double newY = y - velocity;

                    if (!(newY + height + 25 > panel.getHeight() - tileHeight))
                        performJumpAction(velocity);
                    else
                        canJump = false;
                }
            }
        };

        timer.scheduleAtFixedRate(jumpTask, 0, Physics.getJumpInterval());
    }

    private void performJumpAction(double velocity)
    {
        double newY = y - velocity;
        double newX = Physics.calculateHorizComponent(velocity, 0);

        int tileHeight = WorldGeneration.getTileLength();

        // x += newX;

        if (newY + 25 > 0 && newY + height + 25 < panel.getHeight() - tileHeight)
            y = (int) newY;
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
