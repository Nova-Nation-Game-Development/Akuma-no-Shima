package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private final Player playerEntity;

    public InputHandler(Player player) { this.playerEntity = player; }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {

        // I will temporarily configure the physics here for the player
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> playerEntity.move();
            case KeyEvent.VK_RIGHT -> playerEntity.move();
            case KeyEvent.VK_SPACE -> playerEntity.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");

        // playerEntity.stopMoving();
    }
    
}
