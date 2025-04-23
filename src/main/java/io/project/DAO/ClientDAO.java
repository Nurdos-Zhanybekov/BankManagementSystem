package io.project.DAO;

import io.project.model.Client;
import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> getClientList(){
        String listClients = "select * from clients";
        List<Client> clients = new ArrayList<>();

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(listClients)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                clients.add(new Client(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("salary"), resultSet.getString("property_type"),
                        resultSet.getDouble("property_price"), resultSet.getInt("credit_period"), resultSet.getDouble("credit_amount"), resultSet.getDouble("balance")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;
    }

    public Client findClient(String login, List<String> apartments, List<Double> creditAmounts) {
        String getClient = "SELECT * FROM clients WHERE name = ?";

        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getClient)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            int id = 0;
            String clientName = "";
            String propertyType = "";
            double creditAmount = 0;
            double salary = 0;
            double propertyPrice = 0;
            int creditPeriod = 0;
            double balance = 0;
            boolean found = false;

            while (resultSet.next()) {
                if (!found) {
                    id = resultSet.getInt("id");
                    clientName = resultSet.getString("name");
                    propertyType = resultSet.getString("property_type");
                    creditAmount = resultSet.getDouble("credit_amount");
                    salary = resultSet.getDouble("salary");
                    propertyPrice = resultSet.getDouble("property_price");
                    creditPeriod = resultSet.getInt("credit_period");
                    balance = resultSet.getDouble("balance");
                    found = true;
                }

                apartments.add(resultSet.getString("property_type"));
                creditAmounts.add(resultSet.getDouble("credit_amount"));
            }

            if (found) {
                return new Client(id, clientName, salary, propertyType, propertyPrice, creditPeriod, creditAmount, balance);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public Client getMaxCreditClient(){
        String getMaxCredit = "select * from clients where credit_amount = (select max(credit_amount) from clients)";
        return getMaxOrMinCredit(getMaxCredit);
    }

    public Client getMinCreditClient(){
        String getMinCredit = "select * from clients where credit_amount = (select min(credit_amount) from clients)";
        return getMaxOrMinCredit(getMinCredit);
    }

    private Client getMaxOrMinCredit(String getCredit) {
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getCredit)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return new Client(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("salary"), resultSet.getString("property_type"),
                        resultSet.getDouble("property_price"), resultSet.getInt("credit_period"), resultSet.getDouble("credit_amount"), resultSet.getDouble("balance"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void addNewClient(String name, double salary, String propertyType, double propertyPrice, int creditPeriod, Double creditSum){
        String insertClient = "insert into clients (name, salary, property_type, property_price, credit_period, credit_amount) values (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(insertClient)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, salary);
            preparedStatement.setString(3, propertyType);
            preparedStatement.setDouble(4, propertyPrice);
            preparedStatement.setInt(5, creditPeriod);
            preparedStatement.setDouble(6, creditSum);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Client getCreditHistory(String name, List<String> apartments, List<Double> creditAmounts){
        String getClientCreditHistory = "select * from clients where name = ?";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getClientCreditHistory)) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean found = false;

            int id = 0;
            String clientName = "";
            String propertyType = "";
            double creditAmount = 0;
            double salary = 0;
            double propertyPrice = 0;
            int creditPeriod = 0;
            double balance = 0;
            double totalCredit = 0;

            while(resultSet.next()){
                if(!found){
                    id = resultSet.getInt("id");
                    clientName = resultSet.getString("name");
                    salary = resultSet.getDouble("salary");
                    propertyType = resultSet.getString("property_type");
                    propertyPrice = resultSet.getDouble("property_price");
                    creditPeriod = resultSet.getInt("credit_period");
                    creditAmount = resultSet.getDouble("credit_amount");
                    balance = resultSet.getDouble("balance");
                    found = true;
                }

                totalCredit += resultSet.getDouble("credit_amount");
                apartments.add(resultSet.getString("property_type"));
                creditAmounts.add(resultSet.getDouble("credit_amount"));
            }

            if(found){
                return new Client(id, clientName, salary, propertyType, propertyPrice, creditPeriod, creditAmount, balance, totalCredit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Double getBalance(int currency, String login){
        int som = 1;
        int dollar = 2;
        String getBalanceSom = "select balance_som from balance inner join clients on client_login = name where client_login = ?";
        String getBalanceDollar = "select balance_dollar from balance inner join clients on client_login = name where client_login = ?";
        if(currency == som){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getBalanceSom)) {
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    return resultSet.getDouble("balance_som");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } ;
        }else if (currency == dollar){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getBalanceDollar)) {
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    return resultSet.getDouble("balance_dollar");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public void buyCurrency(int currency, double sum, String login){
        int som = 1;
        int dollar = 2;
        String updateSomBalance = "update balance set balance_som = ? where client_login = ?";
        String updateDollarBalance = "update balance set balance_dollar = ? where client_login = ?";

        if(currency == som){

            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(updateSomBalance);
            PreparedStatement preparedStatement1 = DBConnection.getConnection().prepareStatement(updateDollarBalance)) {
                preparedStatement.setDouble(1, sum);
                preparedStatement.addBatch(updateDollarBalance);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(currency == dollar){

        }


    }
}
