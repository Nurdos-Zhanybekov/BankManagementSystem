package io.project.controllers;

import io.project.DAO.ClientDAO;
import io.project.model.Client;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ClientController {
    private final ClientDAO clientDAO = new ClientDAO();

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
                int enterResponse;
                int answer_no = 2;

                int chooseCurrency;

                switch (chooseOption){
                    case 1:
                        List<String> apartments = new ArrayList<>();
                        List<Double> creditAmounts = new ArrayList<>();
                        client = clientDAO.getCreditHistory(client.getName(), apartments, creditAmounts);
                        System.out.println(client.getId() + ". " + client.getName());
                        System.out.println("Credits: ");
                        for (int i = 0; i < apartments.size(); i++) {
                            System.out.println((i + 1) + ". " + apartments.get(i) + "(" + creditAmounts.get(i) + "$)");
                        }
                        System.out.println("Total sum: " + client.getTotalCredit());
                        System.out.println();
                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enterResponse = scanner.nextInt();
                        scanner.nextLine();

                        if(enterResponse == answer_no){
                            finished = true;
                        }
                        break;
                    case 2:
                        System.out.println("1. Show balance in soms");
                        System.out.println("2. Show balance in dollars");
                        chooseCurrency = scanner.nextInt();
                        scanner.nextLine();

                        double balance = clientDAO.getBalance(chooseCurrency, client.getName());

                        if(chooseCurrency == 1) {
                            System.out.println("Your balance: " + balance + " soms");
                        }else{
                            System.out.println("Your balance: " + balance + " dollars");
                        }

                        System.out.println("Do you wish to continue: ");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        enterResponse = scanner.nextInt();
                        scanner.nextLine();

                        if(enterResponse == answer_no){
                            finished = true;
                        }
                        break;
                    case 3:
                        System.out.println("Choose currency which you want to obtain: ");
                        System.out.println("1. Som");
                        System.out.println("2. Dollar");
                        chooseCurrency = scanner.nextInt();
                    case 5:
                        System.out.println("Exiting.");
                        finished = true;
                }
            }catch (InputMismatchException e){
                System.out.println("Input Mismatch. Please try again");
            }
        }
    }
}
