package io.project.controllers;

import io.project.model.Client;
import io.project.DAO.BankWorkerDAO;
import io.project.model.User;
import io.project.service.CreditLoan;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankWorkerController {
    private final BankWorkerDAO bankWorkerDao = new BankWorkerDAO();
    private final Scanner scanner = new Scanner(System.in);
    private final CreditLoan creditLoan = new CreditLoan();

    public void showMenu(User user) {
        System.out.println("Welcome " + user.getLogin());
        boolean finished = false;
        while(!finished) {
            try {
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
                int enter_response;
                int answer_no = 2;

                List<String> apartments;
                List<Double> creditAmounts;

                switch (choose) {
                    case 1:
                        List<Client> clients = bankWorkerDao.getClientList();

                        for (Client client : clients) {
                            System.out.println(client.getId() + ". " + client.getName());
                        }

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 2:
                        System.out.println("Please enter name of the client: ");
                        String enteredLogin = scanner.nextLine();
                        apartments = new ArrayList<>();
                        creditAmounts = new ArrayList<>();
                        Client searchedClient = bankWorkerDao.findClient(enteredLogin, apartments, creditAmounts);
                        System.out.println(searchedClient.getId() + ". " + searchedClient.getLogin());
                        System.out.println("Salary: " + searchedClient.getSalary());
                        System.out.println("Credits: ");

                        for (int i = 0; i < apartments.size(); i++) {
                            System.out.println((i + 1) + ". " + apartments.get(i) + "(" + creditAmounts.get(i) + "$)");
                        }

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 3:
                        Client maxCreditClient = bankWorkerDao.getMaxCreditClient();
                        System.out.println(maxCreditClient.getId() + ". " + maxCreditClient.getLogin());
                        System.out.println("Credit amount: " + maxCreditClient.getCreditAmount());

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 4:
                        Client minCreditClient = bankWorkerDao.getMinCreditClient();
                        System.out.println(minCreditClient.getId() + ". " + minCreditClient.getLogin());
                        System.out.println("Credit amount: " + minCreditClient.getCreditAmount());

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 5:
                        System.out.println("Enter client name: ");
                        String clientName = scanner.nextLine();
                        System.out.println("Enter client salary: ");
                        double clientSalary = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Enter property type: ");
                        String propertyType = scanner.nextLine();
                        System.out.println("Enter property price: ");
                        double propertyPrice = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Enter credit period: ");
                        int creditPeriod = scanner.nextInt();
                        scanner.nextLine();

                        Double creditSum = creditLoan.countCreditSum(propertyType, propertyPrice, creditPeriod, clientSalary);

                        if (creditSum == null) {
                            System.out.println("Client doesn't have enough income to get credit");
                            return;
                        }

                        bankWorkerDao.addNewClient(clientName, clientSalary, propertyType, propertyPrice, creditPeriod, creditSum);
                        System.out.println("Client added successfully");

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 6:
                        System.out.println("Enter client name: ");
                        String creditHistoryName = scanner.nextLine();
                        apartments = new ArrayList<>();
                        creditAmounts = new ArrayList<>();
                        Client clientCreditHistory = bankWorkerDao.getCreditHistory(creditHistoryName, apartments, creditAmounts);
                        System.out.println(clientCreditHistory.getId() + ". " + clientCreditHistory.getName());
                        System.out.println("Credits: ");
                        for (int i = 0; i < apartments.size(); i++) {
                            System.out.println((i + 1) + ". " + apartments.get(i) + "(" + creditAmounts.get(i) + "$)");
                        }
                        System.out.println("Total sum: " + clientCreditHistory.getTotalCredit());

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enter_response = scanner.nextInt();
                        scanner.nextLine();

                        if(enter_response == answer_no){
                            finished = true;
                        }
                        break;
                    case 7:
                        System.out.println("Exiting");
                        finished = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 7");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input mismatch. Please enter a number between 1 and 7");
                scanner.nextLine();
            }
        }
    }
}
