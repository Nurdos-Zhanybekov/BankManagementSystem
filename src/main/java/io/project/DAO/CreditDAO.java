package io.project.DAO;

import io.project.config.DBConnection;
import io.project.model.Client;
import io.project.model.Credit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CreditDAO {

    public Client getMaxCreditClient() {
        return getClientWithExtremeCredit("MAX");
    }

    public Client getMinCreditClient() {
        return getClientWithExtremeCredit("MIN");
    }

    private Client getClientWithExtremeCredit(String extremeType) {
        String query = "SELECT c.* FROM clients c " +
                "INNER JOIN credits cr ON c.login = cr.client_login " +
                "WHERE cr.property_price_with_credit = (SELECT " + extremeType + "(property_price_with_credit) FROM credits)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching client with " + extremeType + " credit", e);
        }

        return null;
    }

    public Credit getMaxCredit() {
        return getCreditWithExtremeValue("MAX");
    }

    public Credit getMinCredit() {
        return getCreditWithExtremeValue("MIN");
    }

    private Credit getCreditWithExtremeValue(String extremeType) {
        String query = "SELECT * FROM credits " +
                "WHERE property_price_with_credit = (SELECT " + extremeType + "(property_price_with_credit) FROM credits)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Credit(
                        rs.getString("property_type"),
                        rs.getDouble("property_price"),
                        rs.getInt("credit_period"),
                        rs.getDouble("property_price_with_credit")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching " + extremeType + " credit", e);
        }

        return null;
    }

    public void addNewClient(String name, String login, double salary) {
        String query = "INSERT INTO clients (name, salary_som, login) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setDouble(2, salary);
            ps.setString(3, login);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new client", e);
        }
    }

    public void addNewUser(String accountType, String login, String password){
        String query = "INSERT INTO users (account_type, login, password) VALUES (?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, accountType);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewClientCredit(String clientLogin, String propertyType, double propertyPrice, int creditPeriod, double totalCredit) {
        String query = "INSERT INTO credits (client_login, property_type, property_price, credit_period, property_price_with_credit) VALUES (?, ?, ?, ?, ?)";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clientLogin);
            ps.setString(2, propertyType);
            ps.setDouble(3, propertyPrice);
            ps.setInt(4, creditPeriod);
            ps.setDouble(5, totalCredit);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new credit", e);
        }
    }

    public Client getCreditHistoryClient(String login) {
        String query = "SELECT * FROM clients WHERE login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("login"),
                            rs.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching credit history client", e);
        }

        return null;
    }

    public Credit getCreditHistory(String login, List<String> propertyTypes, List<Double> propertyPrices) {
        String query = "SELECT * FROM credits WHERE client_login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                double totalCredit = 0;
                String propertyType = "";
                double propertyPrice = 0;
                int creditPeriod = 0;
                double propertyPriceWithCredit = 0;

                while (rs.next()) {
                    propertyType = rs.getString("property_type");
                    propertyPrice = rs.getDouble("property_price");
                    creditPeriod = rs.getInt("credit_period");
                    propertyPriceWithCredit = rs.getDouble("property_price_with_credit");

                    propertyTypes.add(propertyType);
                    propertyPrices.add(propertyPriceWithCredit);
                    totalCredit += propertyPriceWithCredit;
                }

                return new Credit(propertyType, propertyPrice, creditPeriod, propertyPriceWithCredit, totalCredit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching credit history", e);
        }
    }
}
