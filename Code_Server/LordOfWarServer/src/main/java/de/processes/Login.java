package de.processes;

import de.communication.DataPacker;
import de.model.User;

import javax.websocket.Session;

import static de.constants.MessageIdentifier.LOGIN_VALID;


public class Login {

    private boolean loginValid;
    private DataManager dataManager;

    public Login(DataManager dataManager) {
        loginValid = false;
        this.dataManager = dataManager;
    }


    public User isLoginValid(String[] strings, Session session) {
        User validUser = null;
        User user = dataManager.getUser(strings[2]);
        if (user != null) {
            if (strings[3].equals(user.getPassword())) {
                loginValid = true;
                validUser = user;
                validUser.setuSession(session);
            }
        }
        if (loginValid) {
            session.getAsyncRemote().sendObject(DataPacker.packData(LOGIN_VALID, "true"));
        } else {
            //todo fehlt etwas
        }
        return validUser;
    }

}
