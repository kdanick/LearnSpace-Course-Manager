import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JPanel {
    public ProfilePage() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("👤 Profile Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);
    }
}
