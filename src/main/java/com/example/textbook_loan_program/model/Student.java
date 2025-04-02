package com.example.textbook_loan_program.model;


import com.example.textbook_loan_program.model.Book;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private List<Book> borrowedBooks;
    public Student(int userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.borrowedBooks = new ArrayList<>();
    }

    public Student(String username, String password) {
        super(username, password);
    }

    public void searchBook(String title){

    }

    public void reserveBook(Book book){

    }

    public void borrowBook(Book book){

    }

    public void returnBook(Book book){

    }

    public void overdueNotification(){

    }


}
