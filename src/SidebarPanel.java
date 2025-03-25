import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final int SIDE_WIDTH = 264;
    private final int SIDE_HEIGHT = 660;

    public SidebarPanel(JPanel contentPanel, CardLayout cardLayout, String username, String userRole) {
        this.contentPanel = contentPanel;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(SIDE_WIDTH, SIDE_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        JPanel userInfo = new JPanel(new BorderLayout());
        userInfo.setBackground(Color.DARK_GRAY);
        userInfo.setPreferredSize(new Dimension(SIDE_WIDTH, 150));

        JLabel usernameLabel = new JLabel(username, SwingConstants.CENTER);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        userInfo.add(usernameLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.DARK_GRAY);
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] buttonNames;

        if(userRole.equalsIgnoreCase("Admin")){
            buttonNames = new String[] {"Dashboard", "Students", "Lecturers", "Courses", "Users", "Profile", "Logout(Not Page)"};
        } else if (userRole.equalsIgnoreCase("Lecturer")) {
            buttonNames = new String[] {"Home", "Students", "Enrollments", "Courses", "Profile", "Logout(Not Page)"};
        } else {
            buttonNames = new String[] {"LoginPage"};
        }

        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(SIDE_WIDTH, 40)); // Set fixed size to prevent stretching
            button.setPreferredSize(new Dimension(SIDE_WIDTH, 40));
            button.setBackground(new Color(60, 63, 65));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

//            Hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(80, 83, 85));
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(60, 63, 65));
                }
            });

            // Button Click Action
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, name);
                }
            });

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("Logout(Not Page)".equals(name)) {
                        showLogin();
                    } else {
                        cardLayout.show(contentPanel, name);
                    }
                }
            });

            buttonPanel.add(button);

        }

        JLabel sideFooter = new JLabel("© 2025 LearnSpace. All Rights Reserved.", SwingConstants.CENTER);
        sideFooter.setForeground(Color.WHITE);
        sideFooter.setFont(new Font("Arial", Font.BOLD, 11));
        sideFooter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(userInfo, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(sideFooter, BorderLayout.SOUTH);
    }

    private void showLogin() {
        contentPanel.removeAll();
        contentPanel.add(new LoginPage(new App()));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
