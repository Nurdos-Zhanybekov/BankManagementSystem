package io.project;

import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    public User login(String account_type, String login, String password){
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select * from bank_workers where account_type = ? and login = ? and password = ?")){
            preparedStatement.setString(1, account_type);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                System.out.println(resultSet.getString("id"));
                System.out.println(resultSet.getString("account_type"));
                System.out.println(resultSet.getString("login"));
                System.out.println(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Client(account_type, login, password);
    }
}
