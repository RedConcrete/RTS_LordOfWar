package de.main;

import javax.websocket.Session;
import java.util.Map;

import static de.main.MessageIdentifier.LOGIN_VALID;

public class Login extends LowServer{

    private boolean loginValdi = true;
    DataPacker dataPacker;


    public Login() {
        dataPacker = new DataPacker();
    }

    public void isLoginValid(String[] strings, Session session) {


        //todo überprüfung der angebenen Daten!


        if(loginValdi){

            session.getAsyncRemote().sendObject(dataPacker.packData(LOGIN_VALID,"true"));
        }
        else{

        }
    }

}
