package window.panel;

import audio.SoundManager;
import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InstructionsPanel extends GamePanel {

    /**
     * Creates the instructions screen displayed before the game begins.
     * Also basically the main menu of the game
     * @param frame current game frame being used
     */
    public InstructionsPanel(GameFrame frame){
        super(frame);

        setBackground(new Color(20, 20, 40));

        JLabel title = new JLabel("GAME INSTRUCTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        title.setForeground(Color.CYAN);
        title.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(30));
        add(title);
        add(Box.createVerticalStrut(40));

        InstructionSubPanel subPanel = new InstructionSubPanel(frame);
        add(subPanel);
        add(Box.createVerticalStrut(30));

        VolumeControlPanel volumeControlPanel = new VolumeControlPanel(frame);
        add(volumeControlPanel);
        add(Box.createVerticalStrut(40));

        // todo 'ENTER' to play

        setupKeyBind(KeyStroke.getKeyStroke("ENTER"), "startGame", () -> startNewGame(frame));
    }

    /**
     * Starts a new game by switching the screen to the MainGamePanel.
     * @param frame current game frame
     */
    private void startNewGame(GameFrame frame) {
        frame.setScreen(new MainGamePanel(frame));
        SoundManager.get().restartMusic();
    }
}
