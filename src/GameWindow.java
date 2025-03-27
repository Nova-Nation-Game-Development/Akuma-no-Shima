package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {

    private final Container container;

    private final JPanel mainPanel;
    private final GamePanel gamePanel;

    public GameWindow()
    {
        // Setup window
        setTitle("Akuma no Shima");
        setSize(640, 360);

        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // Setup panels

        // Game Panel
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.WHITE);

        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLUE);
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        // Container
        container = getContentPane();
        container.add(mainPanel);

        setVisible(true);

        // Begin game state
        gamePanel.createGameEntities();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();
    }
}
