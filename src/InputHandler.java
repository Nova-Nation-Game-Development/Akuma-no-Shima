package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private final Player playerEntity;

    private boolean isMoving;
    private int direction;

    public InputHandler(Player player)
    {
        this.playerEntity = player;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

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
                playerEntity.jump();
                // backgroundManager.jump();
            }
        }
    }

    public int getDirection() { return direction; }
    public boolean isMoving() { return isMoving; }

    @Override
    public void keyReleased(KeyEvent e)
    {
        isMoving = false;
        direction = 0;
    }
}
