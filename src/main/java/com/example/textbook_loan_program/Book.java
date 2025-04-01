package com.example.textbook_loan_program;

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

    public String getTitle(){
        return title;
    }

    public void checkAvailability(String status){
        this.availabilityStatus = status;
    }

    public String getAuthor() {
        return author;
    }
}
