package com.example.textbook_loan_program;

abstract class User {
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

    public void login(){

    }

    public void logout(){

    }
}
