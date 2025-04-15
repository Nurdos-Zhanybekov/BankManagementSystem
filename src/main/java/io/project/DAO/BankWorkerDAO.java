package io.project.DAO;

import io.project.Client;
import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankWorkerDAO {

    public List<Client> show_client_list(){
        String list_clients = "select * from clients";
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
        String get_client = "select * from clients where name = ?";

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

    public void add_new_client(String name, double salary, String property_type, double property_price, int credit_period, Double credit_sum){
        String insert_client = "insert into clients (name, salary, property_type, property_price, credit_period, credit_sum) values (?, ?, ?, ?, ?, ?)";

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

    public void show_credit_history(String name){
        String get_client_credit_history = "select id, name, property_type, credit_sum from clients where name = ?";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(get_client_credit_history)) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean client_found = false;
            double total_credit = 0;
            while(resultSet.next()){
                if(!client_found){
                    System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("name"));
                    System.out.print("Credit: ");
                    client_found = true;
                }

                System.out.print(resultSet.getString("property_type") + "(" + resultSet.getDouble("credit_sum") + "), ");
                total_credit += resultSet.getDouble("credit_sum");
            }

            System.out.println("Total credit sum: " + total_credit);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
