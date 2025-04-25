package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class EnemyOni implements Entity {

    private final Health health;

    // Location Variables
    private int xPos;
    private int yPos;

    // Design Variables
    private final int width;
    private final int height;

    private final String enemyID;

    private final Image oniImage;

    public EnemyOni(int width, int height, int xPos, int yPos, String enemyID)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.enemyID = enemyID;

        health = new Health();
        oniImage = ImageManager.loadImage("/gfx/characters/char_oni.png");
    }
    
    public String getEnemyID() { return enemyID; }

    @Override
    public int getHeight() { return height; }
    @Override
    public double getX() { return xPos; }
    @Override
    public double getY() { return yPos; }
    @Override
    public Health getHealth() { return health; }

    @Override
    public void draw(Graphics2D g2) { g2.drawImage(oniImage, xPos, yPos, width, height, null); }

    @Override
    public void move(int direction) { xPos += direction; }

    @Override
    public void jump() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'jump'"); }

    @Override
    public void performAction() { // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'performAction'"); }

    @Override
    public Rectangle2D.Double getEntityBounds() { return new Rectangle2D.Double(xPos, yPos, width, height); }

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
