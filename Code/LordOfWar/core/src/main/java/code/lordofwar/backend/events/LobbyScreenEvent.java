package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;

import java.util.ArrayList;
import java.util.Arrays;

import static code.lordofwar.backend.MessageIdentifier.*;

public class LobbyScreenEvent extends Events {

    private String[] players;
    private boolean recievedData;
    private boolean startedGame;
    private String[] gameData;
    private Integer position;

    public LobbyScreenEvent(LOW aGame) {
        super(aGame);
        players = null;
        recievedData = false;
        startedGame = false;
        gameData = null;
        position = null;
    }

    public void sendLeaveLobbyNotice(String lobbyName) {
        ArrayList<String> leaveData = new ArrayList<>();
        leaveData.add(game.getSessionID());
        leaveData.add(lobbyName);
        webSocket.send(DataPacker.packData(LEAVE_LOBBY, DataPacker.stringCombiner(leaveData)));
    }

    public void sendPlayerRequest(String lobbyName) {
        ArrayList<String> playerRequest = new ArrayList<>();
        playerRequest.add(game.getSessionID());
        playerRequest.add(lobbyName);
        webSocket.send(DataPacker.packData(LOBBY_PLAYERS, DataPacker.stringCombiner(playerRequest)));

    }

    public void setPlayers(String[] players) {
        this.players = new String[players.length - 2];
        if (players.length - 2 >= 0) {
            recievedData = true;//todo move later
            System.arraycopy(players, 1, this.players, 0, players.length - 2);
            position = Integer.parseInt(players[players.length - 1]);//set starting position here
        }
    }

    public boolean isStartedGame() {
        return startedGame;
    }

    public String[] getGameData() {
        return gameData;
    }

    public void setGameData(String[] gameData) {
        this.gameData = new String[gameData.length - 1];
        if (gameData.length - 1 >= 0) {
            System.arraycopy(gameData, 1, this.gameData, 0, gameData.length - 1);
            startedGame = true;
        }
    }

    public void sendGameStartRequest(String lobbyID) {
        ArrayList<String> startRequest = new ArrayList<>();
        startRequest.add(game.getSessionID());
        startRequest.add(lobbyID);
        webSocket.send(DataPacker.packData(GAME_START, DataPacker.stringCombiner(startRequest)));
    }

    public boolean isRecievedData() {
        return recievedData;
    }

    public String[] getPlayers() {
        return players;
    }

    public Integer getPosition() {
        return position;
    }
}
