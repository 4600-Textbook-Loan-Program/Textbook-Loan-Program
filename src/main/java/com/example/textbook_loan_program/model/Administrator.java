package com.example.textbook_loan_program.model;


import com.example.textbook_loan_program.model.Book;

public class Administrator extends User {


    public Administrator(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Administrator(String username, String password) {
        super(username, password);
    }

    public void manageInventory(Book book, String action){

    }

    public void generateReports(String report){

    }

}
