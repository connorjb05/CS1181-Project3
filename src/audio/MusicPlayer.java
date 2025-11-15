package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.*;


// Credit to ChatGPT making this class implement runnable
public class MusicPlayer implements Runnable {

    /*

    Credit to ChatGPT for most audio code
    everything with "Clip" / currentClip was chatGPT

     */

    private final List<File> songs;
    private Iterator<File> songIterator;
    private Clip currentClip;
    private boolean running = false;
    private boolean paused = false;
    private int pausedFramePosition = 0;

    /**
     * Constructor for initializing a music player instead
     * in charge of the background music for the game
     */
    public MusicPlayer(){

        String path = "src/assets/sound/music";

        File folder = new File(path);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".wav"));

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No .wav music files found in: " + path);
        }

        songs = new ArrayList<>(Arrays.asList(files));
        shuffleSongs();
    }

    /**
     * Method for shuffling the songs inside the arraylist randomly
     */
    private void shuffleSongs(){
        Collections.shuffle(songs);
        songIterator = songs.iterator();
    }

    /**
     * Handles background music playback with pause, resume, stop and volume control
     * Manages transitions between songs and automatically plays whatever is next in the track list
     * Credit to ChatGPT for most of the audio code inside this method
     */
    private void playNext(){
        try {

            if (!songIterator.hasNext()) shuffleSongs();
            File song = songIterator.next();

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(song);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioIn);

            FloatControl floatControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = SoundManager.get().getBackgroundMusicVolume();
            float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f));
            floatControl.setValue(dB);

            currentClip.start();

            currentClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && running && !paused) {
                    currentClip.close();
                    playNext();
                }
            });

        } catch (Exception ignored) {}
    }

    /**
     * Pauses whatever current track is playing and saves the frame position of the audio clip
     */
    public void pause(){
        if (currentClip != null && currentClip.isRunning()) {
            pausedFramePosition = currentClip.getFramePosition();
            currentClip.stop();
            paused = true;
        }
    }

    /**
     * Resumes playback from the frame position of the clip that was previously paused
     */
    public void resume(){
        if (currentClip != null && paused) {
            currentClip.setFramePosition(pausedFramePosition);
            currentClip.start();
            paused = false;
        }
    }

    /**
     * Skips the current song and plays the next one in the track list
     */
    public void next(){
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }
        playNext();
    }

    /**
     * Stops playback entirely and releases any audio in use
     */
    public void stop(){
        running = false;
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }
    }

    /**
     * Continuously manages playback in a separate thread, advancing
     * to the next song once the current one finishes
     */
    @Override
    public void run() {
        running = true;
        playNext();
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * Updates playback volume for the currently active clip
     *
     * @param volume the selected volume level between 0.0 and 1.0
     */
    public void updateVolume(float volume) {
        if (currentClip != null && currentClip.isOpen()) {
            try {
                FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f));
                gainControl.setValue(dB);
            } catch (Exception ignored) {}
        }
    }

    public Clip getCurrentClip() { return currentClip; }
}
