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
        contentPanel.add(new ProfilePage("example"), "Profile");
        contentPanel.add(new StudentsPage(), "Students");
        contentPanel.add(new CoursePage(), "Courses");
        contentPanel.add(new LecturersPage(), "Lecturers");
        contentPanel.add(new AdminDashboard(), "Dashboard");


        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, "Admin", "Admin");
//        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, " Lecturer", "Lecturer");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
