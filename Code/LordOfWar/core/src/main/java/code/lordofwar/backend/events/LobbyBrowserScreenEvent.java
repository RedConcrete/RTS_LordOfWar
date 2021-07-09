package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.GET_LOBBYS;
import static code.lordofwar.backend.MessageIdentifier.JOIN_LOBBY;

public class LobbyBrowserScreenEvent extends Events {

    private String[] lobbyList;
    private String[] lobbyInfo;

    public LobbyBrowserScreenEvent(LOW game) {
        super(game);
        lobbyList = new String[0];
        lobbyInfo = null;
    }

    public void sendRequestGetLobbys() {
        webSocket.send(DataPacker.packData(GET_LOBBYS, game.getSessionID()));
    }

    public void sendRequestJoinLobby(String lobbyName) {
        ArrayList<String> data = new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyName);
        webSocket.send(DataPacker.packData(JOIN_LOBBY, DataPacker.stringCombiner(data)));
    }

    public void setJoined(String[] lobbyJoinRequestResponse) {
        if (lobbyJoinRequestResponse[1].equals("true")) {
            lobbyInfo = new String[lobbyJoinRequestResponse.length - 2];
            System.arraycopy(lobbyJoinRequestResponse, 2, this.lobbyInfo, 0, lobbyJoinRequestResponse.length - 2);

        }
    }

    public String[] getLobbyInfo() {
        return lobbyInfo;
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
    }

    public String[] getLobbyList() {
        return lobbyList;
    }
}
