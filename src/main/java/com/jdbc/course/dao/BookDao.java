package com.jdbc.course.dao;

import com.jdbc.course.domain.Author;
import com.jdbc.course.domain.Book;

public interface BookDao {

    Book getById(Long id);
    Book findBookByTitle(String title);
    Book saveNewBook(Book book);
    Book updateBook(Book book);
    void deleteBookById(Long id);
}
