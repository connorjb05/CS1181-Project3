package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Platform extends GameObject {


    /**
     * Constructs and builds the main platform of the game
     * @param y vertical position of the platform on the screen
     * @param width width of the platform
     * @param height height of the platform
     */
    public Platform(int y, int width, int height) {
        super(0, y, width, height);
    }

    /**
     * Renders the platform onto the screen
     * @param graphics graphics driver
     */
    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(new Color(0, 0, 0, 100));
        graphics.fillRect(x, y, width, height);
    }
}
