package window.panel;

import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InstructionsPanel extends GamePanel {

    public InstructionsPanel(GameFrame frame){
        super(frame);

        JLabel title = new JLabel("Instructions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(CENTER_ALIGNMENT);

        String instructionsText = """
                Controls:
                A - Move Left
                D - Move Right
                Space - Jump/Double Jump
                Hold Jump - Glide
                
                ------------------------------------------------
                Press ENTER to Start the Game
                ------------------------------------------------
                """;

        JTextArea instructions = new JTextArea(instructionsText);
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 14));
        instructions.setBackground(this.getBackground());
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("How to Play"));

        add(Box.createVerticalStrut(20));
        add(title);
        add(Box.createVerticalStrut(20));
        add(scrollPane);

        getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "startGame");

        getActionMap().put("startGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setScreen(new MainGamePanel(frame));
            }
        });
    }

}
