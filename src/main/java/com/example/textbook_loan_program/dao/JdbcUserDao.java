package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.config.DatabaseConnector;
import com.example.textbook_loan_program.model.Administrator;
import com.example.textbook_loan_program.model.Student;
import com.example.textbook_loan_program.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String role = rs.getString("role");
                User user = role.equals("admin") ?
                        new Administrator(rs.getString("username"), rs.getString("password")) :
                        new Student(rs.getString("username"), rs.getString("password"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String user = rs.getString("username");
                String pass = rs.getString("password");

                if (role.equals("admin")) {
                    return new Administrator(user, pass);
                } else {
                    return new Student(user, pass);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user instanceof Administrator ? "admin" : "student");
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}