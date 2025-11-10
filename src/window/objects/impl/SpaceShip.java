package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class SpaceShip extends GameObject {

    public SpaceShip(int x, int y) {
        super(x, y, 75, 75);

        setImage("src/assets/spaceship/space_ship.png");
    }

    @Override
    public void draw(Graphics graphics) {
        if (image != null) {
            graphics.drawImage(image, x, y, width, height, null);
        }
    }
}
