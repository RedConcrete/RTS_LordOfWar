package code.lordofwar.backend;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * The Sound class plays ingame sounds, like victory sounds, killing sounds etc.
 * @author Robin Hefner
 */
public class Sounds {

    public static File sound;
    public static float value;
    Clip clip;

    public Sounds (){
        value = -30f;
        load();
        play(sound);
    }

    public void load(){
        sound = new File("assets/music/Ireland.wav");

    }

    public void play(File sound){
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(value);
            clip.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
