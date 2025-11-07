package window.objects.impl;

import window.GameFrame;
import window.objects.GameObject;
import window.panel.MainGamePanel;

import java.awt.*;

public class Player extends GameObject {

    private final double GRAVITY = 0.6;
    private final double FRICTION = 0.6;

    private double velocityX;
    private double velocityY;
    private boolean onGround = false;
    private boolean doubleJumped = false;

    public Player(int startX, int startY) {
        super(startX, startY, 30, 30);

        x = startX;
        y = startY;
    }

    public void moveX(double deltaX){
        velocityX += deltaX;
    }

    public void jump(){
        if (onGround) {
            velocityY = -12;
            onGround = false;
        } else if (!doubleJumped) {

            velocityY = -12;
            onGround = false;

            doubleJumped = true;
        }
    }

    public void land(int groundY){
        y = groundY - height;
        onGround = true;
        doubleJumped = false;
    }

    public void collideWithObstacle(Obstacle obstacle){
        if (intersects(obstacle)) {

            int playerBottom = y + height;
            int playerTop = y;
            int playerLeft = x;
            int playerRight = x + width;

            int obstacleTop = obstacle.getY();
            int obstacleBottom = obstacle.getY() + obstacle.getHeight();
            int obstacleLeft = obstacle.getX();
            int obstacleRight = obstacle.getX() + obstacle.getWidth();

            int tolerance = 5;

            // collisions for landing on top of an obstacle
            if (playerBottom > obstacleTop - tolerance && playerTop < obstacleTop && velocityY >= 0) {

                setY(obstacleTop - height);
                setVelocityX(-MainGamePanel.LEVELS_PLATFORM_SPEED / FRICTION);
                setVelocityY(0);

                onGround = true;
                doubleJumped = false;

                return;
            }

            // Collisions for hitting bottom of obstacle
            if (playerTop <= obstacleBottom && playerBottom > obstacleBottom && velocityY < 0) {

                setY(obstacleBottom);
                setVelocityY(0);

                return;
            }

            // Collisions for hitting left side of obstacle
            if (playerRight > obstacleLeft && playerLeft < obstacleLeft) {

                setX(obstacleLeft - width);
                setVelocityX(0);

                return;
            }

            // Collisions for hitting right side of obstacle
            if (playerLeft < obstacleRight && playerRight > obstacleRight) {

                setX(obstacleRight);
                setVelocityX(0);

                return;
            }
        }
    }

    @Override
    public void update(){
        onGround = false;

        x += (int) velocityX;
        velocityX *= FRICTION;

        if (Math.abs(velocityX) <= 0.5) velocityX = 0;

        velocityY += GRAVITY;
        y += (int) velocityY;

        //if (onGround) velocityY = 0.0;

        //onGround = false;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        graphics.fillOval(x, y, width, height);
    }

    public void setVelocityX(double velocityX){
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY){
        this.velocityY = velocityY;
    }
}
