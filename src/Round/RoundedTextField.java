package Round;

import javax.swing.*;
import java.awt.*;

public class RoundedTextField extends JTextField {
    private int radius = 25;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Adjust padding
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        // Border color and thickness
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2)); // Border thickness
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}