package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;

public class EnemyMaouAnimation {

    // Animations
    private final Animation walkAnim;
    private final Animation attackAnim;
    private final Animation dustAnim;

    private final EnemyMaou maou;

    private final int WALK_LENGTH = 6;
    private final int ATTACK_LENGTH = 6;
    private final int DUST_LENGTH = 25;

    private final double SPEED_SCALE = 3.5;

    public EnemyMaouAnimation(EnemyMaou maou)
    {
        this.maou = maou;
        walkAnim = new Animation(true);
        attackAnim = new Animation(false);
        dustAnim = new Animation(false);

        // Dynamically load the animation frames
        for (int i = 1; i <= WALK_LENGTH; i++)
        {
            Image frame = ImageManager.loadImage("/gfx/animations/maou/walk/maou_" + i + ".png");
            walkAnim.addFrame(frame, (int) (1000 / (WALK_LENGTH * SPEED_SCALE)) * 5); // Average is about 2 steps a second
        }

        for (int i = 1; i <= ATTACK_LENGTH; i++)
        {
            Image frame = ImageManager.loadImage("/gfx/animations/maou/attack/maou_" + i + ".png");
            attackAnim.addFrame(frame, (int) (1000 / (i * 200)) * 60);
        }

        for (int i = 1; i <= DUST_LENGTH; i++)
        {
            Image frame = ImageManager.loadImage("/gfx/animations/maou/dust/frame_" + i + ".png");
            dustAnim.addFrame(frame, 40);
        }
    }

    public void startWalk() { walkAnim.start(); }
    public void stopWalk() { walkAnim.stop(); }

    public void startAttack() { attackAnim.start(); }
    public void stopAttack() { attackAnim.stop(); }

    public void startDust() { dustAnim.start(); }
    public void stopDust() { dustAnim.stop(); }

    public boolean isWalkStillActive() { return walkAnim.isStillActive(); }
    public boolean isAttackStillActive() { return attackAnim.isStillActive(); }
    public boolean isDustStillActive() { return dustAnim.isStillActive(); }
    
    public void draw(Graphics2D g2)
    {
        if (walkAnim.isStillActive())
        {
            walkAnim.update();
            g2.drawImage(walkAnim.getImage(), (int) maou.getX(), (int) maou.getY(), (int) maou.getWidth(), maou.getHeight(), null);
        }

        if (attackAnim.isStillActive())
        {
            attackAnim.update();
            g2.drawImage(attackAnim.getImage(), (int) maou.getX(), (int) maou.getY(), (int) maou.getWidth(), maou.getHeight(), null);
        }

        if (dustAnim.isStillActive())
        {
            dustAnim.update();
            g2.drawImage(dustAnim.getImage(), (int) maou.getX() + (Math.abs(maou.getWidth()) / 2) - 300,
                (int) maou.getY() + 130, (int) maou.getWidth() + 300, maou.getHeight(), null);
        }
    }
}

