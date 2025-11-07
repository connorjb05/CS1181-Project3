package window.panel;

import window.GameFrame;
import window.GamePanel;

import javax.swing.*;

public class PauseOptionsPanel extends GamePanel {
    public PauseOptionsPanel(GameFrame frame) {
        super(frame);

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // todo buttons
    }
}
