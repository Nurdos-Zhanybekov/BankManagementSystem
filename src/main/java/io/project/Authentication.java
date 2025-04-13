package io.project;

import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    public User login(String account_type, String login, String password){
        if(account_type.equalsIgnoreCase("client")){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select * from users where account_type = ? and login = ? and password = ?")){
                preparedStatement.setString(1, account_type);
                preparedStatement.setString(2, login);
                preparedStatement.setString(3, password);
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();

                return new Client(resultSet.getInt("id"), account_type, login, password, resultSet.getDouble("salary"), resultSet.getDouble("credit"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(account_type.equalsIgnoreCase("bankWorker")){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select * from users where account_type = ? and login = ? and password = ?")){
                preparedStatement.setString(1, account_type);
                preparedStatement.setString(2, login);
                preparedStatement.setString(3, password);
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();

                if(resultSet.next()) {
                    return new BankWorker(resultSet.getInt("id"), account_type, login, password);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
