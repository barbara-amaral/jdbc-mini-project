package com.jdbc.course;

import com.jdbc.course.domain.Author;
import com.jdbc.course.domain.Book;
import com.jdbc.course.repositories.AuthorRepository;
import com.jdbc.course.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MySQLIntegrationTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Test
    void testMySQL() {
        long countBefore = bookRepository.count();
        long countBefore2 = authorRepository.count();

        assertThat(countBefore).isGreaterThan(0);
        assertThat(countBefore2).isGreaterThan(0);
    }

    @Test
    void authorTest() {
        Author author = new Author("Barbara", "Amaral");
        Author saved = authorRepository.save(author);

        assertThat(saved).isNotNull();

        Author fetched = authorRepository.getById(saved.getId());
        assertThat(fetched).isNotNull();
    }

    @Test
    void bookTest() {
        Book book = bookRepository.save(new Book());

        assertThat(book).isNotNull();
        assertThat(book.getId()).isNotNull();

        Book fetched = bookRepository.getById(book.getId());
        assertThat(fetched).isNotNull();

    }
}
