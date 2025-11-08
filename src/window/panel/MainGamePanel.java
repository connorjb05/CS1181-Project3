package window.panel;

import audio.SoundManager;
import window.GameFrame;
import window.GamePanel;
import window.objects.impl.Obstacle;
import window.objects.impl.Platform;
import window.objects.impl.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/*
todo
add random generator for adding obstacles (blocks that the user would hae to either jump over or go under) stacked and/or floating
make screen move slowly to the left (ground that way the blocks/obstacles placed are moving to the left)
if user goes off the left side of the screen (becuase floor is moving, they are eliminated)

// todo BOMBSSSS (occasionally spawn bombs that explode and if the user is in radius it will damage them, or eleminate them if they are close enough/hit it
// todo random times where the floor flashes red, (floor is lava type of thing briefly)

*/
public class MainGamePanel extends GamePanel implements GameFrame.UpdatableScreen{

    private boolean preparing = true;
    private int preparationCountDown = 3;

    private final int BACKGROUND_SPEED = 2;
    private int backgroundX = -getWidth();
    private BufferedImage background;

    private final Platform platform = new Platform(0, 0, 50);
    private final Player player = new Player(0, 0);

    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int obstacleTimer = 0;

    private final boolean[] keys = new boolean[256];

    private double score = 0;
    public static int PLATFORM_SPEED = 15; // 10

    public MainGamePanel(GameFrame frame) {
        super(frame);

        try {
            background = ImageIO.read(getClass().getResource("/assets/pngtree-game-space-interstellar-image_2408883.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        new Timer(750, e -> {
            preparationCountDown--;
            if (preparationCountDown <= 0) {
                preparing = false;
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    @Override
    public void update() {

        if (preparing) return;

        updateBackground();
        updatePlayer();
        updateObstacles();

        if (player.getX() + player.getWidth() <= 0) {

            SoundManager.get().stopMusic();
            // todo death sound maybe a delay?
            player.gameOver();
            try {
                Thread.sleep(1500);
            } catch (Exception ignored) {}
            SoundManager.get().restartMusic();

            System.out.println("GAME OVER!");
            frame.setScreen(new InstructionsPanel(frame));

            return;
        }

        updateScore();
    }

    private void updateObstacles(){

        obstacles.forEach(obstacle -> obstacle.setX(obstacle.getX() - PLATFORM_SPEED));

        obstacles.forEach(player::collideWithObstacle);
        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() < 0);

        obstacleTimer++;
        if (obstacleTimer >= 0) { // todo randomize interval based on level
            obstacleTimer = 0;
            spawnObstacles();
        }
    }

    private void updatePlayer(){
        if (keys[KeyEvent.VK_D]) player.moveX(2.3);
        if (keys[KeyEvent.VK_A]) player.moveX(-2.3);

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
        }

        double groundDrag = player.isOnGround() ? -PLATFORM_SPEED * 0.15 : 0;
        player.moveX(groundDrag);
    }

    private void updateBackground(){
        if (background == null) return;

        int panelHeight = getHeight();

        double scale = (double) panelHeight / background.getHeight();
        int scaledWidth = (int) (background.getWidth() * scale);

        backgroundX -= BACKGROUND_SPEED;

        if (backgroundX <= -scaledWidth) {
            backgroundX += scaledWidth;
        }
    }

    private void updateScore(){

        final double baseIncrement = 1.0;
        final double randomFactor = 0.5 + Math.random();
        final double increment = baseIncrement / (Math.log(score + 2));

        score += increment * randomFactor;
    }

    private void spawnObstacles() {

        // todo customize for loop so that there are multiple obstacles that spawn at a time based on level
        for (int i = 0; i < 1; i++) {

            int maxWidth = 100;
            int minWidth = 50;
            int maxHeight = 100;
            int minHeight = 30;

            int obstacleWidth = (int) (Math.random() * (maxWidth - minWidth + 1)) + minWidth;
            int obstacleHeight = (int) (Math.random() * (maxHeight - minHeight + 1)) + minHeight;
            int minY = platform.getY() - obstacleHeight - ((int) (Math.random() * 100));
            int maxY = platform.getY() - getHeight() + 100;

            int spawnX = getWidth() + obstacleWidth; // + 100
            int spawnY = minY;

            if (Math.random() < 0.5) {
                spawnY = maxY + (int) (Math.random() * (minY - maxY));
            }

            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getX() < getWidth() - 50) {
                obstacles.add(new Obstacle(spawnX, spawnY, obstacleWidth, obstacleHeight));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics){

        super.paintComponent(graphics);

        platform.setY(getHeight() - platform.getHeight());
        platform.setWidth(getWidth());
        platform.setX(0);

        paintBackground(graphics);
        paintScore(graphics);

        platform.draw(graphics);
        player.draw(graphics);
        obstacles.forEach(o -> o.draw(graphics));

        paintCountDown(graphics);
    }

    private void paintCountDown(Graphics graphics){
        if (preparing && preparationCountDown > 0) {
            graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 72));
            graphics.setColor(Color.WHITE);
            String text = String.valueOf(preparationCountDown);
            int width = graphics.getFontMetrics().stringWidth(text);
            graphics.drawString(text, getWidth()/2 - width/2, getHeight()/2);
        }
    }

    // credit to ChatGPT
    private void paintBackground(Graphics graphics){

        if (background != null) {

            int panelWidth = getWidth();
            int panelHeight = getHeight();

            double scale = (double) panelHeight / background.getHeight();
            int scaledWidth = (int) (background.getWidth() * scale);

            int x = backgroundX;

            while (x < panelWidth) {
                graphics.drawImage(background, x, 0, scaledWidth, panelHeight, null);
                x += scaledWidth;
            }
        } else {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }

    }

    private void paintScore(Graphics graphics){

        graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        graphics.setColor(Color.WHITE);

        String scoreText = "Score: " + (int)score;
        int textWidth = graphics.getFontMetrics().stringWidth(scoreText);
        graphics.drawString(scoreText, getWidth() / 2 - textWidth / 2, 50);
    }

    private void setupKeyBinds() {
        setupKeyBind("D", "pressRight", () -> keys[KeyEvent.VK_D] = true, true);
        setupKeyBind("D", "releaseRight", () -> keys[KeyEvent.VK_D] = false, false);

        setupKeyBind("A", "pressLeft", () -> keys[KeyEvent.VK_A] = true, true);
        setupKeyBind("A", "releaseLeft", () -> keys[KeyEvent.VK_A] = false, false);

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false),
                "pressJumpOrGlide", () -> {
                    if (!player.isOnGround()) player.setGliding(true);
                    player.jump();
                });

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                "releaseGlide", () -> player.setGliding(false));

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextSong", () -> {
            SoundManager.get().playNextMusic();
        });

        //setupKeyBind(KeyStroke.getKeyStroke("SPACE"), "pressJump", player::jump);

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pauseGame", this::pauseGame);
    }

    /*
    Credit to ChatGPT for the blurred background
     */
    private void pauseGame(){
        BufferedImage snapshot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = snapshot.createGraphics();
        paint(graphics);
        graphics.dispose();

        frame.setScreen(new PausePanel(frame, this, snapshot));

        SoundManager.get().pauseMusic();
    }
}
