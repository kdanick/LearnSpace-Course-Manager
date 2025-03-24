import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    public HomePage() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("🏠 Home Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);
    }
}
