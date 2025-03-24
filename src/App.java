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
        frame.setResizable(true);

        showLogin(); // Show the login page first
    }

    public void showLogin() {
        frame.setContentPane(new LoginPage(this));
        frame.setVisible(true);
    }

    public void showMainApp() {
        frame.setContentPane(new MainApp()); // Switch to main app
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new App();
    }
}
