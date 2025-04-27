package com.novanation.akumanoshima;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class EnemyProjectile implements Projectile {

    private double x, y;
    private double t = 0; // Time parameter for bezier curve (0 to 1)
    private double speed = 0.001; // Speed of projectile movement
    private int size = 30; // Size of projectile
    private boolean active = true;
    private static final int FIREBALL_DAMAGE = 10; // Damage dealt by the fireball

    private double directionX;
    private double directionY;
    private double targetX, targetY;
   

    // Bezier curve points
    private double startX, startY;
    private double controlX, controlY;
    private double endX, endY;

    @Override
    public void move() {
        if (t <= 1.0) {
            Point2D.Double pos = Physics.calculateBezierPoint(
                startX, startY,
                controlX, controlY,
                endX, endY,
                t
            );
            x = pos.x;
            y = pos.y;
            t += speed;
        } else {
            active = false;
        }
    }

    @Override
    public void hit() {
        active = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;
        
        g2.setColor(new Color(255, 50, 0));
        g2.fillOval((int)x - size/2, (int)y - size/2, size, size);
        
        // Draw glow effect
        g2.setColor(new Color(255, 100, 0, 128));
        g2.fillOval((int)x - size/2 - 5, (int)y - size/2 - 5, size + 10, size + 10);
    }

    @Override
    public void spawn(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.t = 0;
        this.active = true;
    }

    public void setTargetPoints(double startX, double startY, double controlX, double controlY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.controlX = controlX;
        this.controlY = controlY;
        this.endX = endX;
        this.endY = endY;
    }

    public boolean isActive() {
        return active;
    }

    public boolean checkCollision(Player player) {
        if (!active) return false;
        
        Rectangle2D.Double projectileBounds = new Rectangle2D.Double(
            x - size/2, y - size/2, size, size
        );
        
        Rectangle2D.Double playerBounds = player.getEntityBounds();
        if (projectileBounds.intersects(playerBounds)) {
            hit();
            player.getHealth().dealDamage(FIREBALL_DAMAGE, EntityType.PLAYER, player);
            System.out.println("Player hit by fireball! Dealing " + FIREBALL_DAMAGE + " damage");
            return true;
        }
        return false;
    }

}
