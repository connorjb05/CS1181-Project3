package window.panel;

import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PausePanel extends GamePanel {

    private final MainGamePanel gamePanel;
    private final BufferedImage background;

    /**
     * Creates the main pause screen panel, which displays a blurred snapshot
     * of the gameplay along with the pause options menu. This panel acts as
     * the container for the pause interface when the game is paused.
     * @param frame the main game frame used for screen switching
     * @param gamePanel the active game panel that was paused
     * @param background the captured background image of the game used for the blur effect
     */
    public PausePanel(GameFrame frame, MainGamePanel gamePanel, BufferedImage background) {
        super(frame);

        this.gamePanel = gamePanel;
        this.background = background;

        setLayout(new GridBagLayout());
        setOpaque(false);

        PauseOptionsPanel pauseOptionsPanel = new PauseOptionsPanel(frame);

        add(pauseOptionsPanel);
    }

    /**
     * Paints the pause screen background. This includes drawing the blurred
     * snapshot of the paused game and applying a semi-transparent dark overlay
     * to enhance menu visibility.
     * @param g the <code>Graphics</code> object to protect
     */
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
    Credit to ChatGPT for this attempt of blurring the background image
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
