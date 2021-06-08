package de.processes;

import de.communication.DataPacker;
import de.model.User;

import javax.websocket.Session;


import static de.constants.MessageIdentifier.LOGIN_VALID;


public class Login{

    private boolean loginValid;

    public Login() {
        loginValid=false;
    }


    public User isLoginValid(String[] strings, Session session) {
        User validUser=null;
        if (DataManager.isFile(strings[2])) {

            User user = DataManager.fileToUser(DataManager.usernameToPath(strings[2]).toFile());
            if (user!=null){
                if (strings[3].equals(user.getPassword())) {
                    loginValid = true;
                    validUser=user;
                    validUser.setuSession(session);
                }
            }
        }


        if(loginValid){
            session.getAsyncRemote().sendObject(DataPacker.packData(LOGIN_VALID,"true"));
        }
        else{
        //todo fehlt etwas
        }
        return validUser;
    }

}
