package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.LOBBY_CREATE;
import static code.lordofwar.backend.MessageIdentifier.REGISTER;

public class LobbyCreateScreenEvent {

    WebSocket webSocket;
    DataPacker dataPacker = new DataPacker();
    LOW game;

    public LobbyCreateScreenEvent(LOW game) {
        this.webSocket = game.getWebSocket();
        this.game = game;
    }

    public void sendLobbyData(ArrayList<String> arr){
        webSocket.send(dataPacker.packData(LOBBY_CREATE, dataPacker.stringCombiner(arr)));
    }

    public static boolean isLobbyCreated(){
        return true;
    }
}
