package code.lordofwar.backend.events;

import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

public class Events {

    WebSocket webSocket;
    LOW game;

    public Events(LOW aGame) {
        game = aGame;
        webSocket = aGame.getWebSocket();
    }

}
