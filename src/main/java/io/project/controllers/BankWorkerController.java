package io.project.controllers;

import io.project.Authentication;
import io.project.BankWorker;
import io.project.Client;
import io.project.DAO.BankWorkerDAO;
import io.project.User;

import java.util.List;
import java.util.Scanner;

public class BankWorkerController {
    private final BankWorkerDAO bankWorkerDAO = new BankWorkerDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu(User user){
        System.out.println("Welcome " + user.getLogin());
        System.out.println("Please choose command: ");

        System.out.println("1. Show clients list");
        System.out.println("2. Find client");
        System.out.println("3. Show client with maximum credit");
        System.out.println("4. Show client with minimum credit");
        System.out.println("5. Add client");
        System.out.println("6. Show credit history");
        System.out.println("7. Exit");

        int choose = scanner.nextInt();
        switch (choose){
            case 1:
                List<Client> clients = bankWorkerDAO.show_client_list();
                for(Client client : clients){
                    System.out.println(client.getId() + ". " + client.getLogin());
                }
                break;
            case 2:
                System.out.println("bla");
                break;
        }
    }
}
