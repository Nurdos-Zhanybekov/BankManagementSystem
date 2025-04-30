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
    private final CreditLoan creditLoan = new CreditLoan();
    private final Scanner scanner = new Scanner(System.in);

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
                            System.out.println(searchedClient.getId() + ". " + searchedClient.getName());
                            System.out.println("Current balance in som: " + searchedClient.getBalanceSom());
                            System.out.println("Current balance in dollar: " + searchedClient.getBalanceDollar());

                            System.out.println("Salary in som: " + searchedClient.getSalarySom());
                            System.out.println("Salary in dollar: " + searchedClient.getSalaryDollar());
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
                        System.out.println(maxCreditClient.getId() + ". " + maxCreditClient.getName());
                        System.out.println("Credit amount: " + maxCredit.getPropertyPriceWithCredit());
                        scanner.nextLine();
                        break;
                    case 4:
                        Client minCreditClient = creditDAO.getMinCreditClient();
                        Credit minCredit = creditDAO.getMinCredit();
                        System.out.println(minCreditClient.getId() + ". " + minCreditClient.getName());
                        System.out.println("Credit amount: " + minCredit.getPropertyPriceWithCredit());
                        scanner.nextLine();
                        break;
                    case 5:
                        System.out.println("Enter client name: ");
                        String enterClientName = scanner.nextLine();
                        System.out.println("Enter client login: ");
                        String enterClientLogin = scanner.nextLine();
                        System.out.println("Enter client password: ");
                        String enterPassword = scanner.nextLine();
                        System.out.println("Choose currency for client's salary: ");
                        System.out.println("1. Som");
                        System.out.println("2. Dollar");
                        System.out.println("Enter client salary: ");
                        double enteredSalary = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Choose currency for client's balance: ");
                        System.out.println("1. Som");
                        System.out.println("2. Dollar");
                        int balanceCurrency = scanner.nextInt();
                        System.out.println("Enter client's current balance: ");
                        double enteredBalance = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Enter property type: ");
                        System.out.println("Apartment");
                        System.out.println("Car");
                        System.out.println("Other");
                        String propertyType = scanner.nextLine();
                        System.out.println("Enter property price(in dollars) : ");
                        double enteredPropertyPrice = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Enter credit period (months): ");
                        int creditPeriod = scanner.nextInt();
                        scanner.nextLine();

                        Double totalCredit = creditLoan.countCreditSum(propertyType, enteredPropertyPrice, creditPeriod, enteredSalary);

                        if (totalCredit == null) {
                            System.out.println("Client doesn't have enough income to get credit");
                            scanner.nextLine();
                            break;
                        }

                        creditDAO.addNewClient(enterClientName, enterClientLogin, enteredSalary);
                        creditDAO.addNewUser("client", enterClientLogin, enterPassword);
                        creditDAO.addNewClientCredit(enterClientLogin, propertyType, enteredPropertyPrice, creditPeriod, totalCredit);
                        balanceDAO.addNewClientBalance(enterClientLogin, balanceCurrency, enteredBalance);
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
