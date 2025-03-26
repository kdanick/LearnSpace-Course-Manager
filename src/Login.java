import DatabaseManager.Db_connect;
import Round.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    public Login(App app) {
        // Set up the JFrame properties
        setTitle("Login UI");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Left Panel (Logo Section)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 0, 0, 255)); // Black background
        leftPanel.setPreferredSize(new Dimension(600, 750));
        leftPanel.setLayout(new GridBagLayout());

        // Load and display logo
        ImageIcon logoIcon = new ImageIcon("resources/logo.png");
        if (logoIcon.getIconWidth() == -1) {
            System.err.println("Logo image not found.");
        } else {
            Image image = logoIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(image));
            leftPanel.add(logoLabel, new GridBagConstraints());
        }

        // Right Panel (Login Section)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        // Error message label
        JLabel EMLabel = new JLabel();
        EMLabel.setForeground(Color.RED);
        EMLabel.setHorizontalAlignment(JLabel.CENTER);
        EMLabel.setVisible(false); // Initially hidden
        mainPanel.add(EMLabel, BorderLayout.NORTH);

        // Form Panel for Login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        // GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Log In Title
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        // Username Text Field
        RoundedTextField usernameField = new RoundedTextField(20);
        usernameField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 2;
        formPanel.add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        // Password Text Field
        RoundedPasswordField passwordField = new RoundedPasswordField(20, 25);
        passwordField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 4;
        formPanel.add(passwordField, gbc);

        // Role Label
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 5;
        formPanel.add(roleLabel, gbc);

        // Role ComboBox
        String[] roles = {"Admin", "Lecturer",};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridy = 6;
        formPanel.add(roleComboBox, gbc);

        // Login Button with Authentication Logic
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.addActionListener(e -> {
            String name = usernameField.getText().trim();
            String password_hash = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            // Check if the credentials are valid
            if (!Db_connect.authenticate(name, password_hash, role)) {
                EMLabel.setText("Invalid username, password, or role.");
                EMLabel.setVisible(true); // Show error message
            } else {
                EMLabel.setText("Login Successful");
                EMLabel.setVisible(true);

                // Transition to the main application
                SwingUtilities.invokeLater(() -> {
                    app.showMainApp(role, name); // Pass role and username
                    dispose(); // Close the login frame
                });
            }
        });

        gbc.gridy = 7;
        formPanel.add(loginButton, gbc);

        // "Forgot Password?" Label
        JLabel fPassLabel = new JLabel("Forgot Password?");
        fPassLabel.setForeground(new Color(0, 123, 255)); // Blue color
        fPassLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fPassLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new ForgotPasswordDialog(Login.this).setVisible(true); // Open dialog
            }

            public void mouseEntered(MouseEvent e) {
                fPassLabel.setForeground(new Color(0, 86, 179)); // Darker blue on hover
            }

            public void mouseExited(MouseEvent e) {
                fPassLabel.setForeground(new Color(0, 123, 255)); // Reset color
            }
        });

        // Add forgot password label to formPanel
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center align
        formPanel.add(fPassLabel, gbc);

        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        rightPanel.add(formPanel); // Add formPanel to rightPanel

        setVisible(true); // Call this only once
    }

    // Dialog for Forgot Password
    class ForgotPasswordDialog extends JDialog {
        public ForgotPasswordDialog(JFrame parent) {
            super(parent, "Reset Password", true);
            setSize(400, 250);
            setLocationRelativeTo(parent);
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Title Label
            JLabel titleLabel = new JLabel("Forgot Password?");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2; // Span two columns
            add(titleLabel, gbc);

            // Email Label
            JLabel emailLabel = new JLabel("Enter your email:");
            emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridy = 1;
            gbc.gridwidth = 1; // Reset to one column
            add(emailLabel, gbc);

            // Email Text Field
            JTextField emailField = new JTextField(20);
            gbc.gridy = 2;
            add(emailField, gbc);

            // Submit Button
            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> {
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your email.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Password reset link sent to " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the dialog
                }
            });

            gbc.gridy = 3;
            gbc.gridwidth = 2; // Span two columns
            gbc.anchor = GridBagConstraints.CENTER; // Center align
            add(submitButton, gbc);
        }
    }
}