package code.lordofwar.backend;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The Class Backgroundmusic is supposed to play Backgroundmusik in the menu as well as in the Game itself.
 *
 * @author Robin Hefner
 */
public class BackgroundMusic {
    private String currentTrack;//currently playing track
    private AudioInputStream currentStream;//current stream
    private Clip currentClip;//current clip
    private final Object synchronizer;//synchronizer
    private final Constants constants;

    public BackgroundMusic(Constants con) {
        currentTrack = null;
        currentStream = null;
        currentClip = null;
        synchronizer = new Object();
        constants = con;
    }

    /**
     * Plays the given audiofile.
     * The audiofile has to be in assets/music.
     * Currently only WAVE, AU & AIFF files are supported.
     *
     * @param track the name of the track to be named
     *///TODO replace tracknames with enums?
    public synchronized void music(String track) {
        //CURRENTLY ONLY PLAYES WAVE AU AND AIFF Files
        //THIS MEANS CONVERT THE MP3 Files to .wav files using a CONVERTER!
        if (currentTrack == null) {//TODO return something here so the program knows it plays
            currentTrack = track;
            final String trackName = "assets/music/" + currentTrack;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File musicFile = new File(trackName).getAbsoluteFile();
                    if (musicFile.exists()) {
                        try {
                            synchronized (synchronizer) {
                                currentStream = AudioSystem.getAudioInputStream(musicFile);
                                currentClip = AudioSystem.getClip();
                                currentClip.open(currentStream);
                                setVolume(constants.musicVolume);
                                currentClip.start();
                                currentClip.loop(Clip.LOOP_CONTINUOUSLY);
                                synchronizer.wait();//waits for notify here
                                currentClip.stop();
                                currentClip = null;
                                currentStream = null;
                                currentTrack = null;
                            }
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    /**
     * Stops the tracks currently being played by this instance.
     */
    public synchronized void stopMusic() {
        synchronized (synchronizer) {
            synchronizer.notifyAll();//thread(s) should autoterminate after this
        }
    }

    /**
     * Changes volume of the currently playing clip to the given volume percentage.
     *
     * @param volume the given volume in percent
     */
    public synchronized void setVolume(float volume) {
        //6.0206 -80.0 were the values on my pc TODO Check the sout on others pc
        if (currentClip != null) {
            float range=Math.abs(((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum()-((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum());
            ((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum()+((range/100)*volume));//might have to change type to MASTER_GAIN if exception is thrown
        }
    }

    //might delete later TODO
//    /**
//     * @return a float array containing the minimum and maximum values allowed by the current clip in that order. If no clip is currently playing null is returned.
//     */
//    public synchronized float[] getBounds() {
//        if (currentClip != null) {
//            return new float[]{((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum(),
//                    ((FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum()
//            };
//        } else return null;
//    }


}



