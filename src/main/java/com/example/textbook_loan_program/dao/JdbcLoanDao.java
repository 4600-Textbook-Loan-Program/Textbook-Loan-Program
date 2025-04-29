package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Loan;
import com.example.textbook_loan_program.model.StudentRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcLoanDao implements LoanDao {

    @Override
    public void borrowBook(Loan loan) {
        String sql = "INSERT INTO loans (student_username, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getStudentUsername());
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
        String sql = "UPDATE loans SET return_date = GETDATE() WHERE loan_id = ?";

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
        String sql = "SELECT * FROM loans WHERE student_username = ?";

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
                rs.getInt("loan_id"),
                rs.getString("student_username"),
                rs.getInt("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
        );
    }

    public void createLoan(String studentUsername, int bookId) {
        String query = "INSERT INTO loans (student_username, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentUsername);
            stmt.setInt(2, bookId);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusMonths(3)));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Loan findActiveLoan(String studentUsername, int bookId) {
        String query = "SELECT * FROM loans WHERE student_username = ? AND book_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentUsername);
            stmt.setInt(2, bookId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToLoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void markAsReturned(int loanId) {
        String updateLoanQuery = "UPDATE loans SET return_date = GETDATE() WHERE loan_id = ?";
        String updateBookQuery = "UPDATE books SET quantity = quantity + 1 WHERE book_id = (SELECT book_id FROM loans WHERE loan_id = ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement updateLoanStmt = conn.prepareStatement(updateLoanQuery);
             PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery)) {

            updateLoanStmt.setInt(1, loanId);
            updateLoanStmt.executeUpdate();

            updateBookStmt.setInt(1, loanId);
            updateBookStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StudentRecord> findAllStudentLoanData() {
        List<StudentRecord> records = new ArrayList<>();
        String sql = """
        SELECT u.username, u.role, COUNT(l.loan_id) AS total_loans
        FROM users u
        LEFT JOIN loans l ON u.username = l.student_username
        WHERE u.role = 'STUDENT'
        GROUP BY u.username, u.role
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String role = rs.getString("role");
                int totalLoans = rs.getInt("total_loans");
                records.add(new StudentRecord(username, role, totalLoans));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

}
