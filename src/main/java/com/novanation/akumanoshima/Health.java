package com.novanation.akumanoshima;


import java.awt.Graphics2D;
import java.awt.Image;

public class Health {

    private final int heartSize = 32; // Size in pixels
    private final int heartOffset = 48; // Distance in pixels

    private final Image healthImage;
    private final Image damagedHealthImage;
    private final Image bonusHealthImage;

    // Health values
    private final int MAX_HP = 5;
    private final int DEFAULT_HP = 3;
    private int currentHealth = 3;

    public Health()
    {
        healthImage = ImageManager.loadImage("/gfx/images/ui/heart_filled.png");
        damagedHealthImage = ImageManager.loadImage("/gfx/images/ui/heart_empty.png");
        bonusHealthImage = ImageManager.loadImage("/gfx/images/ui/heart_filled_bonus.png");
    }

    public void dealDamage(int damage, EntityType entityType, Entity entity)
    {
        damage = Math.abs(damage); // In case of damage being negative

        if (currentHealth - damage <= 0)
            switch (entityType) {
                case PLAYER -> { killPlayer(); }
                case ONI -> { destroyEntity(entity); }
                case HELLHOUND -> { destroyEntity(entity); }
                case MAOU -> { killFinalBoss(); }
            }
        else
            currentHealth -= damage;
    }

    public void destroyEntity(Entity entity)
    {
        EnemyManager.destroyEntity(entity);
    }

    // TODO: Complete (Add Death Screen, etc)
    public void killPlayer() { currentHealth = 0; System.out.println("You Died!");}

    public void killFinalBoss() {}

    public void addHealth(int hp)
    {
        hp = Math.abs(hp); // In case of health being negative

        if (hp + currentHealth > MAX_HP)
            currentHealth = 5;
        else
            currentHealth += hp;
    }

    public void draw(Graphics2D g2)
    {
        if (currentHealth <= DEFAULT_HP)
            for (int i = 0; i < currentHealth; i++)
                g2.drawImage(healthImage, (i * heartOffset) + (heartOffset / 2), (heartOffset / 2), heartSize, heartSize, null);

        if (currentHealth > DEFAULT_HP)
        {
            for (int i = 0; i < DEFAULT_HP; i++)
                g2.drawImage(healthImage, (i * heartOffset) + (heartOffset / 2), (heartOffset / 2), heartSize, heartSize, null);

            for (int i = DEFAULT_HP; i < currentHealth; i++)
                g2.drawImage(bonusHealthImage, (i * heartOffset) + (heartOffset / 2), (heartOffset / 2), heartSize, heartSize, null); 
        }

        if (currentHealth < DEFAULT_HP)
        {
            for (int i = currentHealth; i < DEFAULT_HP; i++)
                g2.drawImage(damagedHealthImage, (i * heartOffset) + (heartOffset / 2), (heartOffset / 2), heartSize, heartSize, null);
        }
            
    }
}
