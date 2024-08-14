package view;

import javax.sound.sampled.*;
import java.io.File;

public final class SoundPlayer {

    /**
     * Singleton instance of the audio for the game
     */
    private static final SoundPlayer INSTANCE = new SoundPlayer();

    /**
     * Clip for the background music
     */
    private transient Clip myBackgroundMusicClip;

    /**
     * Position of where in the audio track the music is.
     */
    private long myBackgroundMusicPosition;

    /**
     * Boolean to see if the music is muted
     */
    private boolean myMute;

    /**
     * The volume for the game.
     */
    private float myVolume = 0.3f;

    /**
     * Private constructor for audio
     */
    private SoundPlayer() {super();}

    /**
     * The instance of the audio
     * @return Singleton instance of the audio
     */
    public static SoundPlayer getInstance() {return INSTANCE;}

    /**
     * Plays the background music
     */
    public void playBackgroundMusic() {
        if (myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning()) {
            return;
        }
        try {
            final File path = new File("audio/mixkit-game-level-music-689.wav");
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(path);
            myBackgroundMusicClip = AudioSystem.getClip();
            myBackgroundMusicClip.open(audioInputStream);
            myBackgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            setVolume(myVolume);
            if (!myMute) {
                myBackgroundMusicClip.start();
            }
            audioInputStream.close();

        } catch (final Exception e) {
            System.out.println("Background music unavailable: " + e.getMessage());
        }
     }

    /**
     * Stops the background music
     */
    public void stopBackgroundMusic() {
        if (myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning()) {
            myBackgroundMusicPosition = myBackgroundMusicClip.getMicrosecondPosition();
            myBackgroundMusicClip.stop();
        }
     }

    /**
     * Continues the background music
     */
    public void resumeBackgroundMusic() {
        if (myBackgroundMusicClip != null && !myBackgroundMusicClip.isRunning()
            && !myMute) {
            myBackgroundMusicClip.setMicrosecondPosition(myBackgroundMusicPosition);
            myBackgroundMusicClip.start();
        }
     }

    /**
     * Mutes the background music
     */
    public void muteBackgroundMusic() {
        if (myBackgroundMusicClip != null) {
            myMute = true;
            stopBackgroundMusic();
        } else {
            myMute = false;
            resumeBackgroundMusic();
        }
     }

    /**
     * Checks if the background music is running
     * @return True if it is, false otherwise
     */
     public boolean isBackgroundMusicRunning() {
        return myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning();
     }

    /**
     * Sets the volume of the game
     * @param theVolume - The volume for the audio
     */
     public void setVolume(float theVolume) {

        if (myBackgroundMusicClip != null) {
            FloatControl gainControl = (FloatControl) myBackgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(theVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
     }

    /**
     * Plays a sound effect
     * @param thePathName - The file path for the sound effect
     */
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
