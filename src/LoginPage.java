import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {
    public LoginPage(App app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("🔑 Login");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            // Simulate login
            System.out.println("User logged in...");
            app.showMainApp(); // Switch to main app
        });

        gbc.gridx = 0; gbc.gridy = 0; add(title, gbc);
        gbc.gridy = 1; add(new JLabel("Username:"), gbc);
        gbc.gridy = 2; add(usernameField, gbc);
        gbc.gridy = 3; add(new JLabel("Password:"), gbc);
        gbc.gridy = 4; add(passwordField, gbc);
        gbc.gridy = 5; add(loginButton, gbc);
    }
}
