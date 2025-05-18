package Example_Screen.View;

import javax.swing.*;
import java.awt.*;

public class GraficoCircular extends JPanel {
    private int progreso;

    public GraficoCircular(int progreso) {
        this.progreso = this.progreso;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight()) - 20;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(20));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawArc(x, y, size, size, 0, 360);

        g2.setColor(Color.GREEN.darker());
        g2.drawArc(x, y, size, size, 90, -progreso * 360 / 100);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String texto = progreso + "%";
        FontMetrics fm = g2.getFontMetrics();
        int textX = getWidth() / 2 - fm.stringWidth(texto) / 2;
        int textY = getHeight() / 2 + fm.getAscent() / 2;
        g2.setColor(Color.BLACK);
        g2.drawString(texto, textX, textY);
    }
}

