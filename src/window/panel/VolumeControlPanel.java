package window.panel;

import audio.SoundManager;
import components.Slider;
import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;

public class VolumeControlPanel extends GamePanel {

    /**
     * Creates the volume control panel used inside the pause menu. This panel
     * contains sliders for adjusting background music and sound effects volume.
     * @param frame the main game frame used for context
     */
    public VolumeControlPanel(GameFrame frame) {
        super(frame);

        setLayout(new GridLayout(4, 1, 10, 10));
        setBackground(new Color(25, 25, 50));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 1),
                "Volume Settings",
                0, 0, new Font("Arial", Font.BOLD, 18), Color.CYAN
        ));

        /*
        Credit to ChatGPT for the Slider class UI that was used here
        and the styling used here (listener as well)
         */

        JLabel musicLabel = createLabel("Background Music Volume");
        JSlider musicSlider = new JSlider(0, 100, (int)(SoundManager.get().getBackgroundMusicVolume() * 100));
        musicSlider.setUI(new Slider(musicSlider));
        styleSlider(musicSlider);
        musicSlider.addChangeListener(e -> {
            float volume = musicSlider.getValue() / 100f;
            SoundManager.get().setBackgroundMusicVolume(volume);
        });

        JLabel effectsLabel = createLabel("Sound Effects Volume");
        JSlider effectsSlider = new JSlider(0, 100, (int)(SoundManager.get().getSoundEffectsVolume() * 100));
        effectsSlider.setUI(new Slider(effectsSlider));
        styleSlider(effectsSlider);
        effectsSlider.addChangeListener(e -> {
            float volume = effectsSlider.getValue() / 100f;
            SoundManager.get().setSoundEffectsVolume(volume);
        });

        add(musicLabel);
        add(musicSlider);
        add(effectsLabel);
        add(effectsSlider);
    }

    /**
     * Creates a styled JLabel for section titles within the volume settings panel.
     * @param text the text content to display on the label
     * @return a formatted JLabel centered and styled for the panel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    /**
     * Applies visual and behavioral styling to a JSlider used for volume adjustment.
     * This removes ticks, labels, and focus effects while keeping the slider clean
     * and consistent with the UI theme.
     * @param slider the JSlider to style
     */
    private void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setBorder(null);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setFocusable(false);
        slider.setSnapToTicks(false);
    }
}
