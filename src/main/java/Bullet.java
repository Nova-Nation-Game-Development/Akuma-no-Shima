

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet implements Projectile{

    private double x;
    private double y;
    private final double height = 10;
    private final double width = 10;
    private final double speed = 6;
    private double angle;

    @Override
    public void move() {
       x += speed * Math.cos(angle);
       y += speed * Math.sin(angle);
    }

    @Override
    public void hit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hit'");
    }

    @Override
    public void draw(Graphics2D g2) {
      
            g2.setColor(Color.YELLOW);
            g2.fillOval((int)x, (int)y, (int)width, (int)height);
        
    }

   
    @Override
    public void spawn(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return (int)x;
    }
    public int getY() {
        return (int)y;
    }

    public int getPosition(){
        return (int)x;
    }
    



}
