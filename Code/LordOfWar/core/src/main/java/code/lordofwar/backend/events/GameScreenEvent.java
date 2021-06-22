package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.backend.Team;
import code.lordofwar.main.LOW;
import code.lordofwar.screens.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static code.lordofwar.backend.MessageIdentifier.GET_GAME_POINTS;
import static code.lordofwar.backend.MessageIdentifier.LEAVE_LOBBY;

public class GameScreenEvent extends Events {
    private int points;
    private final String lobbyID;
    private GameScreenEvent gameScreenEvent;
    private ArrayList conPlayers;
    private HashMap<String,Team> teamHashMap = new HashMap<String,Team>();


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

    public void createTeams(){
        for (int i = 0; i < conPlayers.size(); i++) {
            Team team= new Team(i);
            teamHashMap.put((String) conPlayers.get(i),team);
        }
    }

    public GameScreenEvent getGameScreenEvent() {

        return gameScreenEvent;
    }

    public void getConnectedPlayer(String[] conPlayers){
        this.conPlayers = new ArrayList<String>();
        for(int i = 0; i<conPlayers.length; i++) {
            this.conPlayers.add(conPlayers[i]);
        }
        createTeams();
    }

    public HashMap<String,Team> getTeamHashMap() {
        return teamHashMap;
    }
}
