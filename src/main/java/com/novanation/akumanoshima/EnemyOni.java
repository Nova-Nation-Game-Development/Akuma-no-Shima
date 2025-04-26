package com.novanation.akumanoshima;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class EnemyOni implements Entity {

    private final Health health;

    // Location Variables
    private int xPos;
    private int yPos;

    // Design Variables
    private final int width;
    private final int height;

    private final String enemyID;

    private final Image oniImage;



    //Attacking variables
     private static final int ATTACK_RANGE = 400; // Horizontal range to start attacking
    private static final int ATTACK_INTERVAL = 3000; // Milliseconds between shots
    private long lastAttackTime = 0;
    private List<EnemyProjectile> projectiles = new ArrayList<>();
    private Player targetPlayer;

    private static final int MAX_HEALTH = 100;
    private int currentHealth = MAX_HEALTH;
    private static final int FIREBALL_DAMAGE = 25;

    public EnemyOni(int width, int height, int xPos, int yPos, String enemyID, Player player)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.enemyID = enemyID;
        this.targetPlayer = player;
        this.projectiles = new ArrayList<>();

        health = new Health();
        oniImage = ImageManager.loadImage("/gfx/characters/char_oni.png");
    }

    public void drawHealthBar(Graphics2D g2) {
        int healthBarWidth = 50;
        int healthBarHeight = 5;
        int healthBarX = xPos + (width - healthBarWidth) / 2;
        int healthBarY = yPos - 10;
        
        // Draw background (red)
        g2.setColor(Color.RED);
        g2.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        
        // Draw remaining health (green)
        g2.setColor(Color.GREEN);
        int currentHealthWidth = (int)((currentHealth / (float)MAX_HEALTH) * healthBarWidth);
        g2.fillRect(healthBarX, healthBarY, currentHealthWidth, healthBarHeight);
    }
    
    public String getEnemyID() { return enemyID; }

    @Override
    public void draw(Graphics2D g2) {

        g2.drawImage(oniImage, xPos, yPos, width, height, null);
        drawHealthBar(g2);
        for (EnemyProjectile projectile : projectiles) {
            projectile.draw(g2);
        }
     }


     public void takeDamage(int damage) {
        currentHealth -= damage;
        if(currentHealth <= 0) {
            health.dealDamage(MAX_HEALTH, EntityType.ONI, this);
        }
    }

   /*  private void checkProjectileCollisions() {
        for (EnemyProjectile projectile : projectiles) {
            if (projectile.checkCollision(targetPlayer)) {
                targetPlayer.getHealth().dealDamage(FIREBALL_DAMAGE, EntityType.PLAYER, targetPlayer);
            }
        }
    }
*/

    @Override
    public void move(int direction) { xPos += direction; }

    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }

    @Override
    public void performAction() { 
        long currentTime = System.currentTimeMillis();
        
        // Check attack timing
        if (currentTime - lastAttackTime >= ATTACK_INTERVAL) {
            // Use world position for distance check
            double distanceToPlayer = Math.abs(xPos - targetPlayer.getWorldX());
            
            if (distanceToPlayer <= ATTACK_RANGE) {
                shootFireball();
                lastAttackTime = currentTime;
            }
        }

        // Update active projectiles
        updateProjectiles();
        //checkProjectileCollisions();
       
}


private void shootFireball() {
    EnemyProjectile projectile = new EnemyProjectile();
    
    // Calculate trajectory points
    double startX = xPos + width/2;
    double startY = yPos + height/2;
    double endX = targetPlayer.getX();
    double endY = targetPlayer.getY();
    
    // Calculate control point for arc
    double controlX = (startX + endX) / 2;
    double controlY = Math.min(startY, endY) - 200;
    
    projectile.spawn(startX, startY, 0);
    projectile.setTargetPoints(startX, startY, controlX, controlY, endX, endY);
    projectiles.add(projectile);
}


private void updateProjectiles() {
    projectiles.removeIf(p -> !p.isActive());
    for (EnemyProjectile projectile : projectiles) {
        projectile.move();
    }
}

public List<EnemyProjectile> getProjectiles() {
    return projectiles;
}

    @Override
    public Rectangle2D.Double getEntityBounds() { return new Rectangle2D.Double(xPos, yPos, width, height); }

    @Override
    public Chunk getCurrentChunk() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCurrentChunk'"); }

    @Override
    public void onGround(boolean onGround) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onGround'"); }

    @Override
    public void moveY(double dx) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'moveY'"); }
}
