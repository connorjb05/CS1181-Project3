package window.panel;

import audio.SoundManager;
import components.Button;
import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseOptionsPanel extends GamePanel {

    /**
     * Constructs the pause options panel, which appears when the user pauses the game.
     * This panel provides buttons for resuming, restarting, quitting, and adjusting volume.
     * @param frame the main game frame used to switch screens
     */
    public PauseOptionsPanel(GameFrame frame) {
        super(frame);

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        Button resumeButton = new Button("Resume", () -> resumeGame(frame));
        Button restartButton = new Button("Restart", () -> restartGame(frame));
        Button quitButton = new Button("Quit", () -> quitGame(frame));

        resumeButton.setFontStyle("Arial", Font.BOLD, 20)
                .setColors(new Color(40, 40, 40), Color.WHITE);
        restartButton.setFontStyle("Arial", Font.BOLD, 20)
                .setColors(new Color(40, 40, 40), Color.WHITE);
        quitButton.setFontStyle("Arial", Font.BOLD, 20)
                .setColors(new Color(40, 40, 40), Color.WHITE);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escapePressed");
        getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeGame(frame);
            }
        });

        add(centerButton(resumeButton));
        add(Box.createVerticalStrut(20));
        add(centerButton(restartButton));
        add(Box.createVerticalStrut(20));
        add(centerButton(quitButton));

        add(Box.createVerticalStrut(40));
        add(new VolumeControlPanel(frame));

        add(Box.createVerticalGlue());
    }

    /**
     * Resumes the game by returning to the previous game panel.
     * Also resumes the currently playing background music.
     * @param frame the main game frame used to access the paused game panel
     */
    private void resumeGame(GameFrame frame){
        if (frame.getContentPane().getComponent(0) instanceof PausePanel pausePanel) {
            frame.setScreen(pausePanel.getGamePanel());
        }

        SoundManager.get().resumeMusic();
    }

    /**
     * Restarts the game by creating a new MainGamePanel instance.
     * Also restarts the background music from the beginning.
     * @param frame the main game frame used to load the new game panel
     */
    private void restartGame(GameFrame frame){
        frame.setScreen(new MainGamePanel(frame));
        SoundManager.get().restartMusic();
    }

    /**
     * Quits the current game session and returns the user to the instructions screen.
     * Also stops all background music.
     * @param frame the main game frame used to switch back to the instructions panel
     */
    private void quitGame(GameFrame frame){
        frame.setScreen(new InstructionsPanel(frame));
        SoundManager.get().stopMusic();
    }

    /**
     * Wraps a button in a transparent panel and centers it horizontally.
     * @param button the button to be centered in the panel
     * @return a JPanel that centers the given button
     */
    private JPanel centerButton(JButton button) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(button);
        return panel;
    }
}
