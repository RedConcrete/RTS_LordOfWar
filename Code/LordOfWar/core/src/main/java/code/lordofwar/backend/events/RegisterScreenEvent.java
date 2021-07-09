package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.main.LOW;
import okhttp3.WebSocket;

import java.util.ArrayList;

import static code.lordofwar.backend.MessageIdentifier.REGISTER;

/**
 * The event from the
 * @author Franz Klose,Robin Hefner,Cem Arslan
 */
public class RegisterScreenEvent extends Events{

    boolean registerAnswer;

    public RegisterScreenEvent(LOW aGame) {
        super(aGame);

    }

/*
    isRegisterValid checks if the Registration is Valid and creates a new Account.
    @author Robin Hefner
 */
    /**
     * sends the User data
     */
    public void sendUserData(ArrayList<String> arr) {
        webSocket.send(DataPacker.packData(REGISTER, DataPacker.stringCombiner(arr)));
    }

    public boolean isRegisterAnswer() {
        return registerAnswer;
    }

    public void setRegisterAnswer(String[] strings) {
        registerAnswer = strings[1].equals("true");
    }
}
