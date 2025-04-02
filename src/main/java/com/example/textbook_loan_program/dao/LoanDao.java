package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.model.Loan;

import java.util.List;

public interface LoanDao {

    List<Loan> findAll();

    void save(Loan loan);

    void returnLoan(int loanId);

    void delete(int loanId);
}
