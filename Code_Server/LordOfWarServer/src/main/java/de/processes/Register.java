package de.processes;

import de.communication.DataPacker;
import de.model.User;

import javax.transaction.Transactional;
import javax.websocket.Session;

import static de.constants.MessageIdentifier.REGISTER_VALID;


public class Register {


    private boolean registerValid;
    DataPacker dataPacker;


    public Register() {
        dataPacker = new DataPacker();
        registerValid = false;
    }

    @Transactional
    public void isRegisterValid(String[] strings, Session session) {
        if (!DataManager.isFile(strings[1])) {//username doesnt exist
            Integer id = null;
            while (id == null) {
                id = DataManager.getNextID();//try until
            }
            User user = new User(strings[1], strings[2], 0, id, null);
            registerValid= DataManager.userToFile(user);//successful creation
        }


        if (registerValid) {
            session.getAsyncRemote().sendObject(dataPacker.packData(REGISTER_VALID, "true"));
        } else {

        }
    }

}
