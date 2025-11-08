package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Obstacle extends GameObject {
    public Obstacle(int x, int y, int width, int height) {
        super(x, y, width, height);

        setImage("src/assets/obstacles/obstacle.jpg");
    }

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
