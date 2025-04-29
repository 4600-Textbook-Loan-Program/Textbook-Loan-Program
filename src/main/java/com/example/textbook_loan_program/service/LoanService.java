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


    public void borrowBook(String studentUsername, int bookId) {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusMonths(4);
        Loan loan = new Loan(0, studentUsername, bookId, borrowDate, dueDate, null);
        loanDao.borrowBook(loan);
    }

    public void returnBook(int loanId) {
        loanDao.returnBook(loanId);
    }


    public List<Loan> getLoansByUser(String username) {
        return loanDao.findLoansByUser(username);
    }


    public List<Loan> getAllLoans() {
        return loanDao.findAll();
    }
}