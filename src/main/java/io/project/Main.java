package io.project;

import io.project.config.DBConnection;
import io.project.controllers.MainController;

public class Main {
    public static void main(String[] args) {
        new MainController().start();
    }
}