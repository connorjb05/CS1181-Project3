package window.objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class GameObject {

    protected int x, y;
    protected int width, height;
    protected BufferedImage image = null;

    public GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    public boolean intersects(GameObject gameObject){
        return getBounds().intersects(gameObject.getBounds());
    }

    public void update(){}

    public abstract void draw(Graphics graphics);

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

    public void setImage(String path) {
        try {
            // credit to chatGPt
            image = ImageIO.read(getClass().getResource(path));
        } catch (Exception exception) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
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
