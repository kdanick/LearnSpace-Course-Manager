import javax.swing.*;
import java.awt.*;

public class SettingsPage extends JPanel {
    public SettingsPage() {
        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("⚙️ Settings Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);
    }
}