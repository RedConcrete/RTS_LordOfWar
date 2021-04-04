package de.processes;

import de.communication.DataPacker;
import de.model.User;

import javax.websocket.Session;

import static de.constants.MessageIdentifier.LOGIN_VALID;


public class Login{

    private boolean loginValid;
    DataPacker dataPacker;

    public Login() {
        dataPacker = new DataPacker();
        loginValid=false;
    }


    public void isLoginValid(String[] strings, Session session) {
        if (DataManager.isFile(strings[1])) {//username exists

            User user = DataManager.fileToUser(DataManager.usernameToPath(strings[1]).toFile());
            if (user!=null){
                loginValid=strings[2].equals(user.getPassword());
            }
        }


        if(loginValid){
            session.getAsyncRemote().sendObject(dataPacker.packData(LOGIN_VALID,"true"));
        }
        else{

        }
    }

}
