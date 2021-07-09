package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.LOGIN;

/**
 * The event from the
 * @author Franz Klose,Robin Hefner,Cem Arslan
 */
public class LoginScreenEvent extends Events {

    boolean loginAnswer;

    public LoginScreenEvent(LOW aGame) {
        super(aGame);
    }

    /**
     * sends user date
     * @param arr
     */
    public void sendUserData(ArrayList<String> arr) {
        webSocket.send(DataPacker.packData(LOGIN, DataPacker.stringCombiner(arr)));
    }

    public boolean isLoginAnswer() {
        return loginAnswer;
    }

    public void setLoginAnswer(String[] strings) {
        loginAnswer = strings[1].equals("true");
    }

}
