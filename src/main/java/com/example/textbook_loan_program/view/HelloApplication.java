package com.example.textbook_loan_program.view;

import com.example.textbook_loan_program.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginController loginController = new LoginController();
        loginController.showLoginScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}



