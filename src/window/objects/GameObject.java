package window.objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class used for creating in game objects, such as the player, obstacles, fireballs and more
 */
public abstract class GameObject {

    protected int x, y;
    protected int width, height;
    protected BufferedImage image = null;

    /**
     * Initializes the object within the game
     * @param x starting x pixel-location
     * @param y starting y pixel-location
     * @param width width of the object (in pixels)
     * @param height height of the object (in pixels)
     */
    public GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Bounding box of the object (used for collisions)
     * @return rectangle shape giving us the bounds/hitbox of the object
     */
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    /**
     * Checks if the bounding box of this game object intersects paths
     * with another game object
     * @param gameObject other game object we are checking bounds of
     * @return true if this game object collides/intersects the other game object
     */
    public boolean intersects(GameObject gameObject){
        return getBounds().intersects(gameObject.getBounds());
    }

    /**
     * Update this object every game frame
     */
    public void update(){}

    /**
     * Draw the object onto the screen
     * if the game panel implements UpdatableScreen, then it will
     * repaint automatically animating the panel
     * @param graphics graphics driver
     */
    public abstract void draw(Graphics graphics);

    /**
     * Gives the game object an icon
     * @param path path of where the image is
     */
    public void setImage(String path) {
        try {
            // credit to chatGPT
            image = ImageIO.read(getClass().getResource(path));
        } catch (Exception exception) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException ioException) {
                System.out.println("Could not find image in path: " + path);
                //ioException.printStackTrace();
            }
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public BufferedImage getImage(){
        return image;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }
}
