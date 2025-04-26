package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;

public class PlayerAnimation {

    private Animation walkLeft;
    private Animation walkRight;

    private Animation currentAnimation;

    private int x;
    private int y;
    private int width = 64;
    private int height = 64;

    private boolean movingLeft = false;
    private boolean movingRight = false;

    Player player;

    public PlayerAnimation(Player player) {
        this.player = player;
        loadAnimations();
        currentAnimation = walkRight; // Default facing right

        start();
   }
    

    private void loadAnimations() {

        GamePanel panel = player.getPanel();
        // Load walking left frames
        Image left1 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerLeft1.png");
        Image left2 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerLeft2.png");
        Image left3 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerLeft3.png");

        walkLeft = new Animation(panel);
        walkLeft.addFrame(left1, 150);
        walkLeft.addFrame(left2, 150);
        walkLeft.addFrame(left3, 150);

        // Load walking right frames
        Image right1 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerRight1.png");
        Image right2 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerRight2.png");
        Image right3 = ImageManager.loadImage("/gfx/animations/PlayerWalking/playerRight3.png");

        walkRight = new Animation(panel);
        walkRight.addFrame(right1, 150);
        walkRight.addFrame(right2, 150);
        walkRight.addFrame(right3, 150);

        System.out.println("Loading animations...");
        System.out.println("Left1 image loaded: " + (left1 != null));
    }

    public void start() {
        x = 100;
        y = 300;
        walkLeft.start();
        walkRight.start();
    }

    public void update() {
        if (movingLeft) {
            currentAnimation = walkLeft;
            currentAnimation.update();
        } else if (movingRight) {
            currentAnimation = walkRight;
            currentAnimation.update();
        }

        //currentAnimation.update();
    }

    

    public void draw(Graphics2D g2) {
        if (currentAnimation != null && currentAnimation.getImage() != null) {
            currentAnimation.setPosition(x, y);
            g2.drawImage(currentAnimation.getImage(), x, y, width, height, null);
        }
    }

    public void moveLeft(boolean moving) {
        movingLeft = moving;
    }

    public void moveRight(boolean moving) {
        movingRight = moving;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

