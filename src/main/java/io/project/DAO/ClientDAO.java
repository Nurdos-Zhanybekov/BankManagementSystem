package io.project.DAO;

import io.project.model.Client;
import io.project.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> getClientList() {
        String query = "SELECT * FROM clients";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching client list", e);
        }

        return clients;
    }

    public Client findClient(String clientLogin, List<String> apartments, List<Double> creditAmounts) {
        Client client = new Client();
        String clientQuery = "SELECT * FROM clients WHERE login = ?";
        String creditsQuery = "SELECT property_type, property_price_with_credit FROM credits WHERE client_login = ?";
        String balanceQuery = "SELECT balance_som, balance_dollar FROM balance WHERE client_login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement clientPS = conn.prepareStatement(clientQuery);
             PreparedStatement creditsPS = conn.prepareStatement(creditsQuery);
             PreparedStatement balancePS = conn.prepareStatement(balanceQuery)) {

            clientPS.setString(1, clientLogin);
            creditsPS.setString(1, clientLogin);
            balancePS.setString(1, clientLogin);

            try (ResultSet clientRS = clientPS.executeQuery();
                 ResultSet creditsRS = creditsPS.executeQuery();
                 ResultSet balanceRS = balancePS.executeQuery()) {

                while (creditsRS.next()) {
                    apartments.add(creditsRS.getString("property_type"));
                    creditAmounts.add(creditsRS.getDouble("property_price_with_credit"));
                }

                if(balanceRS.next()){
                    client.setBalanceSom(balanceRS.getDouble("balance_som"));
                    client.setBalanceDollar(balanceRS.getDouble("balance_dollar"));
                }

                if (clientRS.next()) {
                    client.setId(clientRS.getInt("id"));
                    client.setName(clientRS.getString("name"));
                    client.setSalarySom(clientRS.getDouble("salary_som"));
                    client.setSalaryDollar(clientRS.getDouble("salary_dollar"));
                    client.setLogin(clientRS.getString("login"));
                    return client;
                }

                clientPS.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding client", e);
        }

        return null;
    }
}
