package audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {

    private static SoundManager instance;

    private final ExecutorService soundService;
    private Thread musicThread;
    private MusicPlayer musicPlayer;

    private float backgroundMusicVolume = 0.5f;
    private float soundEffectsVolume = 0.5f;

    public SoundManager(){

        instance = this;

        soundService = Executors.newCachedThreadPool();

        startMusic();
    }

    /*
    Credit to ChatGPT
     */
    public void playSound(String subFolder, String name){
        soundService.execute(() -> {

            try {

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

    private void startMusic(){
        musicPlayer = new MusicPlayer();

        musicThread = new Thread(musicPlayer);
        musicThread.setDaemon(true);
        musicThread.start();
    }

    public void pauseMusic(){
        if (musicPlayer != null) musicPlayer.pause();
    }

    public void resumeMusic(){
        if (musicPlayer != null) musicPlayer.resume();
    }

    public void playNextMusic(){
        if (musicPlayer != null) musicPlayer.next();
    }

    public void stopMusic(){
        if (musicPlayer != null) musicPlayer.stop();
    }

    public void restartMusic(){
        stopMusic();
        startMusic();
    }

    public void shutdown(){
        stopMusic();
        soundService.shutdownNow();
    }

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

    public static SoundManager get(){
        return instance == null ? new SoundManager() : instance;
    }
}
