import javax.swing.*;
import java.awt.*;
import analytics.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // **Title Label**

        // **TOP PANEL: Dashboard Cards**
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        cardsPanel.add(createDashboardCard("Courses", "100"));
        cardsPanel.add(createDashboardCard("Students", "50"));
        cardsPanel.add(createDashboardCard("Enrollments", "30"));
        cardsPanel.add(createDashboardCard("Instructors", "50"));

        // BOTTOM PANEL: Analytics + Table
        JPanel analyticsPanel = new JPanel(new GridBagLayout());
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        analyticsPanel.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Analytics Row: Bar Chart & Pie Chart**
        JPanel chartRow = new JPanel(new GridLayout(1, 2, 20, 10));
        chartRow.setBackground(Color.LIGHT_GRAY);
        chartRow.setBorder(BorderFactory.createTitledBorder("Analytics Overview"));
        chartRow.setPreferredSize(new Dimension(800, 300));

        chartRow.add(new StudentBarChart());
        chartRow.add(new CoursePieChart());

        gbc.gridy = 0;
        analyticsPanel.add(chartRow, gbc);

        // **Table Row: Average Grade per Course**
        gbc.gridy = 1;
        analyticsPanel.add(new CourseGradeTable(), gbc);

        // **Main Content Panel (Scrollable)**
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(cardsPanel);
        mainContent.add(analyticsPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    // **DASHBOARD CARDS**
    private JPanel createDashboardCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
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
