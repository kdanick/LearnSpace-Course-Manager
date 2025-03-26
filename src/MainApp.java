import javax.swing.*;
import java.awt.*;

public class MainApp extends JPanel {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainApp(String role, String username) {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        initializeContentPanels();

        SidebarPanel sidebar = createSidebar(role, username);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initializeContentPanels() {
        contentPanel.add(new HomePage(), "Home");
        contentPanel.add(new SettingsPage(), "Settings");
        contentPanel.add(new ProfilePage("example@email.com"), "Profile");
        contentPanel.add(new StudentsPage(), "Students");
        contentPanel.add(new CoursePage(), "Courses");
        contentPanel.add(new AdminDashboard(), "Dashboard");
        contentPanel.add(new LecturersPage(), "Lecturers");
        contentPanel.add(new UsersPage(), "Users");
        contentPanel.add(new EnrollmentPage(), "Enrollments");
    }

    private SidebarPanel createSidebar(String role, String username) {
        if (role.equalsIgnoreCase("Admin")) {
            return new SidebarPanel(contentPanel, cardLayout, username, "Admin");
        } else if (role.equalsIgnoreCase("Lecturer")) {
            return new SidebarPanel(contentPanel, cardLayout, username, "Lecturer");
        } else {
            return new SidebarPanel(contentPanel, cardLayout, username, "User");
        }
    }
}