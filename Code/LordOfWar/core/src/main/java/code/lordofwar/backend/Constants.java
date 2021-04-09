package code.lordofwar.backend;

import com.badlogic.gdx.Graphics;

public class Constants {

    public static final int WORLD_WIDTH_PIXEL = 1920;
    public static final int WORLD_HEIGHT_PIXEL = 1080;
    public static final float CAMERASPEED = 500f;
    public static final String STRINGSEPERATOR = "///";

    public Boolean WEBSOCKET_OPEN = false;
    public Boolean FPS = false; //Todo an und ausschaltbar in Optionen
    public Boolean MUSIC = true;


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
