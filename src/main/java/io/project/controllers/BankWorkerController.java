package io.project.controllers;

import io.project.DAO.BalanceDAO;
import io.project.DAO.CreditDAO;
import io.project.model.Client;
import io.project.DAO.ClientDAO;
import io.project.model.Credit;
import io.project.model.User;
import io.project.service.CreditLoan;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankWorkerController {
    private final ClientDAO clientDao = new ClientDAO();
    private final CreditDAO creditDAO = new CreditDAO();
    private final BalanceDAO balanceDAO = new BalanceDAO();
    private final Scanner scanner = new Scanner(System.in);
    private final CreditLoan creditLoan = new CreditLoan();

    public void getMenu(User user) {
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

                List<String> propertyTypes;
                List<Double> creditPrices;

                switch (choose) {
                    case 1:
                        List<Client> clients = clientDao.getClientList();

                        for (Client client : clients) {
                            System.out.println(client.getId() + ". " + client.getName());
                        }
                        scanner.nextLine();
                        break;
                    case 2:
                        System.out.println("Please enter login of the client: ");
                        String enterLogin = scanner.nextLine();
                        propertyTypes = new ArrayList<>();
                        creditPrices = new ArrayList<>();
                        Client searchedClient = clientDao.findClient(enterLogin, propertyTypes, creditPrices);

                        if(searchedClient == null){
                            System.out.println("Couldn't find the client");
                        }else {
                            System.out.println(searchedClient.getId() + ". " + searchedClient.getLogin());
                            System.out.println("Current balance: " + searchedClient.getBalance());
                            System.out.println("Salary: " + searchedClient.getSalary());
                            System.out.println("Credits: ");

                            for (int i = 0; i < propertyTypes.size(); i++) {
                                System.out.println((i + 1) + ". " + propertyTypes.get(i) + "(" + creditPrices.get(i) + "$)");
                            }
                        }

                        scanner.nextLine();
                        break;
                    case 3:
                        Client maxCreditClient = creditDAO.getMaxCreditClient();
                        Credit maxCredit = creditDAO.getMaxCredit();
                        System.out.println(maxCreditClient.getId() + ". " + maxCreditClient.getLogin());
                        System.out.println("Credit amount: " + maxCredit.getPropertyPriceWithCredit());
                        scanner.nextLine();
                        break;
                    case 4:
                        Client minCreditClient = creditDAO.getMinCreditClient();
                        Credit minCredit = creditDAO.getMinCredit();
                        System.out.println(minCreditClient.getId() + ". " + minCreditClient.getLogin());
                        System.out.println("Credit amount: " + minCredit.getPropertyPriceWithCredit());
                        scanner.nextLine();
                        break;
                    case 5:
                        System.out.println("Enter client name: ");
                        String enterClientName = scanner.nextLine();
                        System.out.println("Enter client login: ");
                        String enterClientLogin = scanner.nextLine();

                        System.out.println("Choose currency for client's balance: ");
                        int chooseCurrency = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter client's current balance: ");
                        double enterClientBalance = scanner.nextDouble();
                        scanner.nextLine();
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

                        Double totalCredit = creditLoan.countCreditSum(propertyType, propertyPrice, creditPeriod, clientSalary);

                        if (totalCredit == null) {
                            System.out.println("Client doesn't have enough income to get credit");
                            return;
                        }

                        creditDAO.addNewClient(enterClientName, enterClientLogin, clientSalary);
                        creditDAO.addNewClientCredit(enterClientLogin, propertyType, propertyPrice, creditPeriod, totalCredit);
                        balanceDAO.addNewClientBalance(enterClientLogin, chooseCurrency, enterClientBalance);
                        System.out.println("Client added successfully");
                        scanner.nextLine();
                        break;
                    case 6:
                        System.out.println("Enter client login: ");
                        String clientCreditHistoryLogin = scanner.nextLine();
                        propertyTypes = new ArrayList<>();
                        creditPrices = new ArrayList<>();
                        Client clientCreditHistory = creditDAO.getCreditHistoryClient(clientCreditHistoryLogin);
                        Credit creditHistory = creditDAO.getCreditHistory(clientCreditHistoryLogin, propertyTypes, creditPrices);

                        if(clientCreditHistory == null || creditHistory == null){
                            System.out.println("Couldn't find the client");
                        }else {
                            System.out.println(clientCreditHistory.getId() + ". " + clientCreditHistory.getName());
                            System.out.println("Credits: ");
                            for (int i = 0; i < propertyTypes.size(); i++) {
                                System.out.println((i + 1) + ". " + propertyTypes.get(i) + "(" + creditPrices.get(i) + "$)");
                            }
                            System.out.println("Total sum: " + creditHistory.getTotalCredit());
                        }
                        scanner.nextLine();
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
