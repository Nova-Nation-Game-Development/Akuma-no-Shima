

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class GameWindow extends JFrame {

    private final Container container;

    private final JPanel mainPanel;
    private final GamePanel gamePanel;

    private final LoadingPanel loadingPanel;
    private final Menu menuPanel;

    private final SoundManager soundManager;

    public GameWindow()
    {
        // Setup window and container
        setTitle("Akuma no Shima");
        setSize(1366, 768);
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

        // Load Game Files
        loadGameConfig();

        // Main Panel
        
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.add(gamePanel);

        container.add(SceneLoader.getCardPanel());
        setVisible(true);
    }

    private void loadGameConfig()
    {
        File config = new File("/user-config/game_save.config");
        if (config.exists())
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(config)))
            {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) { }
        }
        else
        {
            try {
                    Files.write(Paths.get(config.getPath()),
                                Arrays.asList("Line 1", "Line 2"),
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) { }
        }
    }

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
