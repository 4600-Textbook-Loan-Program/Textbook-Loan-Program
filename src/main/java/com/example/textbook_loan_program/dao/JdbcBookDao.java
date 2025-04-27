package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.model.StudentRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookDao implements BookDao{

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"),
                        rs.getString("availability_status"),
                        rs.getString("cover_url"),
                        rs.getString("description")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Book findById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"),
                        rs.getString("availability_status"),
                        rs.getString("cover_url"),
                        rs.getString("description")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Book book) throws SQLException {
        String query = "INSERT INTO books (isbn, title, author, quantity, availability_status, cover_url, description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getQuantity());
            stmt.setString(5, book.getAvailabilityStatus());
            stmt.setString(6, book.getCoverUrl());
            stmt.setString(7, book.getDescription());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void update(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getQuantity());
            stmt.setInt(4, book.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book findByIsbn(String isbn) {
        String query = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"),
                        rs.getString("availability_status"),
                        rs.getString("cover_url"),
                        rs.getString("description")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<StudentRecord> findAllStudentLoanData() {
        List<StudentRecord> students = new ArrayList<>();

        String query = """
            SELECT u.username, u.role, COUNT(l.id) AS total_loans
            FROM users u
            LEFT JOIN loans l ON u.username = l.student_username
            GROUP BY u.username, u.role
            """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StudentRecord student = new StudentRecord(
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getInt("total_loans")
                );
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }


}