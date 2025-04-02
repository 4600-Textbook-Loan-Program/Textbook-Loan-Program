package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();

    User findByUsername(String username);

    void save(User user);

    void delete(String username);
}
