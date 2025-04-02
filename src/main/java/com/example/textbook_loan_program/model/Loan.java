package com.example.textbook_loan_program.model;

import java.time.LocalDate;
import java.util.Date;

public class Loan {
    private int loanId;
    private Student student;
    private Book book;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;

    public Loan(int loanId, Student student, Book book, Date borrowDate, Date dueDate, Date returnDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Loan(Book book, Student student, LocalDate loanDate, LocalDate returnDate) {

    }

    public void markReturned(){

    }

    public boolean checkOverdue(){
        return false;
    }

    public Book getBook() {
        return null;
    }

    public User getStudent() {
        return null;
    }

    public LocalDate getLoanDate() {
        return null;
    }
}
