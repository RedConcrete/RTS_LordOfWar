package de.processes.db;

import de.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStatementManager {

    public static void main(String[] args) throws Exception {
        //TODO config db
        DBCreator.createDB();
        //DBStatementManager db=new DBStatementManager();
        //db.createUser("AlphA","TesT");
       // db.updateUserScore(10,"AlphA");
       // System.out.println(db.getUser("AlphA"));
       // db.deleteUser("AlphA");
        //System.out.println(db.getUser("AlphA"));
    }

    //TODO test these
    private Connection connection;
    //pretty damn sure were not gonna need more
    private final PreparedStatement getUser;
    private final PreparedStatement deleteUser;
    private final PreparedStatement updateUserScore;
    private final PreparedStatement createUser;

    public DBStatementManager() throws SQLException {
        try {
            this.connection = DBCreator.connector(DBCreator.dbURL, DBCreator.uName, DBCreator.password);
        } catch (SQLException e) {
            throw new SQLException("Connection Failure");//TODO better error handling/returning
        }
        try {//removed public from dbs
            getUser = connection.prepareStatement("SELECT * FROM player WHERE username=?");//get user by name
            deleteUser = connection.prepareStatement("DELETE FROM player WHERE username=?");//delete user by name
            updateUserScore = connection.prepareStatement("UPDATE player SET score=? WHERE username=?");//set userscore
            createUser = connection.prepareStatement("INSERT INTO player(username,password,score,userid) Values(?,?,0,nextval('idGenerator')) RETURNING userid");
        } catch (SQLException e) {
            throw new SQLException("Statement Failure");//TODO better error handling/returning
        }
    }

    public User getUser(String username) {
        User user = null;
        try {
            getUser.setString(1, username);
            ResultSet rs = getUser.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    user = new User(rs.getString("username"), rs.getString("password"), rs.getInt("score"), rs.getInt("userid"), null);
                }
            }
        } catch (SQLException e) {
            System.err.println("getuser fail");
        }
        return user;
    }

    //true means successfull
    public boolean updateUserScore(int score, String username) {
        boolean update = false;
        try {
            updateUserScore.setInt(1, score);
            updateUserScore.setString(2, username);
            update = updateUserScore.executeUpdate() != 0;//if row was affected
        } catch (SQLException e) {
            System.err.println("updateuserscore fail");
        }
        return update;
    }

    public boolean deleteUser(String username) {
        boolean update = false;
        try {
            deleteUser.setString(1, username);
            update = deleteUser.executeUpdate() != 0;//if row was affected
        } catch (SQLException e) {
            System.err.println("deleteuser fail");
        }
        return update;
    }

    public User createUser(String username,String password) {
        User user=null;
        try {
            createUser.setString(1,username);
            createUser.setString(2,password);
            createUser.executeQuery();
        }catch (SQLException e){
            System.err.println("createUser fail");
        }
        return getUser(username);//check if user inserted properly & return
    }


}
