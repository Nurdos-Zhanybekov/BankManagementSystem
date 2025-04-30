package io.project.DAO;

import io.project.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceDAO {
    private static final int SOM = 1;

    public Double getBalance(int currency, String login) {
        String query;
        if(currency == SOM){
            query = "SELECT balance_som FROM balance WHERE client_login = ?";
        }else{
            query = "SELECT balance_dollar FROM balance WHERE client_login = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                if (currency == SOM) {
                    return rs.getDouble("balance_som");
                } else {
                    return rs.getDouble("balance_dollar");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching balance", e);
        }

        return null;
    }

    public void buyCurrency(int currency, double amount, String login) {
        String query = currency == SOM ?
                "UPDATE balance SET balance_som = COALESCE(balance_som, 0) + ? WHERE client_login = ?" :
                "UPDATE balance SET balance_dollar = COALESCE(balance_dollar, 0) + ? WHERE client_login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDouble(1, amount);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error buying currency", e);
        }
    }

    public void transferCurrency(int currency, double amount, String sender, String receiver) {
        String balanceQuery;
        String senderQuery;
        String receiverQuery;

        if (currency == SOM) {
            balanceQuery = "SELECT balance_som FROM balance WHERE client_login = ?";
            senderQuery = "UPDATE balance SET balance_som = balance_som - ? WHERE client_login = ?";
            receiverQuery = "UPDATE balance SET balance_som = balance_som + ? WHERE client_login = ?";
        } else {
            balanceQuery = "SELECT balance_dollar FROM balance WHERE client_login = ?";
            senderQuery = "UPDATE balance SET balance_dollar = balance_dollar - ? WHERE client_login = ?";
            receiverQuery = "UPDATE balance SET balance_dollar = balance_dollar + ? WHERE client_login = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement balancePS = conn.prepareStatement(balanceQuery);
             PreparedStatement senderPS = conn.prepareStatement(senderQuery);
             PreparedStatement receiverPS = conn.prepareStatement(receiverQuery)) {

            balancePS.setString(1, sender);
            ResultSet rs = balancePS.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble(1);

                if (amount > currentBalance) {
                    System.out.println("Transfer failed: insufficient funds.");
                    return;
                }
            } else {
                System.out.println("Sender not found.");
                return;
            }

            senderPS.setDouble(1, amount);
            senderPS.setString(2, sender);

            receiverPS.setDouble(1, amount);
            receiverPS.setString(2, receiver);

            senderPS.executeUpdate();
            receiverPS.executeUpdate();

            System.out.println("Transfer successful.");

        } catch (SQLException e) {
            throw new RuntimeException("Error transferring currency", e);
        }
    }


    public void addNewClientBalance(String login, int currency, double balanceAmount){
        String query;
        if(currency == SOM){
            query = "INSERT INTO balance (client_login, balance_som) values (?, ?)";
        }else{
            query = "INSERT INTO balance (client_login, balance_dollar) values (?, ?)";
        }

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, login);
            ps.setDouble(2, balanceAmount);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new client balance", e);
        }
    }
}
