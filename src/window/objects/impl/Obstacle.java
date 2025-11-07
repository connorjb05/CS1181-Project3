package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Obstacle extends GameObject {
    public Obstacle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(x, y, width, height);
    }
}
