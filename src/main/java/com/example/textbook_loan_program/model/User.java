package com.example.textbook_loan_program.model;

public abstract class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int userId, String name, String email, String password) {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
