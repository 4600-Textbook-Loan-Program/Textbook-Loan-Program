package com.example.textbook_loan_program;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        Book book = BookApiClient.fetchBookInfo("The Hobbit");
        if (book != null) {
            System.out.println("📗 " + book.getTitle() + " by " + book.getAuthor());
        } else {
            System.out.println("❌ Book not found");
        }
    }
}