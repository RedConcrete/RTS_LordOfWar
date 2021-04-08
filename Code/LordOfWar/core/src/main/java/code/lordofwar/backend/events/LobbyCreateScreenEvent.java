package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.backend.Lobby;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;
import java.util.Collections;

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
        isCreated = false;
    }

    public void sendLobbyCreateRequest(Lobby lobby) {

        ArrayList<String> lobbyArr = new ArrayList();

        lobbyArr.add(game.getSessionID());
        lobbyArr.add(lobby.getLobbyname());
        lobbyArr.add(lobby.getMap());
        lobbyArr.add(String.valueOf(lobby.getPlayerAmount()));
        lobbyArr.add(lobby.getGamemode());

        System.out.println(lobbyArr);
        webSocket.send(dataPacker.packData(CREATE_LOBBY,dataPacker.stringCombiner(lobbyArr)));
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
