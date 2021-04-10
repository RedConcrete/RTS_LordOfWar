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

        ArrayList<String> lobbyArr = new ArrayList<>();
        //0
        lobbyArr.add(game.getSessionID()); //1
        lobbyArr.add(lobby.getLobbyname());//2
        lobbyArr.add(lobby.getMap());//3
        lobbyArr.add(String.valueOf(lobby.getPlayerAmount()));//4
        lobbyArr.add(lobby.getGamemode());//5

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
