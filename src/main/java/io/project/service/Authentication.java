package io.project.service;

import io.project.config.DBConnection;
import io.project.model.BankWorker;
import io.project.model.Client;
import io.project.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    public User authenticateUser(String accountType, String login, String password){
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM users WHERE account_type = ? AND login = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountType);
            ps.setString(2, login);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("account_type");

                if (role.equalsIgnoreCase("BANKWORKER")) {
                    return new BankWorker(id, role, login, password);
                } else if (role.equalsIgnoreCase("CLIENT")) {
                    return getClientData(login);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Client getClientData(String login) {
        String sql = "SELECT * FROM clients WHERE login = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                return new Client(id, login, name);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching full client data", e);
        }
        return null;
    }


}
