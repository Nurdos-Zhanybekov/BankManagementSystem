package io.project.controllers;

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
        scanner.nextLine();
        switch (choose){
            case 1:
                List<Client> clients = bankWorkerDAO.show_client_list();
                for(Client client : clients){
                    System.out.println(client.getId() + ". " + client.getLogin());
                }
                break;
            case 2:
                System.out.println("Please enter name of the client: ");
                String enter_login = scanner.nextLine();
                Client searched_client = bankWorkerDAO.find_client(enter_login);
                System.out.println(searched_client.getId() + ". " + searched_client.getLogin());
                System.out.println("Salary: " + searched_client.getSalary());
                System.out.println("Credit: " + searched_client.getCredit());
                break;
            case 3:
                Client client_max_credit = bankWorkerDAO.get_max_credit_client();
                System.out.println(client_max_credit.getId() + ". " + client_max_credit.getLogin());
                System.out.println("Credit amount: " + client_max_credit.getCredit());
                break;
            case 4:
                Client client_min_credit = bankWorkerDAO.get_min_credit_client();
                System.out.println(client_min_credit.getId() + ". " + client_min_credit.getLogin());
                System.out.println("Credit amount: " + client_min_credit.getCredit());
                break;
            case 5:
                System.out.println("Enter client name: ");
                String new_client_name = scanner.nextLine();
                System.out.println("Enter client salary: ");
                double client_salary = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter property type: ");
                String client_property_type = scanner.nextLine();
                System.out.println("Enter property price: ");
                double client_property_price = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter credit period");
                int credit_period = scanner.nextInt();
                scanner.nextLine();

                bankWorkerDAO.add_new_client(new_client_name, client_salary, client_property_type, client_property_price, credit_period);
                System.out.println("Client added successfully");
        }
    }
}
