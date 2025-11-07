package window.panel;

import window.GameFrame;
import window.GamePanel;
import window.objects.impl.Obstacle;
import window.objects.impl.Platform;
import window.objects.impl.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
todo
add random generator for adding obstacles (blocks that the user would hae to either jump over or go under) stacked and/or floating
make screen move slowly to the left (ground that way the blocks/obstacles placed are moving to the left)
if user goes off the left side of the screen (becuase floor is moving, they are eliminated)
*/
public class MainGamePanel extends GamePanel implements GameFrame.UpdatableScreen{

    private final int OBSTACLE_INTERVAL = 5; // todo make this randomized (faster based on level)

    private final Platform platform = new Platform(0, 0, 50);
    private final Player player = new Player(0, 0);
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final boolean[] keys = new boolean[256];

    private int level;
    private int levelTimer;
    public static int LEVELS_PLATFORM_SPEED = 5;
    private int obstacleTimer = 0;

    public MainGamePanel(GameFrame frame) {
        super(frame);

        setFocusable(true);
        requestFocusInWindow();

        setupKeyBinds();

        SwingUtilities.invokeLater(() -> {
            platform.setX(0);
            platform.setY(getHeight() - 50);
            platform.setWidth(getWidth());
            platform.setHeight(50);

            player.setX(platform.getWidth() / 2 - player.getWidth() / 2);
            player.setY(platform.getY() - player.getHeight());
        });
    }

    @Override
    public void update() {

        for (Obstacle obstacle : obstacles) {
            obstacle.setX(obstacle.getX() - LEVELS_PLATFORM_SPEED);
        }

        if (keys[KeyEvent.VK_D]) player.moveX(3);
        if (keys[KeyEvent.VK_A]) player.moveX(-3);

        if (keys[KeyEvent.VK_SPACE]) player.jump();

        player.update();

        if (player.getX() + player.getWidth() > getWidth()) {
            player.setX(getWidth() - player.getWidth());
        }

        if (player.getY() < 0) {
            player.setY(0);
            player.setVelocityY(0);
        }

        if (player.getY() + player.getHeight() >= platform.getY()) {
            player.land(platform.getY());
            player.moveX(-LEVELS_PLATFORM_SPEED);
        }

        obstacles.forEach(player::collideWithObstacle);
        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() < 0);

        obstacleTimer++;
        if (obstacleTimer >= OBSTACLE_INTERVAL) {
            obstacleTimer = 0;
            spawnObstacles();
        }

        if (player.getX() + player.getWidth() <= 0) {
            System.out.println("GAME OVER!");
            frame.setScreen(new InstructionsPanel(frame));
        }
    }

    private void spawnObstacles(){

        int obstacleWidth = 50;
        int obstacleHeight = 50;
        int minY = platform.getY() - obstacleHeight;
        int maxY = platform.getY() - 150;

        int spawnX = getWidth() + 100;
        int spawnY = minY;

        if (Math.random() < 0.5) {
            spawnY = maxY + (int)(Math.random() * (minY - maxY));
        }

        if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getX() < getWidth() - 200) {
            obstacles.add(new Obstacle(spawnX, spawnY, obstacleWidth, obstacleHeight));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics){

        super.paintComponent(graphics);

        platform.setY(getHeight() - platform.getHeight());
        platform.setWidth(getWidth());
        platform.setX(0);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        platform.draw(graphics);
        player.draw(graphics);
        obstacles.forEach(o -> o.draw(graphics));
    }

    private void setupKeyBinds() {
        setupKeyBind("D", "pressRight", () -> keys[KeyEvent.VK_D] = true, true);
        setupKeyBind("D", "releaseRight", () -> keys[KeyEvent.VK_D] = false, false);

        setupKeyBind("A", "pressLeft", () -> keys[KeyEvent.VK_A] = true, true);
        setupKeyBind("A", "releaseLeft", () -> keys[KeyEvent.VK_A] = false, false);

        setupKeyBind(KeyStroke.getKeyStroke("SPACE"), "pressJump", player::jump);

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pauseGame", this::pauseGame);
    }

    /*
    Credit to ChatGPT for the blurred background
     */
    private void pauseGame(){
        BufferedImage snapshot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = snapshot.createGraphics();
        paint(g2d);
        g2d.dispose();

        frame.setScreen(new PausePanel(frame, this, snapshot));
    }
}
