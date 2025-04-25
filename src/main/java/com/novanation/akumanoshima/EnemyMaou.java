package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class EnemyMaou implements Entity {

    public int yPos;
    public int xPos;
    public int height;

    private final Health health;

    // Design variables
    private final Image maouImage;

    public EnemyMaou()
    {
        health = new Health();
        maouImage = ImageManager.loadImage("/gfx/characters/char_maou.png");
    }

    @Override
    public int getHeight() { return height; }
    @Override
    public double getX() { return xPos; }
    @Override
    public double getY() { return yPos; }
    @Override
    public Health getHealth() { return health; }

    @Override
    public void move(int direction) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'move'"); }

    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }

    @Override
    public void draw(Graphics2D g2) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'draw'"); }

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
    public void moveY(double dx) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'moveY'"); }

    @Override
    public double getDY() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDY'"); }

    @Override
    public void setY(double newY) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setY'"); }

    @Override
    public double getVelocityY() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getVelocityY'"); }

    @Override
    public void setVelocityY(double vy) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setVelocityY'"); }

    @Override
    public boolean isGrounded() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isGrounded'"); }

    @Override
    public void setGrounded(boolean grounded) { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setGrounded'"); }
    
}
