package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.model.Loan;

import java.util.List;

public interface LoanDao {
    void borrowBook(Loan loan);
    void returnBook(int loanId);
    List<Loan> findLoansByUser(String username);
    List<Loan> findAll();
}
