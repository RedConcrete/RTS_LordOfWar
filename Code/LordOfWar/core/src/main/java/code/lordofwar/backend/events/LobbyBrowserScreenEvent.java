package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;


import java.util.ArrayList;
import java.util.Arrays;

import static code.lordofwar.backend.MessageIdentifier.GET_LOBBYS;
import static code.lordofwar.backend.MessageIdentifier.JOIN_LOBBY;

public class LobbyBrowserScreenEvent {

    private LOW game;
    private String[] lobbyList;
    private String joined;

    public LobbyBrowserScreenEvent(LOW game) {
        this.game = game;
        lobbyList = new String[0];
        joined=null;
    }

    public void sendRequestGetLobbys(){
        game.getWebSocket().send(DataPacker.packData(GET_LOBBYS,game.getSessionID()));
    }

    public void sendRequestJoinLobby(String lobbyName){
        ArrayList<String> data=new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyName);
        game.getWebSocket().send(DataPacker.packData(JOIN_LOBBY, DataPacker.stringCombiner(data)));
    }

    public void setJoined(String[] lobbyJoinRequestResponse) {
        if (lobbyJoinRequestResponse[1].equals("true")) {
            this.joined = lobbyJoinRequestResponse[2];
        }
    }

    public String getJoined() {
        return joined;
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
        System.out.println(Arrays.toString(lobbyList));
    }

    public String[] getLobbyList() {
        return lobbyList;
    }
}
