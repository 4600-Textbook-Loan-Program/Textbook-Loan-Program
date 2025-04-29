package com.example.textbook_loan_program.model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private String studentUsername;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;


    public Loan(int loan_id, String student_username, int book_id, LocalDate borrow_date, LocalDate due_date, LocalDate returnDate) {
        this.id = loan_id;
        this.studentUsername = student_username;
        this.bookId = book_id;
        this.borrowDate = borrow_date;
        this.dueDate = due_date;
        this.returnDate = returnDate;
    }


    public int getId() {
        return id;
    }

    public String getStudentUsername() {
        return studentUsername;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}

