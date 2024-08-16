/**
 * TCSS 360 - Trivia Maze
 * SoundPlayer.java
 */
package view;

import java.io.File;

import javax.sound.sampled.*;

/**
 * Plays sound effects and music to the game
 *
 * @author Eric John
 * @version 8/15/2024
 */
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
     * Winning music for the game.
     */
    private Clip myWinClip;

    /**
     * Losing music for the game.
     */
    private Clip myLoseClip;

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
        myMute = !myMute;
        if (myMute) {
            stopBackgroundMusic();
        } else {
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
     public void setVolume(final float theVolume) {
         myVolume = theVolume;
         if (myBackgroundMusicClip != null) {
             setClipVolume(myBackgroundMusicClip, theVolume);
         }
         if (myWinClip != null) {
             setClipVolume(myWinClip, theVolume);
         }
         if (myLoseClip != null) {
             setClipVolume(myLoseClip, theVolume);
         }

     }

    /**
     * Gets the volume of the game
     * @return A float representing the volume of the game.
     */
    public float getVolume() {
         return myVolume;
     }

    /**
     * Gets if the audio is muted or not.
     * @return True if it is muted, false otherwise.
     */
    public boolean isMuted() {
        return myMute;
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
            setClipVolume(clip, myVolume);
            clip.start();
            audioStream.close();
        } catch (final Exception e) {
            System.out.println("Sound playing unavailable: " + e.getMessage());
        }
     }

    /**
     * Plays the winning music.
     */
    public void playWinMusic() {
         stopAllMusic();
         playDelayedMusic("audio/mixkit-game-level-completed-2059.wav", myWinClip);
     }

    /**
     * Play the losing music.
     */
    public void playLoseMusic() {
        stopAllMusic();
        playDelayedMusic("audio/mixkit-horror-lose-2028.wav", myLoseClip);
    }

    /**
     * Delays the music for the game
     * @param theFilePath - The filePath of the audio
     * @param theClip - The clip to play it to.
     */
    private void playDelayedMusic(final String theFilePath, final Clip theClip) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1 second delay
                playMusic(theFilePath, theClip);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }


    /**
     * Stops all the music for the game.
     */
    private void stopAllMusic() {
        if (myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning()) {
            myBackgroundMusicClip.stop();
        }
        if (myWinClip != null && myWinClip.isRunning()) {
            myWinClip.stop();
        }
        if (myLoseClip != null && myLoseClip.isRunning()) {
            myLoseClip.stop();
        }
    }

    /**
     * Plays music from a specific file path.
     * @param theFilePath - The file for the audio
     * @param theClip - The clip to play it from
     */
    private void playMusic(final String theFilePath, Clip theClip) {
        try {
            if (theClip != null && theClip.isRunning()) {
                theClip.stop();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(theFilePath));
            theClip = AudioSystem.getClip();
            theClip.open(audioInputStream);
            setClipVolume(theClip, myVolume);
            theClip.start();
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }

    /**
     * Sets the clip volume
     * @param theClip - The clip for the audio
     * @param theVolume - The volume for the music
     */
    private void setClipVolume(final Clip theClip, final float theVolume) {
        if (theClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) theClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(theVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }


}
