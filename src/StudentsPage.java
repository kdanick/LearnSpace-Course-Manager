import javax.swing.*;
import java.awt.*;

public class StudentsPage extends JPanel {
    public StudentsPage() {
        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Students Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel label2 = new JLabel("Here there will go a list of students", SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 18));

        add(label, BorderLayout.CENTER);
        add(label2, BorderLayout.SOUTH);
    }
}