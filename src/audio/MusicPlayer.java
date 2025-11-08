package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MusicPlayer implements Runnable {

    private final List<File> songs;
    private Iterator<File> songIterator;
    private Clip currentClip;
    private boolean running = false;
    private boolean paused = false;
    private int pausedFramePosition = 0;

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


    private void shuffleSongs(){
        Collections.shuffle(songs);
        songIterator = songs.iterator();
    }

    // Credit to ChatGPT for audio code
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

    public void pause(){
        if (currentClip != null && currentClip.isRunning()) {
            pausedFramePosition = currentClip.getFramePosition();
            currentClip.stop();
            paused = true;
        }
    }

    public void resume(){
        if (currentClip != null && paused) {
            currentClip.setFramePosition(pausedFramePosition);
            currentClip.start();
            paused = false;
        }
    }

    public void next(){
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }
        playNext();
    }

    public void stop(){
        running = false;
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }
    }

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
