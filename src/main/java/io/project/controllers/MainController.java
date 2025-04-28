package io.project.controllers;

import io.project.service.Authentication;
import io.project.model.BankWorker;
import io.project.model.Client;
import java.util.Scanner;

public class MainController {
    private final Authentication authentication = new Authentication();
    private static final Scanner scanner = new Scanner(System.in);

    public void start(){
        boolean running = true;
        while(running) {
            System.out.println("Please enter your account type, login and password: ");
            String enterAccountType = scanner.nextLine();
            String enterLogin = scanner.nextLine();
            String enterPassword = scanner.nextLine();

            var user = authentication.authenticateUser(enterAccountType, enterLogin, enterPassword);

            if (user == null) {
                System.out.println("User has not been found. Try again");
            }

            if (user instanceof Client client) {
                running = false;
                clearConsole();
                new ClientController().getMenu(client);
            } else if (user instanceof BankWorker bankWorker) {
                running = false;
                clearConsole();
                new BankWorkerController().getMenu(bankWorker);
            }
        }
    }

    public static void clearConsole(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }
}
