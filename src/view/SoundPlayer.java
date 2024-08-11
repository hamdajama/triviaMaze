package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class SoundPlayer {

    private static final SoundPlayer INSTANCE = new SoundPlayer();

    private Clip myBackgroundMusicClip;

    private long myBackgroundMusicPosition;

    private boolean myMute;

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
            setVolume(0.3f);
            audioInputStream.close();

        } catch (final Exception e) {
            System.out.println("Background music unavailable: " + e.getMessage());
        }
     }

     public void stopBackgroundMusic() {
        if (myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning()) {
            myBackgroundMusicPosition = myBackgroundMusicClip.getMicrosecondPosition();
            myBackgroundMusicClip.stop();
        }
     }

     public void resumeBackgroundMusic() {
        if (myBackgroundMusicClip != null && !myBackgroundMusicClip.isRunning()
            && !myMute) {
            myBackgroundMusicClip.setMicrosecondPosition(myBackgroundMusicPosition);
            myBackgroundMusicClip.start();
        }
     }

     public void muteBackgroundMusic() {
        if (myBackgroundMusicClip != null) {
            myMute = true;
            stopBackgroundMusic();
        } else {
            myMute = false;
            resumeBackgroundMusic();
        }
     }

     public boolean isBackgroundMusicRunning() {
        return myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning();
     }

     public void setVolume(float theVolume) {
        if (myBackgroundMusicClip != null) {
            FloatControl gainControl = (FloatControl) myBackgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(theVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
     }

     public void playSFX(final String thePathName) {
        try {
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(thePathName));
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            audioStream.close();
        } catch (final Exception e) {
            System.out.println("Sound playing unavailable: " + e.getMessage());
        }
     }


}
