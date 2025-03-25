package Round;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private int radius = 25; // Corner radius for rounding
    private Color defaultColor = new Color(0, 123, 255); // Default button color
    private Color hoverColor = new Color(0, 86, 179); // Color on hover

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false); // Remove focus painting
        setContentAreaFilled(false); // Make transparent
        setOpaque(false); // Make background transparent
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Adjust padding
        setForeground(Color.WHITE); // Text color
        setBackground(defaultColor); // Set default background color

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor); // Change background on hover
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor); // Reset background color
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); // Rounded background

        g2.setColor(Color.WHITE); // Text color
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3); // Centered text

        g2.dispose();
    }
}