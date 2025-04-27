package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcLoanDao implements LoanDao {

    @Override
    public void borrowBook(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getBorrowDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = GETDATE() WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Loan> findLoansByUser(String username) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.* FROM loans l JOIN users u ON l.user_id = u.id WHERE u.username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }

    private Loan mapRowToLoan(ResultSet rs) throws SQLException {
        return new Loan(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
        );
    }

    public void createLoan(int userId, int bookId) {
        String query = "INSERT INTO loans (user_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusMonths(3)));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
