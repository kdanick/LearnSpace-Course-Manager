import javax.swing.*;
import java.awt.*;

public class App {
    private final JFrame frame;

    public App() {
        frame = new JFrame("LearnSpace");
        Image logo = Toolkit.getDefaultToolkit().getImage("resources/logo.png");
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setMinimumSize(new Dimension(1200, 700));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        showLogin(); // Show the login page first
    }

    public void showLogin() {
        frame.setContentPane(new Login(this));
        frame.setVisible(true); // Show the login frame
    }

    public void showMainApp(Integer user_id, String role, String username) {
        frame.setContentPane(new MainApp(user_id, role, username)); // Switch to main app
        frame.setVisible(true); // Show the main application frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }


}