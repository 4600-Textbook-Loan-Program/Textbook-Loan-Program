package com.example.textbook_loan_program.service;

import com.example.textbook_loan_program.dao.LoanDao;
import com.example.textbook_loan_program.model.Loan;

import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private final LoanDao loanDao;

    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    // Borrow a book (default 2-week loan)
    public void borrowBook(int userId, int bookId) {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusMonths(4);
        Loan loan = new Loan(0, userId, bookId, borrowDate, dueDate, null);
        loanDao.borrowBook(loan);
    }

    // Return a book by marking return date
    public void returnBook(int loanId) {
        loanDao.returnBook(loanId);
    }

    // All current or past loans by user
    public List<Loan> getLoansByUser(String username) {
        return loanDao.findLoansByUser(username);
    }

    // For admin view: all loans in the system
    public List<Loan> getAllLoans() {
        return loanDao.findAll();
    }
}