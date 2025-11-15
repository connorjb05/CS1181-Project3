package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Player extends GameObject {

    private final double GRAVITY = 0.6;
    private final double FRICTION = 0.81;

    private double velocityX;
    private double velocityY;
    private boolean onGround = false;
    private boolean wasOnGround = false;
    private boolean doubleJumped = false;
    private boolean gliding = false;

    /**
     * Main player constructor
     * @param startX starting horizontal position of the player
     * @param startY starting vertical position of the player
     */
    public Player(int startX, int startY) {
        super(startX, startY, 75, 75);

        // load player icon image
        setImage("src/assets/image/player_icon.png");

        x = startX;
        y = startY;
    }

    /**
     * Moves the player by their x coordinate
     * @param deltaX delta change that will be added to the players horizontal velocity
     */
    public void moveX(double deltaX){
        velocityX += deltaX;
    }

    /**
     * Forces the player logic to jump or double jump
     */
    public void jump() {
        if (onGround || wasOnGround) {
            velocityY = -14;
            onGround = false;
            wasOnGround = false;
        } else if (!doubleJumped) {
            velocityY = -18;
            doubleJumped = true;
        }
    }

    /**
     * Forces the player logic to land
     * @param groundY vertical pixel at which where the player landed
     */
    public void land(int groundY){
        y = groundY - height;
        onGround = true;
        doubleJumped = false;
        gliding = false;
    }

    /**
     * Handles collisions with the obstacles within the game
     * @param obstacle the current obstacle the player MIGHT be colliding with
     */
    public void collideWithObstacle(Obstacle obstacle){

        if (!intersects(obstacle)) return;

        // Credit to ChatGPT for the Overlap Math
        int overlapX = Math.min(x + width - obstacle.getX(), obstacle.getX() + obstacle.getWidth() - x);
        int overlapY = Math.min(y + height - obstacle.getY(), obstacle.getY() + obstacle.getHeight() - y);

        // handle vertical collision only if its stronger than horizontal
        if (overlapY < overlapX - 2) { // small tolerance that favors vertical collisions
            if (y < obstacle.getY()) {
                // land on top
                y = obstacle.getY() - height;
                velocityY = 0;
                onGround = true;
                doubleJumped = false;
                gliding = false;
            } else {
                // hit bottom
                y = obstacle.getY() + obstacle.getHeight();
                velocityY = Math.max(velocityY, 0.5);
            }
        } else {
            // horizontal collisions
            if (x < obstacle.getX()) {
                // player is on the left side of obstacle
                x = obstacle.getX() - width;

                // only stop movement into the obstacle
                if (velocityX > 0) velocityX = 0;

            } else {
                // player is on the right side of obstacle
                x = obstacle.getX() + obstacle.getWidth();

                // only stop movement into the obstacle
                if (velocityX < 0) velocityX = 0;
            }

            // force a small vertical lift so gravity can pull them out
            y -= 1;
        }
    }

    /**
     * Player's update method, handles all players movement,
     * runs at frame time and forces players movement logic
     */
    @Override
    public void update(){
        wasOnGround = onGround;
        onGround = false;

        x += (int) velocityX;
        if (Math.abs(velocityX) <= 0.5) velocityX = 0;
        velocityX *= FRICTION;

        if (gliding && velocityY > 0) {
            velocityY += GRAVITY * 0.15;
        } else {
            velocityY += GRAVITY;
        }

        y += (int) velocityY;
    }

    /**
     * Draws the player on the screen. If an image is set, it renders the player
     * as that image. otherwise, it draws a solid green circle
     * as a fallback representation.
     *
     * @param graphics the Graphics context used for rendering the player
     */
    @Override
    public void draw(Graphics graphics) {

        if (image != null) {
            graphics.drawImage(image, x, y, width, height, null);
        } else {
            graphics.setColor(Color.GREEN);
            graphics.fillOval(x, y, width, height);
        }

    }

    public boolean isOnGround(){
        return this.onGround;
    }

    public void setGliding(boolean gliding){
        this.gliding = gliding;
    }

    public void setVelocityX(double velocityX){
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY){
        this.velocityY = velocityY;
    }
}
