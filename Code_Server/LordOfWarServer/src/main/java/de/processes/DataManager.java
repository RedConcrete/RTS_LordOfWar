package de.processes;


import de.model.User;
import de.processes.db.DBStatementManager;

import java.sql.SQLException;

//management class to buffer errors still have to do proper exceptionhandling

/**
 * Management class for selecting appropriate query.
 */
public class DataManager {
    private DBStatementManager dbManager;

    public DataManager() {
        try {
            dbManager = new DBStatementManager();
        } catch (SQLException e) {
            e.printStackTrace();//TODO Better errorhandling
        }
    }

    /**
     * Attempt to register user.
     * @param username given username
     * @param password given password
     * @return the given user if registration successful {@code null} otherwise
     */
    public User registerUser(String username, String password) {
        return dbManager.createUser(username, password);

    }

    /**
     * Attempt to delete user.
     * @param username given username
     * @return {@code true} if successful
     */
    public boolean deleteUser(String username) {
        return dbManager.deleteUser(username);
    }

    /**
     * Attempt to retrieve user.
     * @param username given username
     * @return the requested user as a {@link User} object.
     */
    public User getUser(String username) {
        return dbManager.getUser(username);
    }

    /**
     * Attempt to change score of user
     * @param score new score
     * @param username username of given player
     * @return {@code true} if successful
     */
    public boolean updateUserScore(int score, String username) {
        return dbManager.updateUserScore(score, username);
    }
}
