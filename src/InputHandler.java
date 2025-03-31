package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private final Player playerEntity;
    private final BackgroundManager backgroundManager;

    private final int dx = 10;
    private final int worldSpeed = 5;

    public InputHandler(Player player, BackgroundManager backgroundManager)
    {
        this.playerEntity = player;
        this.backgroundManager = backgroundManager;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {

        // I will temporarily configure the physics here for the player
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                if (playerEntity.getX() - dx > 0)
                {
                    playerEntity.move(-dx);
                    backgroundManager.move(-1);
                    WorldGeneration.move(-worldSpeed);
                }
            }
            case KeyEvent.VK_D -> {
                 // Prevent the background from scrolling too much
                if (playerEntity.getX() + dx < 1000)
                {
                    backgroundManager.move(1);
                    WorldGeneration.move(worldSpeed);
                }

                playerEntity.move(dx);
            }
            case KeyEvent.VK_SPACE -> {
                playerEntity.jump();
                // backgroundManager.jump();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { playerEntity.stopMoving();}
}
