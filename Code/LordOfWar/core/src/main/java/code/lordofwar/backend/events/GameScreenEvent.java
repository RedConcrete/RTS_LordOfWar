package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.GET_GAME_POINTS;

public class GameScreenEvent {

    WebSocket webSocket;
    DataPacker dataPacker;
    LOW game;
    private int points;
    private final String lobbyID;
    private int userScore;
    public GameScreenEvent(LOW aGame,String lobbyID) {
        points = 0;
        game = aGame;
        dataPacker = new DataPacker();
        webSocket = aGame.getWebSocket();
        this.lobbyID=lobbyID;
    }

    public void sendPointRequest() {
        //TODO EDIT REQUEST AND GAME SO SERVER KNOWS WHICH LOBBY
        ArrayList<String> data=new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyID);
        webSocket.send(dataPacker.packData(GET_GAME_POINTS, dataPacker.stringCombiner(data)));
    }

    public int getPoints() {
        return points;
    }

    public void updatePoints(String[] arr) {
        if (Integer.parseInt(arr[1]) >= 0) {
            this.points = Integer.parseInt(arr[1]);
        }
    }
}
