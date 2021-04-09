package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;


import static code.lordofwar.backend.MessageIdentifier.GET_LOBBYS;

public class LobbyBrowserScreenEvent {

    private LOW game;
    private DataPacker dP;
    private String[] lobbyList;

    public LobbyBrowserScreenEvent(LOW game) {
        this.game = game;
        dP = new DataPacker();
        lobbyList = null;
    }

    public void sendRequestGetLobbys(){
        game.getWebSocket().send(dP.packData(GET_LOBBYS,game.getSessionID()));
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
        System.out.println(lobbyList);
    }

    public String[] getLobbyList() {
        return lobbyList;
    }
}
