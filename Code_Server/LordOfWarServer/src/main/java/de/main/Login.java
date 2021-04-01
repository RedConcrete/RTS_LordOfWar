package de.main;

import javax.websocket.Session;

import static de.main.MessageIdentifier.LOGIN_VALID;


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

        //todo überprüfung der angebenen Daten!
        if(loginValid){
            session.getAsyncRemote().sendObject(dataPacker.packData(LOGIN_VALID,"true"));
        }
        else{

        }
    }

}
