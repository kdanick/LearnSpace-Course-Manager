import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // Title Label
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // **TOP PANEL: Dashboard Cards**
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        cardsPanel.add(createDashboardCard("Users", "100"));
        cardsPanel.add(createDashboardCard("Courses", "50"));
        cardsPanel.add(createDashboardCard("Reports", "30"));
        cardsPanel.add(createDashboardCard("Settings", "..."));

        // **BOTTOM PANEL: Graphs and Tables**
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2, 20, 0)); // Two columns: One for Graphs, One for Tables
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Placeholder panels for Graphs & Tables
        JPanel graphPanel = new JPanel();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createTitledBorder("User Statistics"));

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Recent Activities"));

        bottomPanel.add(graphPanel);
        bottomPanel.add(tablePanel);

        // **MAIN CONTENT PANEL**
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.add(cardsPanel, BorderLayout.NORTH);
        mainContent.add(bottomPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
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
