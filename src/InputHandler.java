package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private boolean isMoving;
    private boolean canJump;

    private int direction;

    // In case of multiplayer, this will be instantiated
    public InputHandler() { }

    // Accessors
    public boolean canJump() { return canJump; }
    public int getDirection() { return direction; }
    public boolean isMoving() { return isMoving; }

    // Mutators
    public void stopJump() { canJump = false; }

    @Override
    public void keyPressed(KeyEvent e) {
        // I will temporarily configure the physics here for the player
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                isMoving = true;
                direction = -1;
            }
            case KeyEvent.VK_D -> {
                 isMoving = true;
                 direction = 1;
            }
            case KeyEvent.VK_SPACE -> {
                if (!canJump)
                {
                    isMoving = true;
                    canJump = true;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (direction == 1 && e.getKeyCode() == KeyEvent.VK_D)
            stopMovement();
        if (direction == -1 && e.getKeyCode() == KeyEvent.VK_A)
            stopMovement();
    }

    private void stopMovement()
    {
        isMoving = false;
        direction = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
