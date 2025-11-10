package window.objects.impl;

import audio.SoundManager;
import window.GameFrame;
import window.objects.GameObject;
import window.panel.MainGamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    private final double GRAVITY = 0.6;
    private final double FRICTION = 0.81;

    private double velocityX;
    private double velocityY;
    private boolean onGround = false;
    private boolean wasOnGround = false;
    private boolean doubleJumped = false;
    private boolean gliding = false;

    public Player(int startX, int startY) {
        super(startX, startY, 75, 75);

        setImage("src/assets/player/player_icon.png");

        x = startX;
        y = startY;
    }

    public void moveX(double deltaX){
        velocityX += deltaX;
    }

    public void jump() {
        if (onGround || wasOnGround) {
            velocityY = -12;
            onGround = false;
            wasOnGround = false;
        } else if (!doubleJumped) {
            velocityY = -18;
            doubleJumped = true;
        }
    }

    public void land(int groundY){
        y = groundY - height;
        onGround = true;
        doubleJumped = false;
        gliding = false;
    }

    public void collideWithObstacle(Obstacle obstacle){

        if (!intersects(obstacle)) return;

        // Credit to ChatGPT for the Overlap Math
        int overlapX = Math.min(x + width - obstacle.getX(), obstacle.getX() + obstacle.getWidth() - x);
        int overlapY = Math.min(y + height - obstacle.getY(), obstacle.getY() + obstacle.getHeight() - y);

        if (overlapY < overlapX) {

            // vertical collisions
            if (y < obstacle.getY()) {

                y = obstacle.getY() - height;
                velocityY = 0.0;
                onGround = true;
                doubleJumped = false;
                gliding = false;
            } else {
                y = obstacle.getY() + obstacle.getHeight();
                velocityY = 0.5;
            }

        } else {

            //Horizontal collisions
            if (x < obstacle.getX()) {
                x = obstacle.getX() - width;
            } else {
                x = obstacle.getX() + obstacle.getWidth();
            }

            velocityX = 0.0;

        }
    }

    public void gameOver(){
        if (Math.random() < 0.5) {
            SoundManager.get().playSound("gameover", "game_over_1");
        } else {
            SoundManager.get().playSound("gameover", "game_over_2");
        }
    }

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

    @Override
    public void draw(Graphics graphics) {

        if (image != null) {
            graphics.drawImage(makeCircular(image), x, y, width, height, null);
        } else {
            graphics.setColor(Color.GREEN);
            graphics.fillOval(x, y, width, height);
        }
    }

    private BufferedImage makeCircular(BufferedImage source) {
        int diameter = Math.min(source.getWidth(), source.getHeight());
        BufferedImage mask = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = mask.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create circular clipping
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(source, 0, 0, diameter, diameter, null);
        g2.dispose();

        return mask;
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
