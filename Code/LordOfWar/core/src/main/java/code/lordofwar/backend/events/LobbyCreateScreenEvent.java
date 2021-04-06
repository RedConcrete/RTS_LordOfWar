package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.CREATE_LOBBY;
import static code.lordofwar.backend.MessageIdentifier.GET_GAME_POINTS;

public class LobbyCreateScreenEvent {

    WebSocket webSocket;
    DataPacker dataPacker;
    LOW game;
    private String lobbyID;
    private boolean isCreated;
    public LobbyCreateScreenEvent(LOW aGame) {
        game = aGame;
        dataPacker = new DataPacker();
        webSocket = aGame.getWebSocket();
        isCreated=false;
    }

    public void sendLobbyCreateRequest(String lobbyName) {
        ArrayList<String> data=new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyName);
        webSocket.send(dataPacker.packData(CREATE_LOBBY,dataPacker.stringCombiner(data)));
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String[] arr) {
        if (arr[1].equals("true")) {
            lobbyID = arr[2];
            isCreated=true;
        }
    }

    public boolean isCreated() {
        return isCreated;
    }
}
