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
        if (!DataManager.isFile(strings[2])) {
            Integer id = null;
            while (id == null) {
                id = DataManager.getNextID();
            }
            User user = new User(strings[2], strings[3], 0, id, null);
            registerValid= DataManager.userToFile(user);
        }


        if (registerValid) {
            session.getAsyncRemote().sendObject(dataPacker.packData(REGISTER_VALID, "true"));
        } else {

        }
    }

}
