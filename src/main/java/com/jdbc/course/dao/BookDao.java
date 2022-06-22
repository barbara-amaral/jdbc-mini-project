package com.jdbc.course.dao;

import com.jdbc.course.domain.Book;

public interface BookDao {

    Book getById(Long id);
    Book saveNewBook(Book book);
    void deleteBookById(Long id);
    Book updateBook(Book book);
    Book findBookByTitle(String title);
}
