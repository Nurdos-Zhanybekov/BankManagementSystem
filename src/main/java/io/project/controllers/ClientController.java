package io.project.controllers;

import io.project.DAO.BalanceDAO;
import io.project.DAO.ClientDAO;
import io.project.DAO.CreditDAO;
import io.project.model.Client;
import io.project.model.Credit;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ClientController {
    private final ClientDAO clientDAO = new ClientDAO();
    private final CreditDAO creditDAO = new CreditDAO();
    private final BalanceDAO balanceDAO = new BalanceDAO();

    public void getMenu(Client client){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome, " + client.getName());

        boolean finished = false;
        while(!finished) {
            try {
                System.out.println("Please choose command:");

                System.out.println("1. Show list of credits");
                System.out.println("2. Show balance");
                System.out.println("3. Buy currency");
                System.out.println("4. Transfer money to another user");
                System.out.println("5. Exit");

                int chooseOption = scanner.nextInt();
                scanner.nextLine();

                int chooseCurrency;
                int som = 1;
                int dollar = 2;

                double somAmount;
                double dollarAmount;

                switch (chooseOption){
                    case 1:
                        List<String> propertyTypes = new ArrayList<>();
                        List<Double> creditPrices = new ArrayList<>();
                        Credit credit = creditDAO.getCreditHistory(client.getLogin(), propertyTypes, creditPrices);
                        System.out.println(client.getId() + ". " + client.getName());
                        System.out.println("Credits: ");
                        for (int i = 0; i < propertyTypes.size(); i++) {
                            System.out.println((i + 1) + ". " + propertyTypes.get(i) + "(" + creditPrices.get(i) + "$)");
                        }
                        System.out.println("Total sum: " + credit.getTotalCredit());
                        System.out.println();
                        scanner.nextLine();
                        break;
                    case 2:
                        System.out.println("1. Show balance in soms");
                        System.out.println("2. Show balance in dollars");
                        System.out.println(client.getLogin());
                        chooseCurrency = scanner.nextInt();
                        scanner.nextLine();

                        double balance = balanceDAO.getBalance(chooseCurrency, client.getLogin());

                        if(chooseCurrency == 1) {
                            System.out.println("Your balance: " + balance + " soms");
                        }else{
                            System.out.println("Your balance: " + balance + " dollars");
                        }
                        scanner.nextLine();
                        break;
                    case 3:
                        System.out.println("Choose currency which you want to obtain: ");
                        System.out.println("1. Som");
                        System.out.println("2. Dollar");
                        chooseCurrency = scanner.nextInt();

                        if(chooseCurrency == som){
                            System.out.println("Enter amount of dollars you want to convert: ");
                            dollarAmount = scanner.nextDouble();
                            scanner.nextLine();
                            balanceDAO.buyCurrency(som, dollarAmount, client.getName());
                        }else if(chooseCurrency == dollar){
                            System.out.println("Enter amount of soms you want to convert: ");
                            somAmount = scanner.nextDouble();
                            scanner.nextLine();
                            balanceDAO.buyCurrency(dollar, somAmount, client.getName());
                        }
                        scanner.nextLine();
                        break;
                    case 4:
                        System.out.println("Enter client login: ");
                        String enterLogin = scanner.nextLine();
                        System.out.println("Choose currency for transfer: ");
                        System.out.println("1. Som");
                        System.out.println("2. Dollar");
                        chooseCurrency = scanner.nextInt();
                        scanner.nextLine();

                        if(chooseCurrency == som){
                            System.out.println("Enter amount of soms you want to transfer: ");
                            somAmount = scanner.nextDouble();
                            scanner.nextLine();
                            balanceDAO.transferCurrency(som, somAmount, client.getName(), enterLogin);
                        }else if(chooseCurrency == dollar){
                            System.out.println("Enter amount of dollars you want to transfer: ");
                            dollarAmount = scanner.nextDouble();
                            scanner.nextLine();
                            balanceDAO.transferCurrency(dollar, dollarAmount, client.getName(), enterLogin);
                        }
                        scanner.nextLine();
                        break;
                    case 5:
                        System.out.println("Exiting.");
                        finished = true;
                }
            }catch (InputMismatchException e){
                System.out.println("Input Mismatch. Please try again");
                scanner.nextLine();
            }
        }
    }
}
