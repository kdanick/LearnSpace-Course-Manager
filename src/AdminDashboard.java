import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Placeholder panel for dashboard widgets
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 4, 20, 0)); // 2x2 grid layout
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add sample dashboard cards
        contentPanel.add(createDashboardCard("Users", "100"));
        contentPanel.add(createDashboardCard("Courses", "50"));
        contentPanel.add(createDashboardCard("Reports", "30"));
        contentPanel.add(createDashboardCard("Settings", "..."));

        add(contentPanel, BorderLayout.NORTH);
    }

    private JPanel createDashboardCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setPreferredSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}
