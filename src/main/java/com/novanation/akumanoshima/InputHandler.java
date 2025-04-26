package com.novanation.akumanoshima;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener{


    //Mouse handling 
    private static int mouseX = -1;
    private static int mouseY = -1;
    private static int mouseB = 1;
    private double angle;

    // for Jumping
    private boolean isMoving;
    private boolean canJump;

    Player player;
    AssualtWeapon ar;
    private VandalPerk vandalPerk = new VandalPerk();
    private VitalityPerk vitalityPerk = new VitalityPerk();
    private SpeedsterPerk speedsterPerk = new SpeedsterPerk();
    private int direction;

    // TODO: Temporary
    public int damage;
    public int health;

    PlayerAnimation playerAnimation;

    // In case of multiplayer, this will be instantiated
    public InputHandler(Player player) { 
        this.player = player; 
        this.playerAnimation = player.getPlayerAnimation();

        if (this.playerAnimation != null) {
            this.playerAnimation.start();
        }

        System.out.println("InputHandler initialized with playerAnimation: " + (this.playerAnimation != null));
    }

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
                playerAnimation.moveLeft(true);
            }
            case KeyEvent.VK_D -> {
                 isMoving = true;
                 direction = 1;
                 playerAnimation.moveRight(true);
            }
            case KeyEvent.VK_SPACE -> {
                if (!canJump)
                {
                    isMoving = true;
                    canJump = true;
                }
            }
            case KeyEvent.VK_V -> {
                if(!vandalPerk.isActive()) {
                    vandalPerk.applyEffect(player);
                    System.out.println("Vandal Perk Applied!");
                    vandalPerk.setActive(true);

                }
            }
            case KeyEvent.VK_B -> { 
                if(!vitalityPerk.isActive()) {
                  vitalityPerk.applyEffect(player);
                   vitalityPerk.setActive(true);
                 }
        }
        case KeyEvent.VK_N -> {
            if (!speedsterPerk.isActive()) {
                speedsterPerk.applyEffect(player);
            }
        }
    }
    }
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (direction == 1 && e.getKeyCode() == KeyEvent.VK_D){
            stopMovement();
            playerAnimation.moveLeft(false);
        }
        if (direction == -1 && e.getKeyCode() == KeyEvent.VK_A){
            stopMovement();
            playerAnimation.moveRight(false);
        }
      //  if (e.getKeyCode() == KeyEvent.VK_L){
         //   ar.setArIsFiring(false);
           

       // }
      
        // TODO: Temporary
        if (e.getKeyCode() == KeyEvent.VK_K)
            damage = 1;

        if (e.getKeyCode() == KeyEvent.VK_L)
            health = 1;
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            EnemyManager.killAllEntities();
    }

    private void stopMovement()
    {
        isMoving = false;
        direction = 0;
    }

    public void setWeapon(AssualtWeapon ar) {
        this.ar = ar;
    }

    @Override
    public void keyTyped(KeyEvent e) { }



    // Mouse controls

    //getters and setters
    public static int getMouseX() { return mouseX; }
    public static int getMouseY() { return mouseY; }
    public static int getButton() { return mouseB; }
    public double getAngle() { return angle; }


    //Mouse Methods

    private void updateMouseAngle(){
        double distX = mouseX - player.getX();
        double distY = mouseY - player.getY();

        angle = Math.atan2(distY, distX);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        updateMouseAngle();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        updateMouseAngle();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        
    }


    @Override
    public void mousePressed(MouseEvent e) {
       mouseB = e.getButton();
       if (e.getButton() == MouseEvent.BUTTON1) { // Left click
        ar.setArCanFire(true);
        ar.setArIsFiring(true);
    }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        mouseB = -1;
        if (e.getButton() == MouseEvent.BUTTON1) { // Left click released
            ar.setArIsFiring(false);
            ar.setArCanFire(false);
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
       
    }


    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
