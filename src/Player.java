package src;

import java.awt.Graphics2D;
import java.awt.Image;

public class Player extends Physics implements Entity {
    
    // Location
    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;
    double timeElapsed = 0;
    double startY;
    int moveDirection;
    

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
        dx = 10;

        if (x + dx > 0 && x + dx < panel.getWidth() - width) {
            if (direction == 1) {
                x -= dx; // left
                moveDirection = -1;
            }
            if (direction == 2) {
                x += dx; // right
                moveDirection = 1;
            }
            if (direction == 3 && !isJumping) {
                jump(); // jump
                
            }
        }

        System.out.println("moving");
    }


    public void update() {
       double vertDisplacement = 0;
       double horizDisplacement = 0;
       double newY;
       double newX;
       

        if (isJumping) {
            timeElapsed++;
            vertDisplacement = (int) calculateVertComponent(2, timeElapsed);
            
        newY = (int) (startY - vertDisplacement);
        y = (int) newY;


        if(moveDirection != 0){
            horizDisplacement = (int) calculateHorizComponent(moveDirection, timeElapsed);
            newX = x + horizDisplacement;

            if (newX > 0 && newX < panel.getWidth() - 500) {
                x = (int) newX;
            }
        }

            //  ground collision
            if (y >= panel.getHeight() - 264) {
                y = panel.getHeight() - 264;
                resetJumpPos();
                moveDirection = 0;
            }

        }

        System.out.println("displacement: " + vertDisplacement + "  Time elasped: " + timeElapsed + "  y: " + y);
    }


    public void stopMoving() {
         dx = 0; 
        moveDirection = 0;
    }

    @Override
    public void jump() 
    {
        System.out.println("Jumping");
       if(!isJumping){
        startJump(y, x);
        timeElapsed = 0;     // logic with isJumping needs to be seperated accordingly
      }
       
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
