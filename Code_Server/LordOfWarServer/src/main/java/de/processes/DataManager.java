package de.processes;


import de.constants.Constants;
import de.model.User;
import de.processes.db.DBStatementManager;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;

//management class to buffer errors still have to do proper exceptionhandling
public class DataManager {
    private DBStatementManager dbManager;

    public DataManager() {
        try {
            dbManager = new DBStatementManager();
        } catch (SQLException e) {
            e.printStackTrace();//TODO Better errorhandling
        }
    }

    public User registerUser(String username, String password) {
        return dbManager.createUser(username, password);

    }
    public boolean deleteUser(String username){
        return dbManager.deleteUser(username);
    }
    public User getUser(String username){
        return dbManager.getUser(username);
    }

    public boolean updateUserScore(int score, String username) {
        return dbManager.updateUserScore(score, username);
    }
}
