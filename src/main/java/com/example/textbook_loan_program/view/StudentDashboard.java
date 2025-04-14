package com.example.textbook_loan_program.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentDashboard {
    public static void show(Stage stage) {
        Label welcome = new Label("Welcome, Student!");
        VBox vbox = new VBox(10, welcome);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Student Dashboard");
        stage.show();
    }
}