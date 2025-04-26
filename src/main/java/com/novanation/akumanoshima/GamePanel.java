package com.novanation.akumanoshima;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        
        if(ar != null) {
            ar.render(imageContext);
            ar.drawBullets(imageContext);
        }
        if (playerEntity != null)
        {
            playerEntity.draw(imageContext);
            
            if (playerEntity.getHealth() != null)
                playerEntity.getHealth().draw(imageContext);

            // Draw the player's current chunk
            // if (playerEntity.getCurrentChunk() != null)
            //     playerEntity.getCurrentChunk().showChunkBounds(imageContext);

            // Draw the player collider
            // imageContext.setColor(new Color(255, 255, 255, 128));
            // if (playerEntity.getEntityBounds() != null)
            //     imageContext.fill(playerEntity.getEntityBounds());
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

        if (LevelManager.showLevelClear() && LevelManager.isSetUp())
        {
            long elapsed = System.currentTimeMillis() - LevelManager.levelClearStartTime();
            if (elapsed <= 3000)
            {
                int x = LevelManager.getImgClearX();
                int y = LevelManager.getImgClearY();
        
                imageContext.drawImage(LevelManager.getClearImage(), x, y, this);
            }
            else
            {
                System.out.println("I Changed nothing");

                LevelManager.setSetup(false);
                LevelManager.setLevelClear(false);
                startNewLevel();
            }
        }

        if (LevelManager.isFinalLevel() && LevelManager.isSetUp())
        {
            long elapsed = System.currentTimeMillis() - LevelManager.levelClearStartTime();
            if (elapsed <= 5000)
            {
                int x = LevelManager.getImgWinX();
                int y = LevelManager.getImgWinY();
        
                imageContext.drawImage(LevelManager.getWinImage(), x, y, this);
            }
            else
            {
                LevelManager.setFinal(false);
                // Return the player to the main menu
                SceneLoader.switchScene("Menu");
                window.playAudioClip("Menu", ClipType.MENU, true);
                stopGameThread();
            }
        }

        imageContext.dispose();
    }

    public void startNewLevel()
    {
        SceneLoader.switchScene("LoadingPanel");
        stopGameThread();
        window.loadGame();
    }

    public void updateEntityCalculations()
    {
        // Constantly apply gravity to the player
        Physics.applyGravity(playerEntity, playerEntity.getX(), playerEntity.getY());

        // Add entities to a separate list to prevent modification during iteration
        ArrayList<Entity> entityList = new ArrayList<>();
        for (Entity entity : EnemyManager.getEnemies().values())
            entityList.add(entity);

        for (Entity entity : entityList)
        {
            Physics.applyGravity(entity, entity.getX(), entity.getY());
            entity.update();
        }
            
        playerEntity.update();
        

         for (Entity enemy : EnemyManager.getAllEnemies()) {
            if (enemy instanceof EnemyOni oni) {
                oni.performAction();
            }
    }
        // This will keep track of the world and player and update their locations accordingly
        camera.update();
        if(ar != null) {
            ar.updateShooting();
            ar.updateReloading();
        }
    }

    public GameWindow getGameWindow() { return window; }

    public Player getPlayerEntity() { return playerEntity; }

    public void createGameEntities()
    {
        Physics.setPanel(this);
      
        LevelManager.setupToast(this);
        LevelManager.setLevelClear(false);
        LevelManager.setFinal(false);

        int playerHeight = (window.getHeight() / 164) * 40;
        int playerWidth = (playerHeight / 2);

        WorldType world = WorldGeneration.getRandomWorld();
        backgroundManager = new BackgroundManager(this, window, world);

        if (currentLevel >= finalLevel && !isEndless)
            WorldGeneration.generateLevel(this, WorldType.END);
        else
            WorldGeneration.generateLevel(this, world);

        tiles = WorldGeneration.getAllTiles();
        tileDepths = WorldGeneration.getAllDepthTiles();

        playerEntity = new Player(this, 30, (getHeight() - playerHeight) - (int) (WorldGeneration.getTileLength() * 1.5) - 30, playerWidth, playerHeight);
        playerEntity.setWorldPos((int) playerEntity.getX());

        playerInput = new InputHandler(playerEntity);
        EnemyManager.setPlayer(playerEntity); // Set player instance before generating enemies
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

    public void stopGameThread() { gameThread.interrupt(); gameThread = null; }

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
