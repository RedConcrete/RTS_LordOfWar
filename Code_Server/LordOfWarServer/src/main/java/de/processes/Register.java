package de.processes;

import de.communication.DataPacker;
import de.model.User;

import javax.transaction.Transactional;
import javax.websocket.Session;

import static de.constants.MessageIdentifier.REGISTER_VALID;


public class Register {


    private boolean registerValid;
    private DataManager dataManager;

    public Register(DataManager dataManager) {
        registerValid = false;
        this.dataManager = dataManager;
    }

    @Transactional
    public void isRegisterValid(String[] strings, Session session) {
        if (dataManager.getUser(strings[2]) == null) {//username does not exist
            //TODO length check here
            //TODO return actual error messages
            User user = dataManager.registerUser(strings[2], strings[3]);
            registerValid = user != null;
        }


        if (registerValid) {
            session.getAsyncRemote().sendObject(DataPacker.packData(REGISTER_VALID, "true"));
        } else {
            //TODO Reason?
        }
    }

}
