package io.project;

import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    public User login(String account_type, String login, String password){
        if(account_type.equalsIgnoreCase("client")){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select * from clients where account_type = ? and login = ? and password = ?")){
                if (checkDatabase(account_type, login, password, preparedStatement)) return null;

                return new Client(account_type, login, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(account_type.equalsIgnoreCase("bankworker")){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select * from bank_workers where account_type = ? and login = ? and password = ?")){
                if (checkDatabase(account_type, login, password, preparedStatement)) return null;

                return new BankWorker(account_type, login, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private boolean checkDatabase(String account_type, String login, String password, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, account_type);
        preparedStatement.setString(2, login);
        preparedStatement.setString(3, password);
        preparedStatement.executeQuery();

        ResultSet resultSet = preparedStatement.getResultSet();

        return !resultSet.isBeforeFirst();
    }
}
