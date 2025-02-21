module com.example.textbook_loan_program {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.textbook_loan_program to javafx.fxml;
    exports com.example.textbook_loan_program;
}