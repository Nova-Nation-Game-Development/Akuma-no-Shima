
import javax.swing.JFrame;

public class GameWindow extends JFrame {
    
    public GameWindow()
    {
        setTitle("Akuma no Shima");
        setSize(640, 360);

        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
