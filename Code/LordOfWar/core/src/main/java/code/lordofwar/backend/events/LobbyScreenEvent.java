package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.CREATE_LOBBY;
import static code.lordofwar.backend.MessageIdentifier.LEAVE_LOBBY;

public class LobbyScreenEvent {
    WebSocket webSocket;
    DataPacker dataPacker;
    LOW game;
    public LobbyScreenEvent(LOW aGame){
        game = aGame;
        dataPacker = new DataPacker();
        webSocket = aGame.getWebSocket();
    }

    public void sendLeaveLobbyNotice(String lobbyName){
        ArrayList<String> leaveData=new ArrayList<>();
        leaveData.add(game.getSessionID());
        leaveData.add(lobbyName);
        webSocket.send(dataPacker.packData(LEAVE_LOBBY,dataPacker.stringCombiner(leaveData)));
    }


}
