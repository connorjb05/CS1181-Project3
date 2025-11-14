package window.panel;

import audio.SoundManager;
import window.GameFrame;
import window.GamePanel;
import window.objects.impl.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainGamePanel extends GamePanel implements GameFrame.UpdatableScreen{

    private boolean gameOver;
    private boolean preparing;
    private int preparationCountDown;

    private final int BACKGROUND_SPEED = 2;
    private int backgroundX;
    private BufferedImage background;

    private final Platform platform;
    private final Player player;

    private final ArrayList<Obstacle> obstacles;
    private int obstacleTimer;

    private final ArrayList<FireBall> fireBalls;
    private int fireBallTimer;

    private final boolean[] keys = new boolean[256];

    private double score;
    private double lastMilestone;
    public static double PLATFORM_SPEED;

    /**
     * Constructor that sets up a new game and the main ongoing game panel
     *
     * @param frame the main frame this panel will be displayed upon
     */
    public MainGamePanel(GameFrame frame) {
        super(frame);

        // initialize all variables for the new game
        gameOver = false;
        preparing = true;
        preparationCountDown = 3;
        backgroundX = -getWidth();
        platform = new Platform(0, 0, 50);
        player = new Player(getWidth() / 2, getHeight() / 2);
        obstacles = new ArrayList<>();
        fireBalls = new ArrayList<>();
        obstacleTimer = 0;
        fireBallTimer = 0;
        score = 0;
        lastMilestone = 0;
        PLATFORM_SPEED = 5;

        try {
            background = ImageIO.read(getClass().getResource("/assets/image/pngtree-game-space-interstellar-image_2408883.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFocusable(true);
        requestFocusInWindow();

        // load all key binds and their actions
        setupKeyBinds();

        // On a different thread initialize the player and platform
        SwingUtilities.invokeLater(() -> {
            platform.setX(0);
            platform.setY(getHeight() - 50);
            platform.setWidth(getWidth());
            platform.setHeight(50);

            player.setX(platform.getWidth() / 2 - player.getWidth() / 2);
            player.setY(platform.getY() - player.getHeight());
        });

        // Initiate game starting count down
        new Timer(750, e -> {
            preparationCountDown--;
            if (preparationCountDown <= 0) {
                preparing = false;
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    /**
     * Game's main update loop (runs per frame change)
     */
    @Override
    public void update() {

        if (gameOver) return; // don't update game if the game is over
        if (preparing) return; // don't update game if the preparation countdown is not complete

        updateBackground();
        updatePlayer();
        updateObstacles();
        updateFireBalls();

        // If the player's x coordinate leaves the screen then the game is over
        if (player.getX() + player.getWidth() <= 0) {
            gameOver();
            return;
        }

        updateScore();
    }

    /**
     * Forces game over
     * Displays the starting panel, then disposes of this panel in memory
     */
    private void gameOver(){
        SoundManager.get().stopMusic();

        if (Math.random() < 0.5) {
            SoundManager.get().playSound("gameover", "game_over_1");
        } else {
            SoundManager.get().playSound("gameover", "game_over_2");
        }

        try {
            Thread.sleep(1250);
        } catch (Exception ignored) {}

        SoundManager.get().restartMusic();

        System.out.println("GAME OVER!");
        frame.setScreen(new InstructionsPanel(frame));

        gameOver = true;
    }

    /**
     * Removes fireballs that are not being used (off the screen)
     * Moves each fireball from right to left at the speed of the platform
     * Generates and spawns more fire balls based on the players score
     */
    private void updateFireBalls() {

        fireBalls.forEach(FireBall::update);

        fireBalls.removeIf(fireBall -> fireBall.getX() + fireBall.getWidth() < 0);

        int baseSpawnDelay = 180;
        int minSpawnDelay = 60;
        int spawnDelay = Math.max(minSpawnDelay, baseSpawnDelay - (int) (score / 500.0) * 15);

        int maxFireBalls = 3;

        fireBallTimer++;

        if (fireBallTimer >= spawnDelay && fireBalls.size() < maxFireBalls) {
            fireBallTimer = 0;

            int spawnY = (int) (Math.random() * (getHeight() - 150));
            fireBalls.add(new FireBall(getWidth() + 50, spawnY, 50, 50));
        }

        for (FireBall fireBall : fireBalls) {
            if (!player.intersects(fireBall)) continue;
            gameOver();
            break;
        }
    }

    /**
     * Removes obstacles that are no longer in use (off of the screen)
     * Moves each obstacle from right to left at the speed of the platform
     * Generates and spawns more obstacles based on the players score
     */
    private void updateObstacles(){

        obstacles.forEach(obstacle -> obstacle.setX(obstacle.getX() - (int)Math.ceil(PLATFORM_SPEED)));

        obstacles.forEach(player::collideWithObstacle);
        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() < 0);

        obstacleTimer++;
        if (obstacleTimer >= 0) { // todo randomize interval based on level
            obstacleTimer = 0;
            spawnObstacles();
        }
    }

    /**
     * Update the players movement based off their input
     * and where they are on the screen
     */
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

    /**
     * Updates the background image of the game
     * moves the background image continuously through a layered image
     */
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

    /**
     * Updates the players score, score goes up slower as the game continues
     */
    private void updateScore(){

        final double baseIncrement = 1.0;
        final double randomFactor = 0.5 + Math.random();
        final double increment = baseIncrement / (Math.log(score + 2));

        score += increment * randomFactor;

        if (score - lastMilestone >= 250) {
            PLATFORM_SPEED += 1.0;
            lastMilestone = Math.floor(score / 250.0) * 250.0;
        }
    }

    /**
     * Randomly generates size of an obstacle and spawns it at a random height from the platform
     * to the top of the screen
     */
    private void spawnObstacles() {

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

        if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getX() < getWidth() - 50) { // - 150
            obstacles.add(new Obstacle(spawnX, spawnY, obstacleWidth, obstacleHeight));
        }
    }

    /**
     *
     * @param graphics the <code>Graphics</code> object to protect
     */
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
        fireBalls.forEach(f -> f.draw(graphics));

        paintCountDown(graphics);
    }

    /**
     *
     * @param graphics Graphics driver this panel is using
     */
    private void paintCountDown(Graphics graphics){
        if (preparing && preparationCountDown > 0) {
            graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 72));
            graphics.setColor(Color.WHITE);
            String text = String.valueOf(preparationCountDown);
            int width = graphics.getFontMetrics().stringWidth(text);
            graphics.drawString(text, getWidth()/2 - width/2, getHeight()/2);
        }
    }

    /**
     * Paints background so that it is moving
     * CREDIT TO CHATGPT FOR THE MOVING BACKGROUND
     * @param graphics Graphics driver this panel is using
     */
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

    /**
     * Displays and updates the score of the player at the top of the screen
     * @param graphics Graphics driver this panel is using
     */
    private void paintScore(Graphics graphics){

        graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        graphics.setColor(Color.WHITE);

        String scoreText = "Score: " + (int)score;
        int textWidth = graphics.getFontMetrics().stringWidth(scoreText);
        graphics.drawString(scoreText, getWidth() / 2 - textWidth / 2, 50);
    }

    /**
     * Sets up key input and actions for this panel
     */
    private void setupKeyBinds() {

        // basic left and right motion using 'D' and 'A'
        setupKeyBind("D", "pressRight", () -> keys[KeyEvent.VK_D] = true, true);
        setupKeyBind("D", "releaseRight", () -> keys[KeyEvent.VK_D] = false, false);

        setupKeyBind("A", "pressLeft", () -> keys[KeyEvent.VK_A] = true, true);
        setupKeyBind("A", "releaseLeft", () -> keys[KeyEvent.VK_A] = false, false);

        // pressing space forces player to jump, if release input and action is not called then glide is set to true
        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false),
                "pressJumpOrGlide", () -> {
                    if (!player.isOnGround()) player.setGliding(true);
                    player.jump();
                });

        // releasing space bar sets players glide to false
        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                "releaseGlide", () -> player.setGliding(false));

        // Right arrow forces the song to change to the next song in the track list
        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextSong", () -> {
            SoundManager.get().playNextMusic();
        });

        setupKeyBind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pauseGame", this::pauseGame);
    }

    /**
     * Pauses the game and displays a paused panel
     * allowing the user to customize volume of sound effects or music
     * and 'resume', 'restart', or 'quit' the game
     *
     * CREDIT TO CHATGPT FOR THE ATTEMPT OF A BLURRED BACKGROUND (I ended up liking the glitch look and kept that instead)
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
