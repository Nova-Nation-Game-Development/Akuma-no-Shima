package com.novanation.akumanoshima;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class EnemyOni implements Entity {

    private final Health health;

    // Movement variables
    private int dy;
    private int dx;
    private int worldX;

    private double vy;

    // Location Variables
    private double xPos;
    private double yPos;

    // Design Variables
    private final int width;
    private final int height;

    private Chunk currentChunk;
    private boolean onGround;

    private Rectangle2D.Double chunk;
    private Chunk previousChunk;
    private Chunk nextChunk;

    private Rectangle2D.Double entityBounds;

    private String enemyID;
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

    private final GamePanel panel;

    public EnemyOni(int width, int height, int xPos, int yPos, String enemyID, GamePanel panel)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.enemyID = enemyID;

        this.panel = panel;

        this.worldX = xPos;
        this.projectiles = new ArrayList<>();

        health = new Health();
        oniImage = ImageManager.loadImage("/gfx/characters/char_oni.png");
    }

    @Override
    public void update()
    {
        int tileLength = WorldGeneration.getTileLength();
        currentChunk = WorldGeneration.getChunk((((int) worldX) / tileLength) * tileLength);

        entityBounds = new Rectangle2D.Double(xPos, yPos - 2, width, height);
        
        if (targetPlayer == null)
            targetPlayer = panel.getPlayerEntity();

        Chunk newChunk = WorldGeneration.getChunk((((int) worldX + tileLength) / tileLength) * tileLength);
        determineChunkTile(newChunk);
    }

    private void determineChunkTile(Chunk newChunk)
    {
        if (newChunk != null && newChunk.getTileType() == TileType.TERTIARY)
        {
            // TODO: Update enemy behaviour
            switch (newChunk.getWorldType())
            {
                case FOREST -> {
                }
                case VOLCANIC -> {
                }
                case BLIZZARD -> {
                }
                case END -> {}
            }
        }
    }

    public void drawHealthBar(Graphics2D g2) {
        int healthBarWidth = 50;
        int healthBarHeight = 5;
        double healthBarX = xPos + (width - healthBarWidth) / 2;
        double healthBarY = yPos - 10;
        
        // Draw background (red)
        g2.setColor(Color.RED);
        g2.fillRect((int) healthBarX, (int) healthBarY, healthBarWidth, healthBarHeight);
        
        // Draw remaining health (green)
        g2.setColor(Color.GREEN);
        int currentHealthWidth = (int)((currentHealth / (float)MAX_HEALTH) * healthBarWidth);
        g2.fillRect((int) healthBarX, (int) healthBarY, currentHealthWidth, healthBarHeight);

        g2.setColor(Color.WHITE);
        String healthText = currentHealth + "/" + MAX_HEALTH;
        g2.drawString(healthText, (int)healthBarX, (int)healthBarY - 2);
    }
    
    public String getEnemyID() { return enemyID; }
    public void setEnemyID(String enemyID) { this.enemyID = enemyID; }

    @Override
    public int getHeight() { return height; }
    @Override
    public double getX() { return xPos; }
    @Override
    public double getY() { return yPos; }
    @Override
    public Health getHealth() { return health; }

    @Override
    public void setWorldPos(int xPos) { worldX += xPos; }
    public void draw(Graphics2D g2) {

        g2.drawImage(oniImage, (int) xPos, (int) yPos, width, height, null);
        drawHealthBar(g2);
        for (EnemyProjectile projectile : projectiles) {
            projectile.draw(g2);
        }
     }


     public void takeDamage(int damage) {
        System.out.println("EnemyOni taking damage: " + damage + ", Current health: " + currentHealth);
        currentHealth -= damage;
        if(currentHealth <= 0) {
            currentHealth = 0;
            health.dealDamage(MAX_HEALTH, EntityType.ONI, this);
        }
        System.out.println("EnemyOni health after damage: " + currentHealth);
    }

   /*  private void checkProjectileCollisions() {
        for (EnemyProjectile projectile : projectiles) {
            if (projectile.checkCollision(targetPlayer)) {
                targetPlayer.getHealth().dealDamage(FIREBALL_DAMAGE, EntityType.PLAYER, targetPlayer);
            }
        }
    }
*/


    // Movement
    @Override
    public void move(int direction) { xPos += direction; }
    @Override
    public void moveY(double dx) { yPos += dx; }
    @Override
    public double getDY() { return dy; }
    @Override
    public void setY(double newY) { yPos = newY; }
    @Override
    public double getVelocityY() { return vy; }
    @Override
    public void setVelocityY(double vy) { this.vy = vy; }

    @Override
    public boolean isGrounded() { return onGround; }
    @Override
    public void setGrounded(boolean grounded) { onGround = grounded; }

    // Shape
    @Override
    public Rectangle2D.Double getEntityBounds() { return new Rectangle2D.Double(xPos, yPos, width, height); }
    @Override
    public Chunk getCurrentChunk() { return currentChunk; }

    // Actions
    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }
    @Override
    public void performAction() { 
        long currentTime = System.currentTimeMillis();
        
        // Check attack timing
        if (currentTime - lastAttackTime >= ATTACK_INTERVAL) {
            // Use world position for distance check
            if (targetPlayer != null)
            {
                double distanceToPlayer = Math.abs(xPos - targetPlayer.getWorldX());
            
                if (distanceToPlayer <= ATTACK_RANGE) {
                    shootFireball();
                    lastAttackTime = currentTime;
                }
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
        double playerWorldX = targetPlayer.getWorldX() + targetPlayer.getWidth()/2;
        double playerScreenX = targetPlayer.getX() + targetPlayer.getWidth()/2;
        double playerScreenY = targetPlayer.getY() + targetPlayer.getHeight()/2;
        
        double targetX = playerScreenX;
        double targetY = playerScreenY;


        // Calculate control point for arc
        double controlX = (startX + targetX) / 2;
        double controlY = Math.min(startY, targetY) - 400;
        
        projectile.setPanel(panel);

        projectile.spawn(startX, startY, 0);
        projectile.setTargetPoints(startX, startY, controlX, controlY, targetX, targetY);
        projectiles.add(projectile);
    }

    public void moveWithWorld(int worldSpeed)
    {
        if (projectiles == null) return;

        for (EnemyProjectile proj : projectiles)
            proj.moveWithWorld(worldSpeed);
    }

    private void updateProjectiles() {
        projectiles.removeIf(p -> !p.isActive());
        for (EnemyProjectile projectile : projectiles) {
            projectile.move();
            //collision
            if (projectile.isActive() && targetPlayer != null) {
                projectile.checkCollision(targetPlayer);
            }
        }
    }

    public List<EnemyProjectile> getProjectiles() {
        return projectiles;
    }

}
