package com.novanation.akumanoshima;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class GameWindow extends JFrame {

    private final Container container;

    private final JPanel mainPanel;
    private final GamePanel gamePanel;

    private final LoadingPanel loadingPanel;
    private final Menu menuPanel;

    private final SoundManager soundManager;
    private final Config config;

    public GameWindow()
    {
        // Load Game Files
        config = ConfigManager.loadConfig();

        // Setup window and container
        setTitle("Akuma no Shima");
        setSize(config.getResolutionWidth(), config.getResolutionHeight());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container = getContentPane();

        // Sound Manager
        soundManager = SoundManager.getInstance();

        // Panels

        gamePanel = new GamePanel(this);
        loadingPanel = new LoadingPanel(this);
        menuPanel = new Menu(this);

        mainPanel = new JPanel();
        mainPanel.setName("Game");

        SceneLoader.addScene(mainPanel);
        SceneLoader.addScene(loadingPanel);
        SceneLoader.addScene(menuPanel);

        // Load Menu Scene
        SceneLoader.switchScene("Menu");

        // Main Panel

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.add(gamePanel);

        container.add(SceneLoader.getCardPanel());
        setVisible(true);
    }

    public Config getConfig() { return config; } 

    public void playAudioClip(String title, ClipType clipType, boolean loopable) { soundManager.playClip(title, clipType, loopable); }
    public void stopAudioClip(String title, ClipType clipType) { soundManager.stopClip(title, clipType); }

    public void loadGame()
    {
        SceneLoader.switchScene("LoadingPanel");

        loadingPanel.startLoadThread();
        loadingPanel.incrementProgress(30);

        initializeGameLoadingThread();
    }
    
    private void initializeGameLoadingThread()
    {
        new Thread(() -> {
            
            loadingPanel.incrementProgress(30);

            // Load game entities
            gamePanel.createGameEntities();

            // Stop loading and switch to game
            loadingPanel.incrementProgress(40);
            // Force a slight delay to ensure that the final progress is visible
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) { }
            
            // Begin game setup on Swing thread
            javax.swing.SwingUtilities.invokeLater(() -> {
                loadingPanel.stopThread();

                SceneLoader.switchScene("Game");

                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
                startGameThread();
            });
        }).start();
    }

    synchronized void startGameThread()
    {
        try {
            wait(50);
            gamePanel.startGameThread();
        } catch (InterruptedException e) { }
    }
}
