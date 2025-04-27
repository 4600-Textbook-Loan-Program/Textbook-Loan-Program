package com.example.textbook_loan_program.model;

import java.time.LocalDate;

public class Hold {
    private int id;
    private String studentUsername;
    private int bookId;
    private LocalDate holdDate;

    public Hold(int id, String studentUsername, int bookId, LocalDate holdDate) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.holdDate = holdDate;
    }

    public int getId() { return id; }
    public String getStudentUsername() { return studentUsername; }
    public int getBookId() { return bookId; }
    public LocalDate getHoldDate() { return holdDate; }
}
