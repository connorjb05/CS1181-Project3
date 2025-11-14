package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Platform extends GameObject {


    public Platform(int y, int width, int height) {
        super(0, y, width, height);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(new Color(0, 0, 0, 100));
        graphics.fillRect(x, y, width, height);
    }
}
