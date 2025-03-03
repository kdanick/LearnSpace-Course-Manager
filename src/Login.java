import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    private int mouseX, mouseY; // For dragging

    public Login() {
        setTitle("Login UI");
        setSize(1500, 750);
        setUndecorated(true); // Remove title bar
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel with Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Left Panel (Logo Section)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 0, 0, 255));
        leftPanel.setPreferredSize(new Dimension(600, 750));
        leftPanel.setLayout(new GridBagLayout());

        ImageIcon logoIcon = new ImageIcon("C:/Users/adhia/OneDrive/Documents/LearnSpace/logo.png"); // Change path accordingly
        Image image = logoIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        leftPanel.add(logoLabel, new GridBagConstraints());

        // Right Panel (Login Section)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null); // Absolute positioning

        // Form Panel for Login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST; // Left align everything

        // Log In Title (Left-Aligned)
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        // Username Text Field (Left-Aligned)
        RoundedTextField usernameField = new RoundedTextField(20);
        usernameField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 2;
        formPanel.add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        // Password Text Field (Left-Aligned)
        RoundedPasswordField passwordField = new RoundedPasswordField(20, 25);
        passwordField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 4;
        formPanel.add(passwordField, gbc);

        // Login Button with Rounded Edges
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 5;
        formPanel.add(loginButton, gbc);

        // Add formPanel to rightPanel and position it
        formPanel.setBounds(150, 150, 450, 350);
        rightPanel.add(formPanel);

        // Close Button
        JLabel closeButton = new JLabel("❌");
        closeButton.setBounds(870, 10, 30, 30);
        closeButton.setForeground(Color.BLACK);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(Color.BLACK);
            }
        });
        rightPanel.add(closeButton);

        // Drag Window Feature
        rightPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        rightPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });

        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setVisible(true);

        // "Forgot Password?" Label
        JLabel forgetPasswordLabel = new JLabel("Forgot Password?");
        forgetPasswordLabel.setForeground(new Color(0, 123, 255)); // Blue color
        forgetPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        forgetPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                gbc.gridwidth = 2;
                add(titleLabel, gbc);

                // Email Label
                JLabel emailLabel = new JLabel("Enter your email:");
                emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridy = 1;
                gbc.gridwidth = 1;
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
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(submitButton, gbc);
            }
        }

        forgetPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgotPasswordDialog(Login.this).setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgetPasswordLabel.setForeground(new Color(0, 86, 179)); // Darker blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgetPasswordLabel.setForeground(new Color(0, 123, 255)); // Default color
            }
        });

// Adjust GridBagConstraints for correct placement
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Ensures it aligns properly
        gbc.anchor = GridBagConstraints.CENTER; // Center aligns it
        formPanel.add(forgetPasswordLabel, gbc);

// Add formPanel to rightPanel and position it
        formPanel.setBounds(150, 150, 450, 400); // Increase height to fit all components
        rightPanel.add(formPanel);

        setVisible(true); // Move this to the end

    }

    // Custom Rounded TextField
    class RoundedTextField extends JTextField {
        private int radius = 25;

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);  // Make sure the background is transparent
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Adjust padding
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background color
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

            // Border color and thickness
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2)); // Increase border thickness
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
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
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
        private int radius = 25;
        private Color defaultColor = new Color(0, 123, 255);
        private Color hoverColor = new Color(0, 86, 179);

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setForeground(Color.WHITE);
            setBackground(defaultColor);

            // Add hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(defaultColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
};