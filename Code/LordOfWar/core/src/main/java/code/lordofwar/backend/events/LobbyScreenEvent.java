package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;
import java.util.Arrays;

import static code.lordofwar.backend.MessageIdentifier.LEAVE_LOBBY;
import static code.lordofwar.backend.MessageIdentifier.LOBBY_PLAYERS;

public class LobbyScreenEvent {
    WebSocket webSocket;
    LOW game;
    private String[] players;
    private boolean recievedData;
    public LobbyScreenEvent(LOW aGame) {
        game = aGame;
        webSocket = aGame.getWebSocket();
        players = null;
        recievedData=false;
    }

    public void sendLeaveLobbyNotice(String lobbyName) {
        ArrayList<String> leaveData = new ArrayList<>();
        leaveData.add(game.getSessionID());
        leaveData.add(lobbyName);
        webSocket.send(DataPacker.packData(LEAVE_LOBBY, DataPacker.stringCombiner(leaveData)));
    }

    public void sendPlayerRequest(String lobbyName){
        ArrayList<String> playerRequest = new ArrayList<>();
        playerRequest.add(game.getSessionID());
        playerRequest.add(lobbyName);
        webSocket.send(DataPacker.packData(LOBBY_PLAYERS, DataPacker.stringCombiner(playerRequest)));

    }

    public void setPlayers(String[] players) {
        this.players = new String[players.length - 1];
        if (players.length - 1 >= 0) {
            recievedData=true;
            System.arraycopy(players, 1, this.players, 0, players.length - 1);
        }
    }

    public boolean isRecievedData() {
        return recievedData;
    }

    public String[] getPlayers() {
        return players;
    }
}
