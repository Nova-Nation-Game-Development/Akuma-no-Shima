package src;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Entity {
    
    public void move(int direction);
    public void jump();
    public void draw(Graphics2D g2);
    public void performAction();

    public Rectangle2D.Double getEntityBounds();
    public Chunk getCurrentChunk();
    public void onGround(boolean onGround);
    public void moveY(double dx);
}
