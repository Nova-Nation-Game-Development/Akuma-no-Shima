package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class EnemyHellhound implements Entity {

    private final Health health;

    // Location Variables
    private int xPos;
    private int yPos;

    // Design Variables
    private final int width;
    private final int height;

    private final Image hellhoundImage;

    public EnemyHellhound(int width, int height, int xPos, int yPos)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;

        health = new Health();
        // TODO: Get Hellhound Image
        hellhoundImage = ImageManager.loadImage("/gfx/characters/char_maou.png");
    }

    @Override
    public void draw(Graphics2D g2) { g2.drawImage(hellhoundImage, xPos, yPos, width, height, null); }
    
    @Override
    public void move(int direction) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'move'"); }

    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }

    @Override
    public void performAction() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'performAction'"); }

    @Override
    public Rectangle2D.Double getEntityBounds() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getEntityBounds'"); }

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
