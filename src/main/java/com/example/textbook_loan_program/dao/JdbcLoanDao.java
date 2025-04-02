package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.model.Loan;
import com.example.textbook_loan_program.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcLoanDao implements LoanDao {

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book(rs.getInt("book_id"), rs.getString("book_title"), rs.getString("book_author"), rs.getInt("book_quantity"));
                Student student = new Student(rs.getString("student_username"), "");
                LocalDate loanDate = rs.getDate("loan_date").toLocalDate();
                LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;

                Loan loan = new Loan(book, student, loanDate, returnDate);
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }

    @Override
    public void save(Loan loan) {
        String query = "INSERT INTO loans (book_id, book_title, book_author, book_quantity, student_username, loan_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loan.getBook().getId());
            stmt.setString(2, loan.getBook().getTitle());
            stmt.setString(3, loan.getBook().getAuthor());
            stmt.setInt(4, loan.getBook().getQuantity());
            stmt.setString(5, loan.getStudent().getUsername());
            stmt.setDate(6, Date.valueOf(loan.getLoanDate()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnLoan(int loanId) {
        String query = "UPDATE loans SET return_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, loanId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int loanId) {
        String query = "DELETE FROM loans WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loanId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
