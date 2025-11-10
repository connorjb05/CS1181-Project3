package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class GamePanel extends JPanel {

    public final GameFrame frame;

    /**
     * Constructs a new GamePanel instance
     *
     * @param frame the parent GameFrame that contains the panel
     */
    public GamePanel(GameFrame frame){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.frame = frame;
    }

    /**
     * Sets up a keyboard binding using a single character key.
     * Executes the specified action when the key is pressed or released
     *
     * @param key the character key to bind
     * @param name a unique name for the key binding
     * @param action the action to run when the key event occurs
     * @param pressed true to trigger on key press, false for key release
     */
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

    /**
     * Sets up a keyboard binding using a specific KeyStroke.
     * Executes the given action when the KeyStroke is triggered.
     *
     * @param keyStroke the KeyStroke to bind
     * @param actionKey a unique identifier for the key action
     * @param action the action to execute when the key event occurs
     */
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
