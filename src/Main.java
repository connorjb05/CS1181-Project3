import audio.SoundManager;
import window.GameFrame;

public class Main {

    public static void main(String[] args){

        new SoundManager();

        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true);
    }

}
