import javax.swing.*;
import java.awt.*;

public class App {
    private final JFrame frame;

    public App() {
        frame = new JFrame("LearnSpace");
        Image logo = Toolkit.getDefaultToolkit().getImage("resources/logo.png");
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 660);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        showLogin(); // Show the login page first
    }

    public void showLogin() {
//        frame.setContentPane(new Login(this));
        frame.setContentPane(new LoginPage(this));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); // Show the login frame
    }

    public void showMainApp() {
        frame.setContentPane(new MainApp()); // Switch to main app
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new MainApp()); // Set the content pane to MainApp
        frame.setVisible(true); // Show the main application frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
