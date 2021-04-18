package code.lordofwar.backend;



public class Constants {

    public static final int WORLD_WIDTH_PIXEL = 1920;
    public static final int WORLD_HEIGHT_PIXEL = 1080;
    public static final float CAMERASPEED = 500f;
    public static final String STRINGSEPERATOR = "///";

    //GAME STATICS
    //TODO FILL THESE ARRAYS with actual coords
    private static final float[] MAP1CC1=new float[]{};//insert coords here
    private static final float[] MAP1CC2=new float[]{};//insert coords here
    private static final float[] MAP1CC3=new float[]{};//insert coords here
    private static final float[] MAP1CC4=new float[]{};//insert coords here
    private static final float[] MAP1CC5=new float[]{};//insert coords here
    private static final float[] MAP1CC6=new float[]{};//insert coords here

    public Boolean WEBSOCKET_OPEN;
    public Boolean FPS;
    public Boolean MUSIC;

    public Constants(){
        WEBSOCKET_OPEN = false;
        FPS = false;
        MUSIC = true;
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
