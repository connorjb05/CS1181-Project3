package components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/*
Credit to ChatGPT for this entire class
 */
public class Slider extends BasicSliderUI {

    private final Color trackColor = new Color(40, 40, 70);
    private final Color fillColor = new Color(0, 255, 255);
    private final Color thumbColor = new Color(0, 200, 255);
    private final Color glowColor = new Color(0, 255, 255, 100);

    public Slider(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int trackY = trackRect.y + trackRect.height / 2 - 4;
        int trackHeight = 8;

        g2d.setColor(trackColor);
        g2d.fillRoundRect(trackRect.x, trackY, trackRect.width, trackHeight, 8, 8);

        int filledWidth = (int) ((slider.getValue() / 100.0) * trackRect.width);
        g2d.setColor(fillColor);
        g2d.fillRoundRect(trackRect.x, trackY, filledWidth, trackHeight, 8, 8);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = thumbRect.x + thumbRect.width / 2 - 6;
        int y = thumbRect.y + thumbRect.height / 2 - 6;
        int size = 12;

        g2d.setColor(glowColor);
        g2d.fillOval(x - 4, y - 4, size + 8, size + 8);

        g2d.setColor(thumbColor);
        g2d.fillOval(x, y, size, size);

        g2d.dispose();
    }
}
