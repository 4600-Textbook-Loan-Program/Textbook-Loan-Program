package com.example.textbook_loan_program;

import java.util.Date;

public class loan {
    private int loanId;
    private Student student;
    private Book book;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;

    public loan(int loanId, Student student, Book book, Date borrowDate, Date dueDate, Date returnDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public void markReturned(){

    }

    public boolean checkOverdue(){
        return false;
    }

}
