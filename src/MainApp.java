import javax.swing.*;
import java.awt.*;

public class MainApp extends JPanel {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainApp() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new HomePage(), "Home");
        contentPanel.add(new SettingsPage(), "Settings");
        contentPanel.add(new ProfilePage("example@email.com"), "Profile");
        contentPanel.add(new StudentsPage(), "Students");
        contentPanel.add(new CoursePage(), "Courses");
        contentPanel.add(new AdminDashboard(), "Dashboard");
        contentPanel.add(new LecturersPage(), "Lecturers");



         SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, "Admin", "Admin");
        // SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, " Lecturer", "Lecturer");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
