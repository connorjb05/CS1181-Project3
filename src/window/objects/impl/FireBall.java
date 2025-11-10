package window.objects.impl;

import window.objects.GameObject;
import window.panel.MainGamePanel;

import java.awt.*;

public class FireBall extends GameObject {

    public FireBall(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        x -= (int) Math.floor(MainGamePanel.PLATFORM_SPEED + 1);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(x, y, width, height);
    }
}
