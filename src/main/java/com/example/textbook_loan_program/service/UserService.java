package com.example.textbook_loan_program.service;

import com.example.textbook_loan_program.dao.UserDao;
import com.example.textbook_loan_program.model.User;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && password != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    // Optional static method (can be removed if unused)
    public static User authenticate(String username, String password) {
        UserDao dao = new com.example.textbook_loan_program.dao.JdbcUserDao();
        return new UserService(dao).login(username, password);
    }
}


