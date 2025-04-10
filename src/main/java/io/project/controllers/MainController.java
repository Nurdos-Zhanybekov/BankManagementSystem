package io.project.controllers;

import io.project.Authentication;
import io.project.Client;

import java.util.Scanner;

public class MainController {
    private Authentication authentication = new Authentication();
    private static final Scanner scanner = new Scanner(System.in);
    public void start(){
        System.out.println("Please enter your account type, login and password: ");
        String enter_account_type = scanner.nextLine();
        String enter_login = scanner.nextLine();
        String enter_password = scanner.nextLine();

        var user = authentication.login(enter_account_type, enter_login, enter_password);

        if(user instanceof Client){
            System.out.println("ok");
        }else{
            System.out.println("also ok");
        }
    }
}
