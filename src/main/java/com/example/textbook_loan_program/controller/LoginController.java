package com.example.textbook_loan_program.controller;

import com.example.textbook_loan_program.dao.JdbcUserDao;
import com.example.textbook_loan_program.model.Administrator;
import com.example.textbook_loan_program.model.Student;
import com.example.textbook_loan_program.model.User;
import com.example.textbook_loan_program.service.UserService;
import com.example.textbook_loan_program.view.AdminDashboard;
import com.example.textbook_loan_program.view.StudentDashboard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {

    private final UserService userService = new UserService(new JdbcUserDao());

    public void showLoginScreen(Stage primaryStage) {
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label statusLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            User user = userService.login(username, password);

            if (user != null) {
                statusLabel.setText("Login successful!");
                if (user instanceof Administrator) {
                    AdminDashboard.show(primaryStage);
                } else if (user instanceof Student) {
                    StudentDashboard.show(primaryStage);
                }
            } else {
                statusLabel.setText("Invalid username or password");
            }
        });

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginButton, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Textbook Loan Login");
        primaryStage.show();
    }
}
