package src;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements Entity {
    
    // Location
    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;

    // Physics
    private double timeElapsed = 0;
    private double startY;
    private boolean isJumping;
    private boolean canJump;

    // Shape
    private int width;
    private int height;
    private final Image playerImage;

    // Game Panel
    private final GamePanel panel;

    public Player(GamePanel panel, int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;

        this.panel = panel;

        isJumping = false;
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
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Physics Accessors
    public boolean isJumping() { return isJumping; }
    public boolean canJump() { return canJump; }

    // Panel Dimension Accessors
    public Dimension getPanelDimensions() { return panel.getSize(); }

    @Override
    public void move(int direction)
    {
        dx = direction;

        if (x + dx > 0 && x + dx < panel.getWidth() - width)
            x += dx;
    }

    public void stopMoving() { dx = 0; }

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
                if (counter < (Physics.getMaxStep() * 0.33))
                    velocity = Physics.getSpeedScale() * counter;
                
                // Second half
                if (counter >= (Physics.getMaxStep() * 0.33))
                {
                    decrementCounter++;
                    velocity = -(Physics.getGravity() * decrementCounter);
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
        g2.drawImage(playerImage, x, y, width, height, null);
    }
}
