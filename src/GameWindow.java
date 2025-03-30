package src;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
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
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setup panels

        // Game Panel
        gamePanel = new GamePanel(this);
        gamePanel.setBackground(Color.WHITE);

        // Main Panel
        mainPanel = new JPanel();

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		mainPanel.setLayout(flowLayout);
        mainPanel.setBackground(Color.BLUE);
        mainPanel.add(gamePanel);

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
