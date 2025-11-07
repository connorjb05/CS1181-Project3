package window.panel;

import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PausePanel extends GamePanel {

    private final MainGamePanel gamePanel;
    private final BufferedImage background;

    public PausePanel(GameFrame frame, MainGamePanel gamePanel, BufferedImage background) {
        super(frame);

        this.gamePanel = gamePanel;
        this.background = background;

        setLayout(new GridBagLayout());
        setOpaque(false);

        PauseOptionsPanel pauseOptionsPanel = new PauseOptionsPanel(frame);

        add(pauseOptionsPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            // Draw blurred snapshot
            g.drawImage(blurImage(background, 8), 0, 0, getWidth(), getHeight(), null);
        }

        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /*
    Credit to ChatGPT
     */
    private BufferedImage blurImage(BufferedImage img, int radius) {
        if (radius < 1) return img;
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage blurred = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blurred.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        // Simple blur by averaging neighboring pixels (fast + light)
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int rgb = 0;
                for (int dy = -1; dy <= 1; dy++)
                    for (int dx = -1; dx <= 1; dx++)
                        rgb += img.getRGB(x + dx, y + dy);
                rgb /= 9;
                blurred.setRGB(x, y, rgb);
            }
        }
        return blurred;
    }

    public MainGamePanel getGamePanel(){
        return this.gamePanel;
    }
}
