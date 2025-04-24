package com.novanation.akumanoshima;


public class CameraControls {
    
    private final Player player;
    private final InputHandler playerInput;
    private final BackgroundManager backgroundManager;

    // Speeds for camera manipulation
    private final int WORLD_SPEED = 2;                      // This determines the speed of the world
    private final int PLAYER_SPEED = 2;                     // This determines the speed of the player

    // Max size of the world = 3840px
    private final int LEFT_THRESHOLD = 600;                 // Defines the area in which the player must enter before the world moves
    private final int RIGHT_THRESHOLD = 3000;               // Defines the area in which the player must leave before the world stops moving
    // private final float THRESHOLD_SCALE = 10f;              // How much the player speed is reduced when entering the threshold
    
    // Location
    private double xPos;

    public CameraControls(Player player, InputHandler playerInput, BackgroundManager backgroundManager)
    {
        this.player = player;
        this.playerInput = playerInput;
        this.backgroundManager = backgroundManager;

        xPos = player.getX() + 30;
    }

    public void update()
    {
        if (player == null || playerInput == null) return;

        // TODO: Temporary
        if (playerInput.damage != 0)
        {
            player.getHealth().dealDamage(playerInput.damage);
            playerInput.damage = 0;
        }
        if (playerInput.health != 0)
        {
            player.getHealth().addHealth(playerInput.health);
            playerInput.health = 0;
        }
            
        if (playerInput.isMoving())
        {
            int newWorldSpeed;
            int newPlayerSpeed;
            int bgDirection;

            if (playerInput.getDirection() == -1)
            {
                newPlayerSpeed = -PLAYER_SPEED;
                newWorldSpeed = WORLD_SPEED;
                bgDirection = -1;

                if (player.getX() - (player.getWidth() / 2) - newPlayerSpeed > 0)
                    moveWorld(newPlayerSpeed, newWorldSpeed, bgDirection);
            }
            else if (playerInput.getDirection() == 1)
            {
                newPlayerSpeed = PLAYER_SPEED;
                newWorldSpeed = -WORLD_SPEED;
                bgDirection = 1;

                if (player.getX() + newPlayerSpeed + (player.getWidth() / 2) + newPlayerSpeed < player.getPanelDimensions().getWidth() - player.getWidth() / 2)
                    moveWorld(newPlayerSpeed, newWorldSpeed, bgDirection);
            }

            // May be best to leave the world height as is (For now)
            if (playerInput.canJump() && !player.isJumping())
                player.jump();
        }

        if (!player.canJump())
            playerInput.stopJump();
    }

    private void updatePlayer(int newPlayerSpeed)
    {
        xPos += (newPlayerSpeed);
        player.setWorldPos((int) xPos);
    }

    public void moveWorld(int newPlayerSpeed, int newWorldSpeed, int bgDirection)
    {
        // Increase player speed while jumping
        // if (player.isJumping())
        //     newPlayerSpeed *= 1.5;

        // Handle collisions
        // if (!player.isColliding(newPlayerSpeed))
        //     updatePlayer(newPlayerSpeed);
        // else
        // {
        //     if (player.getCollisionDirection() == 0 && newPlayerSpeed < 0)
        //         updatePlayer(newPlayerSpeed);

        //     if (player.getCollisionDirection() == 1 && newPlayerSpeed > 0)
        //         updatePlayer(newPlayerSpeed);
        // }
        
        // For testing purposes only
        updatePlayer(newPlayerSpeed);

        if ((int) xPos < LEFT_THRESHOLD || (int) xPos > RIGHT_THRESHOLD)
            if (!player.isColliding(newPlayerSpeed))
                player.move(newPlayerSpeed);
    
        if ((int) xPos > LEFT_THRESHOLD && (int) xPos < RIGHT_THRESHOLD)
        {
            if (!player.isColliding(newPlayerSpeed))
            {
                backgroundManager.move(bgDirection);
                WorldGeneration.move(newWorldSpeed);
            }
        }
    }
}
