package window.panel;

import window.GameFrame;
import window.GamePanel;

import javax.swing.*;
import java.awt.*;

public class InstructionSubPanel extends GamePanel {

    /**
     * Creates a sub-panel that displays the game's control instructions.
     * @param frame current game frame
     */
    public InstructionSubPanel(GameFrame frame) {
        super(frame);

        setLayout(new GridLayout(0, 1, 5, 5));
        setBackground(new Color(30, 30, 60));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20,  50));

        JLabel moveLeft = createInstructionLabel("A - Move Left", Color.YELLOW);
        JLabel moveRight = createInstructionLabel("D - Move Right", Color.YELLOW);
        JLabel jump = createInstructionLabel("Space - Jump / Double Jump", Color.ORANGE);
        JLabel glide = createInstructionLabel("Hold Space - Glide", Color.ORANGE);

        add(moveLeft);
        add(moveRight);
        add(jump);
        add(glide);
    }

    /**
     * Creates and styles an instruction label with the given text and color.
     * Labels are centered, bold, and sized for readability on the instructions screen.
     * @param text  the instruction text to display
     * @param color the color used for the text
     * @return a fully styled JLabel containing the instruction
     */
    private JLabel createInstructionLabel(String text, Color color){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
