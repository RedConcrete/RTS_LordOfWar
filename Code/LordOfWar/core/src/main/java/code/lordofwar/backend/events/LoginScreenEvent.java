package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.LOGIN;

/*
        isLoginValid checks if the Login is valid and logs the User in.
        @author Robin Hefner
     */

public class LoginScreenEvent {

    WebSocket webSocket;
    DataPacker dataPacker = new DataPacker();
    LOW game;

    boolean loginAnswer;



    public LoginScreenEvent(LOW aGame) {
        this.game = aGame;
        webSocket = aGame.getWebSocket();
    }


    //todo methode schreiben zum senden der Login daten

    public void sendUserData(ArrayList<String> arr) {
        webSocket.send(dataPacker.packData(LOGIN, dataPacker.stringCombiner(arr)));
    }

    public boolean isLoginAnswer() {
        return loginAnswer;
    }

    public void setLoginAnswer(String[] strings) {
        System.out.println(strings[1]);
        loginAnswer = strings[1].equals("true");
    }

}