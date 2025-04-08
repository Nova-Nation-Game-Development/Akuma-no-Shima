package src;

import java.awt.CardLayout;
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

    private final JPanel cardPanel;
    private final CardLayout cardLayout;

    public GameWindow()
    {
        // Setup window and container
        setTitle("Akuma no Shima");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container = getContentPane();

        // Card Layout for scene switching
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panels

        // Loading Panel
        loadingPanel = new LoadingPanel(this);
        cardPanel.add(loadingPanel, "Loading");

        // Game Panel
        gamePanel = new GamePanel(this);
        gamePanel.setBackground(Color.WHITE);

        // Start loading
        loadingPanel.startLoadThread();
        loadingPanel.incrementProgress(30);

        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.add(gamePanel);
        cardPanel.add(mainPanel, "Game");

        container.add(cardPanel);
        setVisible(true);

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
                cardLayout.show(cardPanel, "Game");

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
