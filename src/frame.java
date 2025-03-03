import javax.swing.*;

public class frame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        ImageIcon logo = new ImageIcon("resources/212650752.png");

        frame.setIconImage(logo.getImage());
        frame.setTitle("LearnSpace");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);
    }
}
