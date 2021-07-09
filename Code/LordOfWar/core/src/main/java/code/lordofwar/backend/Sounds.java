package code.lordofwar.backend;

import javax.sound.sampled.Clip;
import java.io.File;

/**
 * The Sound class plays ingame sounds, like victory sounds, killing sounds etc.
 * @author Cem Arslan
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

    }

}
