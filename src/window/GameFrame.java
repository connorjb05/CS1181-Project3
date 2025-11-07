package window;

import window.panel.InstructionsPanel;

import javax.swing.*;

/*
Game idea:

Block as user:
double jump
glide

map:
slowly is being eaten by left side
forcing user to speed up
obstacles randomly generated forcing user to jump
balls come from right or top of screen (can damage/eliminate)
after x time of proceeding, then completion occurs, screen says go again or restart
if user fails or dies screen says restart or quit game
*/

public class GameFrame extends JFrame {

    private final int FPS = 120;
    private final int FRAME_TIME = 1000 / FPS;

    private Timer gameTimer;
    private GamePanel currentPanel = null;

    public GameFrame(){
        super("Pixel Runner");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /*
        example:
        setScreen(new INstructionsPanel);
         */
        setScreen(new InstructionsPanel(this));

        gameTimer = new Timer(FRAME_TIME, e -> {

            if (currentPanel instanceof UpdatableScreen screen) {
                screen.update();
            }

            repaint();
        });
        gameTimer.start();
    }

    /**
     *
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



    public interface UpdatableScreen {
        void update();
    }
}
