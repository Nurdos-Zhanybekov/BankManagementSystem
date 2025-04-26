package io.project.DAO;

import io.project.model.Client;
import io.project.config.DBConnection;
import io.project.model.Credit;

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
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                clients.add(createClient(id, login, name, salary));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;
    }

    public Client findClient(String clientLogin, List<String> apartments, List<Double> creditAmounts) {
        String getClient = "SELECT * FROM clients WHERE login = ?";
        String getCredits = "select property_type, property_price_with_credit from credits inner join clients on client_login = login where client_login = ?";

        try (PreparedStatement getClientDB = DBConnection.getConnection().prepareStatement(getClient);
             PreparedStatement getCreditsDB = DBConnection.getConnection().prepareStatement(getCredits)) {

            getClientDB.setString(1, clientLogin);
            getCreditsDB.setString(1, clientLogin);

            ResultSet clientInfo = getClientDB.executeQuery();
            ResultSet creditInfo = getCreditsDB.executeQuery();

            int id;
            String name;
            String login;
            double salary;
            String propertyType;
            double creditPrice;

            while (creditInfo.next()) {
                propertyType = creditInfo.getString("property_type");
                creditPrice = creditInfo.getDouble("property_price_with_credit");
                apartments.add(propertyType);
                creditAmounts.add(creditPrice);
            }

            if (clientInfo.next()) {
                id = clientInfo.getInt("id");
                login = clientInfo.getString("login");
                name = clientInfo.getString("name");
                salary = clientInfo.getDouble("salary");
                return createClient(id, login, name, salary);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Client getMaxCreditClient(){
        String getMaxCreditClient = "select * from clients inner join credits on login = client_login where property_price_with_credit = (select max(property_price_with_credit) from credits)";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getMaxCreditClient);) {

            ResultSet resultSet = preparedStatement.executeQuery();

            int id = 0;
            String login = "";
            String name = "";
            double salary = 0;

            if(resultSet.next()){
                id = resultSet.getInt("id");
                login = resultSet.getString("login");
                name = resultSet.getString("name");
                salary = resultSet.getDouble("salary");
            }

            return createClient(id, login, name, salary);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Credit getMaxCredit(){
        String getMaxCredit = "select * from credits where property_price_with_credit = (select max(property_price_with_credit) from credits)";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getMaxCredit)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String propertyType = resultSet.getString("property_type");
                double propertyPrice = resultSet.getDouble("property_price");
                int creditPeriod = resultSet.getInt("credit_period");
                double propertyPriceWithCredit = resultSet.getDouble("property_price_with_credit");
                return createCredit(propertyType, propertyPrice, creditPeriod, propertyPriceWithCredit);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Client getMinCreditClient(){
        String getMinCreditClient = "select * from clients inner join credits on login = client_login where property_price_with_credit = (select min(property_price_with_credit) from credits)";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getMinCreditClient);) {

            ResultSet resultSet = preparedStatement.executeQuery();

            int id = 0;
            String login = "";
            String name = "";
            double salary = 0;

            if(resultSet.next()){
                id = resultSet.getInt("id");
                login = resultSet.getString("login");
                name = resultSet.getString("name");
                salary = resultSet.getDouble("salary");
            }

            return createClient(id, login, name, salary);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Credit getMinCredit(){
        String getMinCredit = "select * from credits where property_price_with_credit = (select min(property_price_with_credit) from credits)";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getMinCredit)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String propertyType = resultSet.getString("property_type");
                double propertyPrice = resultSet.getDouble("property_price");
                int creditPeriod = resultSet.getInt("credit_period");
                double propertyPriceWithCredit = resultSet.getDouble("property_price_with_credit");

                return createCredit(propertyType, propertyPrice, creditPeriod, propertyPriceWithCredit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void addNewClient(String name, String login, double salary){
        String insertClient = "insert into clients (name, salary, login) values (?, ?, ?)";

        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(insertClient)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, salary);
            preparedStatement.setString(3, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewClientCredit(String clientLogin, String propertyType, double propertyPrice, int creditPeriod, Double totalCredit){
        String insertCredit = "insert into credits (client_login, property_type, property_price, credit_period, property_price_with_credit) values (?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(insertCredit)) {
            preparedStatement.setString(1, clientLogin);
            preparedStatement.setString(2, propertyType);
            preparedStatement.setDouble(3, propertyPrice);
            preparedStatement.setInt(4, creditPeriod);
            preparedStatement.setDouble(5, totalCredit);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Client getCreditHistoryClient(String login){
        String getClientCreditHistory = "select * from clients where login = ?";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getClientCreditHistory)) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int clientID = resultSet.getInt("id");
                String clientLogin = resultSet.getString("login");
                String clientName = resultSet.getString("name");
                double clientSalary = resultSet.getDouble("salary");
                return createClient(clientID, clientLogin, clientName, clientSalary);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Credit getCreditHistory(String login, List<String> propertyTypes, List<Double> propertyPrices){
        String getCredit = "select * from credits inner join clients on client_login = login where client_login = ?";
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getCredit)){
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            String propertyType = "";
            double propertyPrice = 0;
            int creditPeriod = 0;
            double propertyPriceWithCredit = 0;
            double totalCredit = 0;

            while(resultSet.next()){
                propertyType = resultSet.getString("property_type");
                propertyPrice = resultSet.getDouble("property_price");
                creditPeriod = resultSet.getInt("credit_period");
                propertyPriceWithCredit = resultSet.getDouble("property_price_with_credit");
                totalCredit += propertyPriceWithCredit;
                propertyTypes.add(propertyType);
                propertyPrices.add(propertyPriceWithCredit);
            }

            return new Credit(propertyType, propertyPrice, creditPeriod, propertyPriceWithCredit, totalCredit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            }
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

    public void buyCurrency(int currency, double currencyAmount, String login){
        int som = 1;
        int dollar = 2;
        String updateBalance;

        if(currency == som){
            updateBalance = "update balance set balance_som = balance_som + ?, balance_dollar = balance_dollar - ? where client_login = ?";
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(updateBalance)) {
                preparedStatement.setDouble(1, currencyAmount * 87);
                preparedStatement.setDouble(2, currencyAmount);
                preparedStatement.setString(3, login);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(currency == dollar){
            updateBalance = "update balance set balance_som = balance_som - ?, balance_dollar = balance_dollar + ? where client_login = ?";
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(updateBalance)) {
                preparedStatement.setDouble(1, currencyAmount);
                preparedStatement.setDouble(2, currencyAmount / 87);
                preparedStatement.setString(3, login);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void transferCurrency(int currency, double currencyAmount, String sender, String receiver){
        int som = 1;
        int dollar = 2;
        String updateSenderBalance = "update balance set balance_som = balance_som - ?, balance_dollar = balance_dollar - ? where client_login = ?";
        String updateReceiverBalance = "update balance set balance_som = balance_som + ?, balance_dollar = balance_dollar + ? where client_login = ?";

        if(currency == som){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(updateSenderBalance);
            PreparedStatement preparedStatement1 = DBConnection.getConnection().prepareStatement(updateReceiverBalance)){
                preparedStatement.setDouble(1, currencyAmount);
                preparedStatement.setDouble(2, currencyAmount / 87);
                preparedStatement.setString(3, sender);

                preparedStatement1.setDouble(1, currencyAmount / 87);
                preparedStatement1.setDouble(2, currencyAmount);
                preparedStatement1.setString(3, receiver);

                preparedStatement.executeUpdate();
                preparedStatement1.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(currency == dollar){
            try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(updateSenderBalance);
            PreparedStatement preparedStatement1 = DBConnection.getConnection().prepareStatement(updateReceiverBalance)) {
                preparedStatement.setDouble(1, currencyAmount * 87);
                preparedStatement.setDouble(2, currencyAmount);
                preparedStatement.setString(3, sender);

                preparedStatement1.setDouble(1, currencyAmount * 87);
                preparedStatement1.setDouble(2, currencyAmount);
                preparedStatement1.setString(3, receiver);

                preparedStatement.executeUpdate();
                preparedStatement1.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Client createClient(int id, String login, String name, double salary){
        Client client = new Client();
        client.setId(id);
        client.setLogin(login);
        client.setName(name);
        client.setSalary(salary);

        return client;
    }

    public Credit createCredit(String propertyType, double propertyPrice, int creditPeriod, double propertyPriceWithCredit){
        Credit credit = new Credit();

        credit.setPropertyType(propertyType);
        credit.setPropertyPrice(propertyPrice);
        credit.setCreditPeriod(creditPeriod);
        credit.setPropertyPriceWithCredit(propertyPriceWithCredit);

        return credit;
    }
}
