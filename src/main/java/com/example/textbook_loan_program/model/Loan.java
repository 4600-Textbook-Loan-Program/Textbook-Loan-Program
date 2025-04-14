package com.example.textbook_loan_program.model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int userId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    // Optional: references for UI purposes
    private Student student;
    private Book book;

    public Loan(int id, int userId, int bookId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Loan(int id, int userId, int bookId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, Student student, Book book) {
        this(id, userId, bookId, borrowDate, dueDate, returnDate);
        this.student = student;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
