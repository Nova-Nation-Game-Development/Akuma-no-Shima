package com.novanation.akumanoshima;


import java.awt.Graphics2D;
import java.util.ArrayList;

public class AssualtWeapon implements Weapon {

    private final int fireRate = 200;
    private final int BASE_AMMO = 10;
    
    private AmmoStock ammo;
    private InputHandler inputHandler;
    private Player player;
    public int ammoCount = 10;
    private int currentBulletIndex = 0;
    private ArrayList<Bullet> usableBullets;

    //weapon conditions
    private boolean arFiring;
    private boolean arCanFire;
    private boolean arIsEmpty;
    private long lastShotTime;
    private long reloadStartTime;
    private boolean isReloading;
    private double reloadSpeedMultiplier = 1.0;
    private double ammoMultiplier = 1.0;
    private double damageMultiplier = 1.0;

    public AssualtWeapon(Player player){
        this.player = player;
        // Set the weapon on the player after the constructor finishes
        this.player.setCurrentWeapon(this);
        initializeAmmo();
        arFiring = false;
        arCanFire = true;
        

    }

    private void initializeAmmo() {
        int totalAmmo = (int)(ammoCount * ammoMultiplier);
        ammo = new AmmoStock(totalAmmo);
        usableBullets = ammo.bullets;
        System.out.println("Initialized weapon with " + totalAmmo + " bullets");
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }


    @Override
    public void updateShooting(){
        long currTime = System.currentTimeMillis();

        if(arFiring && arCanFire && !isReloading) {

            if(currTime - lastShotTime >= fireRate) {

                if(currentBulletIndex < ammoCount * ammoMultiplier) {
                    double angle = inputHandler.getAngle();
                    System.out.println("Firing angle: " + Math.toDegrees(angle));
                    usableBullets.get(currentBulletIndex).spawn(player.getX(), player.getY()+50, angle);
                    currentBulletIndex++;
                    lastShotTime = currTime;
                } else {
                    arIsEmpty = true;
                    arCanFire = false;
                    isReloading = true; // Start reloading automatically
                    reloadStartTime = System.currentTimeMillis();
                    System.out.println("Out of ammo! Reloading...");
                }
            }
        }

        
        for(int i = 0; i < currentBulletIndex; i++) {
            usableBullets.get(i).move();
            //System.out.println("Bullet " + i + " position: " + usableBullets.get(i).getX() + ", " + usableBullets.get(i).getY());
        }
        
    }
    @Override
    public void drawBullets(Graphics2D g2) {
        if (g2 != null) {
            for(int i = 0; i < currentBulletIndex; i++) {
                if (usableBullets != null && i < usableBullets.size()) {
                    usableBullets.get(i).draw(g2);
                }
            }
        }
    }

   

    @Override
    public void reload() { // not in use
      //  if (!isReloading && currentBulletIndex > 0) {
       //     isReloading = true;
       //     reloadStartTime = System.currentTimeMillis();
         //   System.out.println("Reloading...");
       // }
    }

    public void updateReloading(){
        if (isReloading) {
            long reloadTime = (long) (2000 * reloadSpeedMultiplier);
            if (System.currentTimeMillis() - reloadStartTime >= reloadTime) {
                initializeAmmo(); // Reinitialize ammo with current multiplier
                currentBulletIndex = 0;
                
                // Reset weapon states
                arIsEmpty = false;
                arCanFire = true;
                isReloading = false;
                System.out.println("Reload complete! New ammo count: " + usableBullets.size());
            }
        }
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

    @Override
    public void setAmmoMultiplier(double multiplier) {
        this.ammoMultiplier = multiplier;
        // Reinitialize ammo when multiplier changes
        int totalAmmo = (int)(BASE_AMMO * ammoMultiplier);
        ammo = new AmmoStock(totalAmmo);
        usableBullets = ammo.bullets;
        System.out.println("Reinitialized weapon with " + totalAmmo + " bullets after multiplier change");
    }

    @Override
    public void setReloadSpeedMultiplier(double multiplier) {
        this.reloadSpeedMultiplier = multiplier;
    }

    @Override
    public void resetAmmoMultiplier() {
        this.ammoMultiplier = 1.0;
    }

    @Override
    public void resetReloadSpeedMultiplier() {
        this.reloadSpeedMultiplier = 1.0;
    }

    public AmmoStock getAmmo() {
        return ammo;
    }

    public void setAmmo(AmmoStock ammo) {
        this.ammo = ammo;
    }

    public ArrayList<Bullet> getUsableBullets() {
        return usableBullets;
    }

    public void setUsableBullets(ArrayList<Bullet> usableBullets) {
        this.usableBullets = usableBullets;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    // Getter for BASE_AMMO
    public int getBaseAmmo() {
        return BASE_AMMO;
    }

    // Getter and setter for ammoMultiplier
    public double getAmmoMultiplier() {
        return ammoMultiplier;
    }

    // Getter and setter for reloadSpeedMultiplier
    public double getReloadSpeedMultiplier() {
        return reloadSpeedMultiplier;
    }

    // Getter for current bullet index
    public int getCurrentBulletIndex() {
        return currentBulletIndex;
    }
     // Getter for isReloading
     public boolean isReloading() {
        return isReloading;
    }
    
    
    
}
