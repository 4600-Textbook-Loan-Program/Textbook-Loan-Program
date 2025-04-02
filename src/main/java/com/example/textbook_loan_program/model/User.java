package com.example.textbook_loan_program.model;

public abstract class User {
    protected int userId;
    protected String name;
    protected String email;
    protected String password;

    public User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String username, String password) {

    }

    public void login(){

    }

    public void logout(){

    }

    public String getUsername() {
        return null;
    }

    public String getPassword() {
        return null;
    }
}
