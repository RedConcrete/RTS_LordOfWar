package code.lordofwar.backend;



import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * The Class Backgroundmusic is supposed to play Backgroundmusik in the menu as well as in the Game itself.
 * @author Robin Hefner
 */
public class BackgroundMusic {

    public static synchronized void music(String track) {
        final String trackName = track;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(trackName));
                        clip.open(inputStream);
                        clip.loop(clip.LOOP_CONTINUOUSLY);
                        Thread.sleep(clip.getMicrosecondLength() / 1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}



