package de.processes.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCreator {
    //TODO get adress etc
    public static final String dbURL = "jdbc:postgresql://localhost:5432/game";
    public static final String uName = "postgres";
    public static final String password = "admin123";

    /*
        private static final String DB_CREATION_DDL_SCRIPT =
                "CREATE SCHEMA public;\n" +
                        "CREATE TABLE Player(\n" +
                        " UserID INT8 NOT NULL,\n" +
                        " UserName VARCHAR(50) NOT NULL,\n" +
                        " PassWord VARCHAR(50) NOT NULL,\n" +
                        " Score INT8,\n" +
                        " primary key (UserID)\n" +
                        ");\n" +
                        //creating id incrementer
                        "CREATE SEQUENCE idGenerator\n" +
                        "INCREMENT 1\n" +
                        "MINVALUE 1\n" +
                        "START 1;\n"
                        //creating indexes
                        + "CREATE INDEX userLoginIndex On Player(UserName,PassWord);\n"
                        + "CREATE INDEX userGetIndex On Player USING hash(UserName);\n";

        public static void createDB() throws ClassNotFoundException, SQLException{
            Class.forName(org.hsqldb.jdbcDriver.class.getName());//TODO insert proper drivers into project
            Connection connection = connector(dbURL, uName, password);
            Statement statement = connection.createStatement();
            try {
                statement.executeUpdate(DB_CREATION_DDL_SCRIPT);
            } catch (PSQLException e) {
                System.err.println("Schema »public« is already defined dropping schema and retrying...");
                System.err.println(e.getMessage());
                statement.executeUpdate("DROP SCHEMA public CASCADE;");
                statement.executeUpdate(DB_CREATION_DDL_SCRIPT);
            }
        }
    */
    public static Connection connector(String dbURL, String userName, String userPassword) throws SQLException {
        return DriverManager.getConnection(dbURL, userName, userPassword);
    }

}
