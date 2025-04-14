package com.example.textbook_loan_program.controller;

import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.service.BookService;

public class DatabaseTest {
    public static void main(String[] args) {
        Book book = BookService.fetchBookInfo("The Hobbit");
        if (book != null) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        } else {
            System.out.println("Book not found");
        }
    }
}