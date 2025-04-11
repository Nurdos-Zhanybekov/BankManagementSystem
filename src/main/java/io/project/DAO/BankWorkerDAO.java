package io.project.DAO;

import io.project.BankWorker;
import io.project.Client;
import io.project.config.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankWorkerDAO {
    private static final String list_clients = "select id, login from clients";
    private static final List<Client> clients = new ArrayList<>();

    public List<Client> show_client_list(){
        try(PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(list_clients)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                clients.add(new Client(resultSet.getInt("id"), resultSet.getString("login")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;
    }
}
