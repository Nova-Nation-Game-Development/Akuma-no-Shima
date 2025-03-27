package src;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // Set game resolution and upscaling factor
    private final int baseWidth = 640;
    private final int baseHeight = 360;
    private final int scale = 3;

    // Parallax background variables
    private final int maxBackground = 3;
    private Image[] volcanicImages = new Image[maxBackground];
    private final BufferedImage image;

    // Thread variables
    private Thread gameThread;
    private final int FPS = 120;
    private final int frameTimePacing = 2;

    public GamePanel()
    {
        setPreferredSize(new Dimension(baseWidth * scale, baseHeight * scale));

        volcanicImages[0] = ImageManager.loadImage("../gfx/images/volcanic/lava.png");

        image = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        // Apply scaling from base resolution to window size
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);

        doubleBufferBackground();

        g2.dispose();
    }

    private void doubleBufferBackground()
    {
        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        imageContext.drawImage(volcanicImages[0], 0, 0, 640, 360, null);
        // repaint();
    }

    public void updateEntityCalculations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void createGameEntities()
    {
        repaint();
    }

    public void startGameThread()
    {
        if (gameThread == null || !gameThread.isAlive())
        {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run()
    {
        double drawInterval = 100000000/FPS;
		long lastTime = System.nanoTime();
		double timeDelta = 0;
		
		while(!Thread.currentThread().isInterrupted())
        {
			long currentTime = System.nanoTime();

			timeDelta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if(timeDelta >= frameTimePacing)
            {
                // updateEntityCalculations();
                // repaint();
				timeDelta -= frameTimePacing;
		    }
		}
    }
}
