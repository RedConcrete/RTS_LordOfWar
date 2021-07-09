package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.backend.Lobby;
import code.lordofwar.main.LOW;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.CREATE_LOBBY;


/**
 * The event from the lobbyscreen
 *
 * @author Franz Klose,Robin Hefner,Cem Arslan
 */
public class LobbyCreateScreenEvent extends Events{
    private String lobbyID;
    private boolean isCreated;

    public LobbyCreateScreenEvent(LOW aGame) {
        super(aGame);
        isCreated = false;
    }

    /**
     * sends a lobby request
     * @param lobby
     */
    public void sendLobbyCreateRequest(Lobby lobby) {

        ArrayList<String> lobbyArr = new ArrayList<>();
        //0
        lobbyArr.add(game.getSessionID()); //1
        lobbyArr.add(lobby.getLobbyname());//2
        lobbyArr.add(lobby.getMap());//3
        lobbyArr.add(String.valueOf(lobby.getPlayerAmount()));//4
        lobbyArr.add(lobby.getGamemode());//5

        webSocket.send(DataPacker.packData(CREATE_LOBBY, DataPacker.stringCombiner(lobbyArr)));
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String[] arr) {
        if (arr[1].equals("true")) {
            lobbyID = arr[2];
            isCreated = true;
        }
    }

    public boolean isCreated() {
        return isCreated;
    }
}
