package com.example.textbook_loan_program.dao;

import com.example.textbook_loan_program.model.Book;
import java.util.List;

public interface BookDao {
    List<Book> findAll();
    Book findById(int id);
    void save(Book book);
    void update(Book book);
    void delete(int id);
}