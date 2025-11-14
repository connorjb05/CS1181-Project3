package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Obstacle extends GameObject {

    /**
     * Initializes an object within the game (platforms of various sizes
     * that the player can use to jump on)
     * @param x starting x location on the screen
     * @param y starting y location on the screen
     * @param width width of the object
     * @param height height of the object
     */
    public Obstacle(int x, int y, int width, int height) {
        super(x, y, width, height);

        setImage("src/assets/image/obstacle.png");
    }

    /**
     * Draws the obstacle object onto the screen
     *
     * @param graphics the Graphics context used for rendering the obstacle
     */
    @Override
    public void draw(Graphics graphics) {

        if (image != null) {
            graphics.drawImage(image, x, y, width, height, null);
        } else {
            graphics.setColor(Color.MAGENTA);
            graphics.fillRect(x, y, width, height);
        }
    }
}
