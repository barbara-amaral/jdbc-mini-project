package com.jdbc.course.dao;

import com.jdbc.course.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE title = ?", getRowMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor());

        Long savedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(savedId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book SET title = ?, isbn = ?, publisher = ?, " +
                "author_id = ? WHERE id = ?", book.getTitle(), book.getIsbn(), book.getPublisher(),
                book.getAuthor(), book.getId());

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return new BookMapper();
    }
}
