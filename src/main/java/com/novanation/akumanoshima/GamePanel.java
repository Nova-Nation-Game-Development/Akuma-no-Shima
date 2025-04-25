package com.novanation.akumanoshima;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class GamePanel extends Scene {

    private final GameWindow window;

    private final int finalLevel = 10;
    private int currentLevel = 1;
    private boolean isEndless = false;

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
    private CameraControls camera;
    private AssualtWeapon ar;

    private Collection<Tile> tiles;
    private Collection<Tile> tileDepths;

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

        if (tiles != null)
            for (Tile tile : tiles)
                tile.draw(imageContext);

        if (tileDepths != null)
            for (Tile tile : tileDepths)
                tile.draw(imageContext);
        
        if(ar != null) 
            ar.drawBullets(imageContext);

        if (playerEntity != null)
        {
            playerEntity.draw(imageContext);
            
            if (playerEntity.getHealth() != null)
                playerEntity.getHealth().draw(imageContext);

            if (playerEntity.getCurrentChunk() != null)
                playerEntity.getCurrentChunk().showChunkBounds(imageContext);
        }
        
        EnemyManager.draw(imageContext);

        imageContext.setColor(Color.WHITE);  // cross hair
        
        if(playerInput != null) {
            int x = InputHandler.getMouseX();
            int y = InputHandler.getMouseY();
            int size = 20; // Size of crosshair
            
            imageContext.setColor(Color.WHITE);
            // Draw horizontal line
            imageContext.drawLine(x - size/2, y, x + size/2, y);
            // Draw vertical line
            imageContext.drawLine(x, y - size/2, x, y + size/2);
            
            // Optional: Add a small dot in the center
            imageContext.fillOval(x - 2, y - 2, 4, 4);
        }

        imageContext.dispose();
    }

    public void updateEntityCalculations()
    {
        // Constantly apply gravity to the player
        Physics.applyGravity(playerEntity, playerEntity.getX(), playerEntity.getY());

        // This will keep track of the world and player and update their locations accordingly
        camera.update();
        if(ar != null) {
            ar.updateShooting();
            ar.updateReloading();
        }
    }

    public GameWindow getGameWindow() { return window; }

    public void createGameEntities()
    {
        backgroundManager = new BackgroundManager(this, window);
        Physics.setPanel(this);

        int playerHeight = (window.getHeight() / 164) * 40;
        int playerWidth = (playerHeight / 2);

        WorldType world = WorldGeneration.getRandomWorld();

        if (currentLevel == finalLevel && !isEndless)
            WorldGeneration.generateLevel(this, WorldType.END);
        else
            WorldGeneration.generateLevel(this, world);

        tiles = WorldGeneration.getAllTiles();
        tileDepths = WorldGeneration.getAllDepthTiles();

        // Fix spawn height
        playerEntity = new Player(this, 30, (getHeight() - playerHeight) - (int) (WorldGeneration.getTileLength() * 1.5) - 30, playerWidth, playerHeight);
        playerInput = new InputHandler(playerEntity);
        camera = new CameraControls(playerEntity, playerInput, backgroundManager);
        ar = new AssualtWeapon(playerEntity);
        ar.setInputHandler(playerInput);
        playerInput.setWeapon(ar);        

        addKeyListener(playerInput);

        //Mouse setup
       
        addMouseListener(playerInput);
        addMouseMotionListener(playerInput);

        currentLevel++; // Create entities will only be called at the start of a new level
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

            // Update the game accordingly to match the FTP of 60FPS
			if(timeDelta >= frameTimePacing)
            {
                updateEntityCalculations();
                repaint();
				timeDelta -= frameTimePacing;
		    }
		}
    }
}
