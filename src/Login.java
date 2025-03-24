import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    private int mouseX, mouseY; // For dragging

    public Login() {
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
        ImageIcon logoIcon = new ImageIcon("resources/logo.png"); // Change path accordingly
        Image image = logoIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        leftPanel.add(logoLabel, new GridBagConstraints());

        // Right Panel (Login Section)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better alignment

        // Form Panel for Login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        // GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST; // Left align components

        // Log In Title
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        formPanel.add(titleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to one column
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

// Login Button with Authentication Logic
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(Login.this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean isAuthenticated = Database.authenticate(username, password);
                if (isAuthenticated) {
                    JOptionPane.showMessageDialog(Login.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // TODO: Open a new dashboard window
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridy = 5;
        formPanel.add(loginButton, gbc);


        // Add components to right panel
        gbc.gridy = 1; // Reset y position for formPanel
        rightPanel.add(formPanel, gbc);

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
        gbc.gridy = 6; // Positioning for forgot password label
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center align
        formPanel.add(fPassLabel, gbc);

        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setVisible(true); // Call this only once
    }

    // Custom Rounded TextField
    class RoundedTextField extends JTextField {
        private int radius = 25; // Corner radius for rounding

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);  // Make background transparent
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Adjust padding
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background color
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

            // Border color and thickness
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2)); // Border thickness
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom Rounded PasswordField
    class RoundedPasswordField extends JPasswordField {
        private int radius;

        public RoundedPasswordField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false); // Make background transparent
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom Rounded Button with Hover Effect
    class RoundedButton extends JButton {
        private int radius = 25; // Corner radius for rounding
        private Color defaultColor = new Color(0, 123, 255); // Default button color
        private Color hoverColor = new Color(0, 86, 179); // Color on hover

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false); // Remove focus painting
            setContentAreaFilled(false); // Make transparent
            setOpaque(false); // Make background transparent
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Adjust padding
            setForeground(Color.WHITE); // Text color
            setBackground(defaultColor); // Set default background color

            // Add hover effect
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor); // Change background on hover
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(defaultColor); // Reset background color
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); // Rounded background

            g2.setColor(Color.WHITE); // Text color
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3); // Centered text

            g2.dispose();
        }
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

    public static void main(String[] args) {
        new Login(); // Launch the login UI
    }
}