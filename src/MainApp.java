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
        contentPanel.add(new ProfilePage(), "Profile");

//        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, "Prof. James Doe", "Admin");
        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout, "Dr. John Doe", "Lecturer");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
