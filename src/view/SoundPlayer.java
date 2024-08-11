package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class SoundPlayer {

    private static final SoundPlayer INSTANCE = new SoundPlayer();

    private Clip myBackgroundMusicClip;

    private SoundPlayer() {super();}

    public static SoundPlayer getInstance() {return INSTANCE;}

    public void playBackgroundMusic() {
        try {
            final File path = new File("./audio/mixkit-game-level-music-689.wav");
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(path);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            myBackgroundMusicClip = clip;
            audioInputStream.close();

        } catch (final Exception e) {
            System.out.println("Background music unavailable: " + e.getMessage());
        }
     }
}
