package window.objects.impl;

import window.objects.GameObject;

import java.awt.*;

public class Platform extends GameObject {

    private boolean platformVisible = true;
    private boolean platformFlashing = false;
    private int platformFlashTimer = 0;
    private int platformDisappearTimer = 0;
    private int nextPlatformEventScore = 1000;

    public Platform(int y, int width, int height) {
        super(0, y, width, height);
    }

    @Override
    public void update(){

        if (platformFlashing) {

            platformFlashTimer++;

            if (platformFlashTimer >= 120) {

                platformFlashing = false;
                platformVisible = false;
                platformDisappearTimer = 0;

            }

        }

        if (!platformVisible) {

            platformDisappearTimer++;

            if (platformDisappearTimer >= 180) {

                platformVisible = true;
                nextPlatformEventScore += (int) ((Math.random() * (1250 - 500) + 500));

            }

        }

    }

    @Override
    public void draw(Graphics graphics) {
        if (platformFlashing) {
            boolean red = (platformFlashTimer / 10) % 2 == 0;
            graphics.setColor(red ? Color.RED : Color.WHITE);
            graphics.fillRect(x, y, width, height);
        } else {
            graphics.setColor(new Color(0, 0, 0, 100));
            graphics.fillRect(x, y, width, height);
        }
    }

    public boolean isPlatformVisible() {
        return platformVisible;
    }

    public void setPlatformVisible(boolean platformVisible) {
        this.platformVisible = platformVisible;
    }

    public boolean isPlatformFlashing() {
        return platformFlashing;
    }

    public void setPlatformFlashing(boolean platformFlashing) {
        this.platformFlashing = platformFlashing;
    }

    public int getPlatformFlashTimer() {
        return platformFlashTimer;
    }

    public void setPlatformFlashTimer(int platformFlashTimer) {
        this.platformFlashTimer = platformFlashTimer;
    }

    public int getPlatformDisappearTime() {
        return platformDisappearTimer;
    }

    public void setPlatformDisappearTime(int platformDisappearTimer) {
        this.platformDisappearTimer = platformDisappearTimer;
    }

    public int getNextPlatformEventScore() {
        return nextPlatformEventScore;
    }

    public void setNextPlatformEventScore(int nextPlatformEventScore) {
        this.nextPlatformEventScore = nextPlatformEventScore;
    }

    public boolean isPlatformDisappearTimerActive() {
        return platformDisappearTimer > 0 && platformDisappearTimer < 180;
    }
}
