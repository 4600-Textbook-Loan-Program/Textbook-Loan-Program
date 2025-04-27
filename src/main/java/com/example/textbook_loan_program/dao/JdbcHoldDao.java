package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Hold;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcHoldDao {

    public List<Hold> findHoldsForBook(int bookId) {
        List<Hold> holds = new ArrayList<>();
        String query = "SELECT * FROM holds WHERE book_id = ? ORDER BY hold_date ASC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                holds.add(new Hold(
                        rs.getInt("id"),
                        rs.getString("student_username"),
                        rs.getInt("book_id"),
                        rs.getDate("hold_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return holds;
    }

    public void deleteHold(int holdId) {
        String query = "DELETE FROM holds WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, holdId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpiredHolds() {
        String query = "DELETE FROM holds WHERE hold_date < DATEADD(day, -3, GETDATE())";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
