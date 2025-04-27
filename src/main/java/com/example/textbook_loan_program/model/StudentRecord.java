package com.example.textbook_loan_program.model;

public class StudentRecord {
    private String username;
    private String role;
    private int totalLoans;

    public StudentRecord(String username, String role, int totalLoans) {
        this.username = username;
        this.role = role;
        this.totalLoans = totalLoans;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public int getTotalLoans() { return totalLoans; }
}