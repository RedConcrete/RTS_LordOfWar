package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.GET_GAME_POINTS;
import static code.lordofwar.backend.MessageIdentifier.LEAVE_LOBBY;

public class GameScreenEvent extends Events {
    private int points;
    private final String lobbyID;

    public GameScreenEvent(LOW aGame, String lobbyID) {
        super(aGame);
        points = 0;
        this.lobbyID = lobbyID;
    }

    public void sendPointRequest() {
        ArrayList<String> data = new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyID);
        webSocket.send(DataPacker.packData(GET_GAME_POINTS, DataPacker.stringCombiner(data)));
    }

    public int getPoints() {
        return points;
    }

    public void updatePoints(String[] arr) {
        if (Integer.parseInt(arr[1]) >= 0) {
            this.points = Integer.parseInt(arr[1]);
        }
    }

    public void sendLeaveGameNotice() {
        ArrayList<String> leaveData = new ArrayList<>();
        leaveData.add(game.getSessionID());
        leaveData.add(lobbyID);
        webSocket.send(DataPacker.packData(LEAVE_LOBBY, DataPacker.stringCombiner(leaveData)));
    }
}
