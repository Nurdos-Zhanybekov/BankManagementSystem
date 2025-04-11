package io.project.controllers;

import io.project.Authentication;
import io.project.BankWorker;
import io.project.Client;

import java.util.Scanner;

public class MainController {
    private final Authentication authentication = new Authentication();
    private static final Scanner scanner = new Scanner(System.in);

    public void start(){
        boolean running = true;
        while(running) {
            System.out.println("Please enter your account type, login and password: ");
            String enter_account_type = scanner.nextLine();
            String enter_login = scanner.nextLine();
            String enter_password = scanner.nextLine();

            var user = authentication.login(enter_account_type, enter_login, enter_password);

            if (user == null) {
                System.out.println("User has not been found. Try again");
            }

            if (user instanceof Client) {
                System.out.println("ok");
            } else if (user instanceof BankWorker bankWorker) {
                running = false;
                clearConsole();
                new BankWorkerController().showMenu(bankWorker);
            }
        }
    }

    public static void clearConsole(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }
}
