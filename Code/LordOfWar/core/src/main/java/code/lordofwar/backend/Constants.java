package code.lordofwar.backend;

public class Constants {

    public static final int WORLD_WIDTH_PIXEL = 1920;
    public static final int WORLD_HEIGHT_PIXEL = 1080;
    public static final String STRINGSEPERATOR = "///";
    public Boolean FPS = false; //Todo an und ausschaltbar in Optionen
    public Boolean MUSIC = true;
    private Boolean CLOSEWEBSOCKET = false;

    public Boolean getCLOSEWEBSOCKET() {
        return CLOSEWEBSOCKET;
    }

    public void setCLOSEWEBSOCKET(Boolean CLOSEWEBSOCKET) {
        this.CLOSEWEBSOCKET = CLOSEWEBSOCKET;
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

}
