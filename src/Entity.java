package src;

import java.awt.Graphics2D;

public interface Entity {
    
    public void move(int direction);
    public void jump();
    public void draw(Graphics2D g2);
    public void performAction();
}
