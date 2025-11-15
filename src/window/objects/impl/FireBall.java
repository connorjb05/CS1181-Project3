package window.objects.impl;

import window.objects.GameObject;
import window.panel.MainGamePanel;

import java.awt.*;

public class FireBall extends GameObject {

    /**
     * Creates a new fireball object at the specified position and size
     * @param x the starting x coordinate of the fireball
     * @param y the starting y coordinate of the fireball
     * @param width the width of the fireball
     * @param height the height of the fireball
     */
    public FireBall(int x, int y, int width, int height) {
        super(x, y, width, height);

        // todo never had time to add a fireball image
        // so feel free to just place the image in the "assets" folder
        // and it will display the appropriate image in game
        setImage("src/assets/image/fireball.png");
    }

    /**
     * Updates the fireballs position every frame
     * The fireball moves from right to left of the screen (based on the platform speed)
     */
    @Override
    public void update() {
        x -= (int) Math.floor(MainGamePanel.PLATFORM_SPEED + 1);
    }

    /**
     * Renders the fireball onto the screen
     * @param graphics graphics driver
     */
    @Override
    public void draw(Graphics graphics) {
        if (image != null) {
            graphics.drawImage(image, x, y, width, height, null);
        } else {
            graphics.setColor(Color.RED);
            graphics.fillRect(x, y, width, height);
        }
    }
}
