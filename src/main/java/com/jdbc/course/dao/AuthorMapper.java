package com.jdbc.course.dao;

import com.jdbc.course.domain.Author;
import com.jdbc.course.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        rs.next();

        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));

        if(rs.getString("isbn") != null) {
            author.setBooks(new ArrayList<>());
            author.getBooks().add(mapBook(rs));
        }

        while (rs.next()){
            author.getBooks().add(mapBook(rs));
        }

        return author;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("book_id"));
        book.setTitle(rs.getString("title"));
        book.setIsbn(rs.getString("isbn"));
        book.setPublisher(rs.getString("publisher"));
        book.setAuthor(rs.getLong("id"));

        return book;
    }
}
