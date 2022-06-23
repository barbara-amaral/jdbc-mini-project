package com.jdbc.course;

import com.jdbc.course.dao.BookDao;
import com.jdbc.course.domain.Author;
import com.jdbc.course.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = "com.jdbc.course.dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {
    @Autowired
    BookDao bookDao;

    @Test
    void testSaveNewBook() {

        Book book = new Book();
        book.setTitle("Twilight");
        book.setIsbn("09987");
        book.setPublisher("Something");
        book.setAuthor(1L);

        Book saved = bookDao.saveNewBook(book);
        assertThat(saved).isNotNull();

    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setTitle("Twilight");
        book.setIsbn("09987");
        book.setPublisher("Something");
        book.setAuthor(1L);

        Book saved = bookDao.saveNewBook(book);

        Book fetched = bookDao.getById(saved.getId());
        assertThat(fetched).isNotNull();
    }

    @Test
    void deleteBookById() {
        Book book = new Book();
        book.setTitle("Twilight");
        book.setIsbn("09987");
        book.setPublisher("Something");

        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookDao.getById(saved.getId());
        });
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setTitle("Twilight");
        book.setIsbn("09987");
        book.setPublisher("Something");
        book.setAuthor(1L);

        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("Outro livro");
        Book updated = bookDao.updateBook(saved);

        assertThat(updated.getTitle()).isEqualTo("Outro livro");
    }

    @Test
    void findBookByTitle() {

        Book book = new Book();
        book.setTitle("All about java 22");
        book.setIsbn("09987008");
        book.setPublisher("Something");
        book.setAuthor(1L);

        Book saved = bookDao.saveNewBook(book);

        Book fetched = bookDao.findBookByTitle(saved.getTitle());
        assertThat(fetched).isNotNull();

    }
}

