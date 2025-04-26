package com.novanation.akumanoshima;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Bullet implements Projectile{

    private double x;
    private double y;
    private final double height = 10;
    private final double width = 10;
    private final double speed = 6;
    private double angle;
    private static final int BULLET_DAMAGE = 10; // 10 bullets to kill an Oni
    private boolean active = true;

    @Override
    public void move() {
       x += speed * Math.cos(angle);
       y += speed * Math.sin(angle);

      
    }
    

    @Override
    public void hit() {
        active = false;
    }

    public boolean checkCollision(EnemyOni enemy) {
        if (!active) return false;
        
        Rectangle2D.Double bulletBounds = new Rectangle2D.Double(
            x - width/2, y - height/2, width, height
        );
        
        Rectangle2D.Double enemyBounds = enemy.getEntityBounds();
        if (bulletBounds.intersects(enemyBounds)) {
            hit();
            enemy.takeDamage(BULLET_DAMAGE);
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
      
            g2.setColor(Color.YELLOW);
            g2.fillOval((int)x, (int)y, (int)width, (int)height);
        
    }

   
    @Override
    public void spawn(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return (int)x;
    }
    public int getY() {
        return (int)y;
    }

    public int getPosition(){
        return (int)x;
    }
    



}
