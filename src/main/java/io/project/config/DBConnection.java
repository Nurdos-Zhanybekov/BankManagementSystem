package io.project.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/bank_management_system";
    private static final String username = "root";
    private static final String password = "root";

    public static Connection getConnection(){
        try{
            if(connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(URL, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
