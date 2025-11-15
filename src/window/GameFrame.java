package window;

import window.panel.InstructionsPanel;

import javax.swing.*;

public class GameFrame extends JFrame {

    private final int FPS = 1000;
    private final int FRAME_TIME = 1000 / FPS;

    private Timer gameTimer;
    private GamePanel currentPanel = null;

    /**
     * Creates the main game window
     * Initializes the JFrame settings and loads the initial screen
     * starts the game timer responsible for updating and repainting the active panel
     */
    public GameFrame(){
        super("Project3");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /*
        example:
        setScreen(new INstructionsPanel);
         */
        setScreen(new InstructionsPanel(this));

        // Creates a game loop using a swing timer.
        // Every frame, the active panel is updated (if updatable) then repainted.
        gameTimer = new Timer(FRAME_TIME, e -> {

            if (currentPanel instanceof UpdatableScreen screen) {
                screen.update();
            }

            repaint();
        });
        gameTimer.start();
    }

    /**
     * Changes the active screen shown in the game window.
     * This method removes the previous panel, adds the new one,
     * and forces a layout refresh.
     * @param gamePanel
     */
    public void setScreen(GamePanel gamePanel){

        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = gamePanel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    /**
     * Updatable screen interface for screens that require animations
     */
    public interface UpdatableScreen {
        void update();
    }
}
