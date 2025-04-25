package com.novanation.akumanoshima;

import java.awt.geom.Rectangle2D;


public class Physics {
    
    private static final double GRAVITY = 1;
    private static final double INITIAL_VERTICAL_VELOCITY = -12.0;
    private static final double INITIAL_HORIZONTAL_VELOCITY = 5.0;

    private static final int JUMP_INTERVAL = 7; // In milliseconds (ms) // Influences jump smoothness
    private static final int MAX_STEP_COUNT = 230; // Maximum steps for calculating the jump // Influences jump height
    private static final double COUNT_SCALE = 0.3f;
    private static final double SPEED_SCALE = 0.2f;

    public static GamePanel panel;

    private static double gravity = 0.5f; // gravity = 9.8 m/s
    private static double terminalVelocity = 10;

    public boolean isJumping = false;
    private double currTime = 0;
    private double initY = 0;
    private double initX = 0;

    // Accessors

    public static int getJumpInterval() { return JUMP_INTERVAL; }
    public static int getMaxStep() { return MAX_STEP_COUNT; }
    public static double getCountScale() { return COUNT_SCALE; }
    public static double getSpeedScale() { return SPEED_SCALE; }
    public static double getGravity() { return gravity; }

    // Functions
    
    private static int count;

    public static void setPanel(GamePanel gamePanel) { panel = gamePanel; }

    public static void applyGravity(Entity entity, double x, double y)
    {
        if (entity == null) return;

        // Apply gravity
        if (!entity.isGrounded())
        {
            double newVelocityY = Math.min(entity.getVelocityY() + gravity, terminalVelocity);
            entity.setVelocityY(newVelocityY);
        }

        entity.moveY(entity.getVelocityY());

        // Check if player is landing
        Rectangle2D.Double chunkBounds = entity.getCurrentChunk() != null
            ? entity.getCurrentChunk().getChunkBounds()
            : null;

        if (chunkBounds != null)
        {
            double entityBottom = entity.getY() + entity.getHeight();
            double groundY = chunkBounds.getY();

            // Only snap the entity to the ground if they are falling on the ground (not air gap)
            // Fix
            if (entityBottom >= groundY && entity.getVelocityY() >= 0)
            {
                entity.setY(groundY - entity.getHeight());
                entity.setGrounded(true);
                entity.setVelocityY(0);
            }
            else
                entity.setGrounded(false);
        }
        else
            entity.setGrounded(false);
    }

    // kinematics 
    // s = ut + 1/2at^2
    public static double calculateVertComponent(double initialVelocity, double timeElapsed) 
    {
        return(initialVelocity * timeElapsed - 0.5 * GRAVITY * timeElapsed * timeElapsed);
    }

    public static double calculateHorizComponent(double direction, double timeElapsed)
    {
        return INITIAL_HORIZONTAL_VELOCITY * direction + timeElapsed;
    }

    public void startJump(double startY, double startX)
    {
        isJumping = true;
        currTime = 0;
        initY = startY;
        initX = startX;
    }

    public void resetJumpPos()
    {
        isJumping = false;
        currTime = 0;
    }
}
