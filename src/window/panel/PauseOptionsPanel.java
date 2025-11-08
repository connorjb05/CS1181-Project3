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
                .setColors(new Color(80, 0, 0), Color.WHITE);

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

    private void resumeGame(GameFrame frame){
        if (frame.getContentPane().getComponent(0) instanceof PausePanel pausePanel) {
            frame.setScreen(pausePanel.getGamePanel());
        }

        SoundManager.get().resumeMusic();
    }

    private void restartGame(GameFrame frame){
        frame.setScreen(new MainGamePanel(frame));
        SoundManager.get().restartMusic();
    }

    private void quitGame(GameFrame frame){
        frame.setScreen(new InstructionsPanel(frame));
        SoundManager.get().stopMusic();
    }

    private JPanel centerButton(JButton button) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(button);
        return panel;
    }
}
