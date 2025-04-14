package com.example.textbook_loan_program.model;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private int quantity;
    private String availabilityStatus;
    private String coverUrl;
    private String description;

    // Full constructor
    public Book(int id, String isbn, String title, String author, int quantity, String availabilityStatus, String coverUrl, String description) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.availabilityStatus = availabilityStatus;
        this.coverUrl = coverUrl;
        this.description = description;
    }

    // Constructor for new books (no ID yet)
    public Book(String isbn, String title, String author, String coverUrl, String description) {
        this(0, isbn, title, author, 1, "Available", coverUrl, description);
    }

    // Constructor for manually entered books
    public Book(String isbn, String title, String author, int quantity) {
        this(0, isbn, title, author, quantity, "Available", null, null);
    }




    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
