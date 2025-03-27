import javax.swing.*;
import java.awt.*;

public class MainApp extends JPanel {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainApp(Integer user_id, String role, String username) {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        initializeContentPanels(user_id);

        SidebarPanel sidebar = createSidebar(role, username);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initializeContentPanels(Integer user_id) {
        contentPanel.add(new HomePage(user_id), "Home");
        contentPanel.add(new ProfilePage(user_id), "Profile");
        contentPanel.add(new StudentsPage(), "Students");
        contentPanel.add(new CoursePage(), "Courses");
        contentPanel.add(new AdminDashboard(user_id), "Dashboard");
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