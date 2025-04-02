package com.example.textbook_loan_program.model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String availabilityStatus;

    public Book(String isbn, String title, String author, String availabilityStatus) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.availabilityStatus = availabilityStatus;
    }

    public Book(int id, String title, String author, int quantity) {

    }

    public String getTitle(){
        return title;
    }

    public void checkAvailability(String status){
        this.availabilityStatus = status;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return 0;
    }

    public int getId() {
        return 0;
    }
}
