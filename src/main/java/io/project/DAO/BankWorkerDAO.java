package io.project.DAO;

import io.project.Client;
import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankWorkerDAO {
    private static final String list_clients = "select * from clients";
    private static final String get_client = "select * from clients where login = ?";
    private static final String get_max_credit = "select * from clients where credit = (select max(credit) from clients)";
    private static final String get_min_credit = "select * from clients where credit = (select min(credit) from clients)";
    private static final String insert_client = "insert into credit_clients (name, salary, property_type, property_price, credit_period) values (?, ?, ?, ?, ?)";
    private static final List<Client> clients = new ArrayList<>();

    public List<Client> show_client_list(){
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
        return get_max_or_min_credit(get_max_credit);
    }

    public Client get_min_credit_client(){
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
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(insert_client)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, salary);
            preparedStatement.setString(3, property_type);
            preparedStatement.setDouble(4, property_price);
            preparedStatement.setInt(5, credit_period);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
