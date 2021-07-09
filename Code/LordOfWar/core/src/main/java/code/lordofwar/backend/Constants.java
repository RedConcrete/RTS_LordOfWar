package code.lordofwar.backend;

/**
 *
 * @author Robin Hefner
 */
public class Constants {

    public static final int WORLD_WIDTH_PIXEL = 1920;
    public static final int WORLD_HEIGHT_PIXEL = 1080;
    public static final float CAMERASPEED = 500f;
    public static final String STRINGSEPERATOR = "///";
    public static final String BLOCK_TILE_PROPERTY="blocked";
    //GAME STATICS
    //TODO FILL THESE ARRAYS with actual coords
    public static final float[] MAP1CC1=new float[]{416,352};//insert coords here (416/352)
    public static final float[] MAP1CC2=new float[]{416,4192};//insert coords here (416/4192)
    public static final float[] MAP1CC3=new float[]{4320,4320};//insert coords here (4320/4320)
    public static final float[] MAP1CC4=new float[]{4320,352};//insert coords here (4320/352)

    public Boolean WEBSOCKET_OPEN;
    public Boolean FPS;
    public Boolean MUSIC;
    public int STANDARD_TIME_WAIT;
    public int musicVolume;//volume of the music(percentage ->0=mute 100=full)
    public Constants(){
        WEBSOCKET_OPEN = false;
        FPS = false;
        MUSIC = true;
        STANDARD_TIME_WAIT=2000;
        musicVolume=100;
    }

    public Boolean getMUSIC() {
        return MUSIC;
    }

    public void setMUSIC(Boolean MUSIC) {
        this.MUSIC = MUSIC;
    }

    public Boolean getFPS() {
        return FPS;
    }

    public void setFPS(Boolean FPS) {
        this.FPS = FPS;
    }

    public Boolean getWEBSOCKET_OPEN() {
        return WEBSOCKET_OPEN;
    }

    public void setWEBSOCKET_OPEN(Boolean WEBSOCKET_OPEN) {
        this.WEBSOCKET_OPEN = WEBSOCKET_OPEN;
    }
}
