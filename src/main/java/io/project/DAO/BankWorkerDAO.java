package io.project.DAO;

import io.project.Client;
import io.project.CreditCounter;
import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankWorkerDAO {
    CreditCounter creditCounter = new CreditCounter();

    public List<Client> show_client_list(){
        String list_clients = "select * from client";
        List<Client> clients = new ArrayList<>();

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(list_clients)) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            while(resultSet.next()) {
                clients.add(new Client(resultSet.getInt("id"), resultSet.getString("account_type"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getDouble("salary"), resultSet.getDouble("credit")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;
    }

    public Client find_client(String login){
        String get_client = "select * from client where name = ?";

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(get_client)) {
            preparedStatement.setString(1, login);
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()){
                return new Client(resultSet.getInt("id"), resultSet.getString("account_type"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getDouble("salary"), resultSet.getDouble("credit"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Client get_max_credit_client(){
        String get_max_credit = "select * from clients where credit = (select max(credit) from clients)";
        return get_max_or_min_credit(get_max_credit);
    }

    public Client get_min_credit_client(){
        String get_min_credit = "select * from clients where credit = (select min(credit) from clients)";
        return get_max_or_min_credit(get_min_credit);
    }

    private Client get_max_or_min_credit(String getMinCredit) {
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getMinCredit)) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next()){
                return new Client(resultSet.getInt("id"), resultSet.getString("account_type"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getDouble("salary"), resultSet.getDouble("credit"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void add_new_client(String name, double salary, String property_type, double property_price, int credit_period){
        String insert_client = "insert into client (name, salary, property_type, property_price, credit_period, credit_sum) values (?, ?, ?, ?, ?, ?)";
        double credit_sum = creditCounter.count_credit_sum(property_type, property_price, credit_period);

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(insert_client)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, salary);
            preparedStatement.setString(3, property_type);
            preparedStatement.setDouble(4, property_price);
            preparedStatement.setInt(5, credit_period);
            preparedStatement.setDouble(6, credit_sum);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
