package src;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // private final int scale = 3;
    private final GameWindow window;

    // Parallax background variables
    private final BufferedImage image;
    private BackgroundManager backgroundManager;

    // Thread variables
    private Thread gameThread;
    private final int FPS = 120;
    private final double frameTimePacing = 8.33f; // milliseconds

    // Entity Variables
    private Player playerEntity;
    private InputHandler playerInput;

    private double timer = 0; // for phys calculations

    public GamePanel(GameWindow window)
    {
        this.window = window;
        setPreferredSize(new Dimension(this.window.getWidth(), this.window.getHeight()));

        image = new BufferedImage(this.window.getWidth(), this.window.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        doubleBufferBackground();

        g.dispose();
    }

    private void doubleBufferBackground()
    {
        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        if (backgroundManager != null)
            backgroundManager.draw(imageContext);

        if (playerEntity != null)
            playerEntity.draw(imageContext);

        imageContext.dispose();
    }

    public void updateEntityCalculations()
    {
        playerEntity.setHeight((window.getHeight() / 164) * 40);
        playerEntity.setWidth(playerEntity.getHeight() / 2);
        playerEntity.update();
    }

    public void createGameEntities()
    {
        backgroundManager = new BackgroundManager(this, window);

        int playerHeight = (window.getHeight() / 164) * 20;
        int playerWidth = (playerHeight / 3);

        playerEntity = new Player(this, 10, window.getHeight() - 264, playerWidth, playerHeight);
        playerInput = new InputHandler(playerEntity, backgroundManager);

        addKeyListener(playerInput);

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

            // Update the game according to match the FTP of 60FPS
			if(timeDelta >= frameTimePacing)
            {
                updateEntityCalculations();
                repaint();
				timeDelta -= frameTimePacing;
		    }
            timer++;
		}
    }

    public double getTimer(){
        return timer;
    }
}
