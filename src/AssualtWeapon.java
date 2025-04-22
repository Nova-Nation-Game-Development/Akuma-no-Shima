package src;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class AssualtWeapon implements Weapon {

    private final int fireRate = 200;
    
    private AmmoStock ammo;
    private InputHandler inputHandler;
    private Player player;
    private static final int ammoCount = 30;
    private int currentBulletIndex = 0;
    private ArrayList<Bullet> usableBullets;

    //weapon conditions
    private boolean arFiring;
    private boolean arCanFire;
    private boolean arIsEmpty;
    private long lastShotTime;

    public AssualtWeapon(Player player){
        ammo = new AmmoStock(ammoCount);
        this.usableBullets = ammo.bullets;
        arFiring = false;
        arCanFire = true;
        this.player = player;
        

    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }


    @Override
    public void updateShooting(){
        long currTime = System.currentTimeMillis();

        if(arFiring && arCanFire) {

            if(currTime - lastShotTime >= fireRate) {

                if(currentBulletIndex < ammoCount) {
                    double angle = inputHandler.getAngle();
                    System.out.println("Firing angle: " + Math.toDegrees(angle));
                    usableBullets.get(currentBulletIndex).spawn(player.getX(), player.getY()+50, angle);
                    currentBulletIndex++;
                    lastShotTime = currTime;
                }
            }
        }

        
        for(int i = 0; i < currentBulletIndex; i++) {
            usableBullets.get(i).move();
            //System.out.println("Bullet " + i + " position: " + usableBullets.get(i).getX() + ", " + usableBullets.get(i).getY());
        }
        
    }

    public void drawBullets(Graphics2D g2) {
        if (g2 != null) {
            for(int i = 0; i < ammoCount; i++) {
                if (usableBullets != null && i < usableBullets.size()) {
                    usableBullets.get(i).draw(g2);
                }
            }
        }
    }

    @Override
    public void reload() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reload'");
    }

    public boolean isArFiring() {
        return arFiring;
    }

    public void setArIsFiring(boolean arFiring) {
        this.arFiring = arFiring;
    }

    public void setArCanFire(boolean arCanFire) {
        this.arCanFire= arCanFire;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    
    
    
    
}
