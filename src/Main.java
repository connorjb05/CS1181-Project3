import audio.SoundManager;
import window.GameFrame;

/*
Program: todo name (Project 3)
Author: Connor Benevich
Date: 11/15/2025
 */
public class Main {

    /**
     * Main driver of the game
     * @param args System input
     */
    public static void main(String[] args){

        // register sounds
        new SoundManager();

        // initialize and register the game frame
        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true); // set it to visible
    }

}
