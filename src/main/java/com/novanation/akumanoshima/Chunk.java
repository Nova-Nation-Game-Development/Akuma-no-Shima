package com.novanation.akumanoshima;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Chunk {

    private Rectangle2D.Double chunk;
    private int xPos;                 // Starting x position of the chunk dimensions
    private int yPos;                 // Starting y position of the chunk dimensions

    private int width;                      // Width of the chunk (tiles)
    private int height;                     // Height of the chunk (tiles)
    
    public Chunk(int xPos, int yPos, int width, int height, int tileLength)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        height *= tileLength;
        width *= tileLength;
        
        chunk = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public void move(int dx)
    {
        xPos += dx;
    }

    // For testing purposes // Draw the chunk bounds
    public void showChunkBounds(Graphics2D g2) { g2.setColor(Color.RED); g2.fill(chunk); }
    public Rectangle2D.Double getChunkBounds() { return chunk; }

    @Override
    public String toString() { return "Chunk -> x Position: " + xPos / 64 + " y Position: " + yPos + " Width: " + width / 64 + " height: " + height / 64; }
}
