package audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {

    private static SoundManager instance;

    // Thread pool for sound effects and background music so they can play asynchronously
    // without blocking or lagging the GUI/game loop
    private final ExecutorService soundService;
    private MusicPlayer musicPlayer;

    private float backgroundMusicVolume = 0.5f;
    private float soundEffectsVolume = 0.5f;

    /**
     * Creates the SoundManager instance, initializes the sound thread pool,
     * which we will use for multithreading sounds
     * and automatically starts background music playback.
     */
    public SoundManager(){
        instance = this;
        soundService = Executors.newCachedThreadPool();
        startMusic();
    }

    /**
     * Plays a sound effect from the given subfolder with the specified file name.
     * The sound is loaded asynchronously so that it does not interrupt gameplay.
     * @param subFolder the subfolder inside the effects directory (e.g., "jump", "hit")
     * @param name the base file name of the sound effect (without .wav extension)
     */
    public void playSound(String subFolder, String name){
        soundService.execute(() -> {

            try {

                /*
                Credit to ChatGPT
                for the volume control
                and file look up and clip load
                 */
                File file = new File("src/assets/sound/effects/" + subFolder + "/" + name + ".wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float volume = 20f * (float) Math.log10(Math.max(soundEffectsVolume, 0.0001f));
                floatControl.setValue(volume);

                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Starts the background music which is fired from our
     * thread cache
     */
    private void startMusic(){
        musicPlayer = new MusicPlayer();
        soundService.execute(() -> musicPlayer.run());
    }

    /**
     * Pauses the currently playing background music.
     */
    public void pauseMusic(){
        if (musicPlayer != null) musicPlayer.pause();
    }

    /**
     * Resumes background music playback after it has been paused.
     */
    public void resumeMusic(){
        if (musicPlayer != null) musicPlayer.resume();
    }

    /**
     * Skips to the next track in the music playlist.
     */
    public void playNextMusic(){
        if (musicPlayer != null) musicPlayer.next();
    }

    /**
     * Stops background music playback entirely.
     */
    public void stopMusic(){
        if (musicPlayer != null) musicPlayer.stop();
    }

    /**
     * Restarts the background music playlist from the beginning.
     */
    public void restartMusic(){
        stopMusic();
        startMusic();
    }

    /**
     * Shuts down the SoundManager by stopping music playback
     * and shutting down all running audio threads.
     */
    public void shutdown(){
        stopMusic();
        soundService.shutdownNow();
    }

    /**
     * Sets the global background music volume, clamped between 0 and 1.
     * If music is currently playing, the clip volume is updated immediately.
     * @param backgroundMusicVolume the new music volume (0.0 - 1.0)
     */
    public void setBackgroundMusicVolume(float backgroundMusicVolume){
        this.backgroundMusicVolume = Math.max(0, Math.min(backgroundMusicVolume, 1));

        if (musicPlayer != null && musicPlayer.getCurrentClip() != null) {
            musicPlayer.updateVolume(backgroundMusicVolume);
        }
    }

    public void setSoundEffectsVolume(float soundEffectsVolume){
        this.soundEffectsVolume = Math.max(0, Math.min(soundEffectsVolume, 1));
    }

    public float getBackgroundMusicVolume(){
        return backgroundMusicVolume;
    }

    public float getSoundEffectsVolume(){
        return soundEffectsVolume;
    }

    /**
     * Returns the instance of the SoundManager.
     * If it does not exist yet, a new one is created.
     * @return the SoundManager instance
     */
    public static SoundManager get(){
        return instance == null ? new SoundManager() : instance;
    }
}
