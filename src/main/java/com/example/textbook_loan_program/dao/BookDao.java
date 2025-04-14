package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    List<Book> findAll();
    Book findById(int id);
    void save(Book book) throws SQLException;
    void update(Book book);
    void delete(int id);
}