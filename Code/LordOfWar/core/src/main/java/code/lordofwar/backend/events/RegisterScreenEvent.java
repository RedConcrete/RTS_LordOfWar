package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.REGISTER;

public class RegisterScreenEvent {
    WebSocket webSocket;
    LOW game;

    boolean registerAnswer;

    public RegisterScreenEvent(LOW aGame) {
        game = aGame;
        webSocket = aGame.getWebSocket();

    }

/*
    isRegisterValid checks if the Registration is Valid and creates a new Account.
    @author Robin Hefner
 */

    public void sendUserData(ArrayList<String> arr) {
        webSocket.send(DataPacker.packData(REGISTER, DataPacker.stringCombiner(arr)));
    }

    public boolean isRegisterAnswer() {
        return registerAnswer;
    }

    public void setRegisterAnswer(String[] strings) {
        System.out.println(strings[1]);
        registerAnswer = strings[1].equals("true");
    }
}
