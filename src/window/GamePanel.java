package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class GamePanel extends JPanel {

    public final GameFrame frame;

    public GamePanel(GameFrame frame){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.frame = frame;
    }

    public void setupKeyBind(String key, String name, Runnable action, boolean pressed) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        

        int keyCode = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
        KeyStroke keyStroke = pressed ? KeyStroke.getKeyStroke(keyCode, 0, false)  // key press
                : KeyStroke.getKeyStroke(keyCode, 0, true);  // key release

        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public void setupKeyBind(KeyStroke keyStroke, String actionKey, Runnable action){
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionKey);
        getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }
}
