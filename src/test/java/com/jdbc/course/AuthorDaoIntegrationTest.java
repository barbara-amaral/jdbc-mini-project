package com.jdbc.course;

import com.jdbc.course.dao.AuthorDao;
import com.jdbc.course.domain.Author;
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
public class AuthorDaoIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthorById() {
        Author author = authorDao.getById(2L);

        assertThat(author).isNotNull();

    }

    @Test
    void testGetAuthorByName() {
        Author author =  authorDao.findAuthorByName("Eric", "Evans");
        assertThat(author).isNotNull();
    }

    @Test
    void saveAuthor() {
        Author author = new Author();
        author.setFirstName("Marcos");
        author.setLastName("T222");
        Author saved = authorDao.saveNewAuthor(author);
        assertThat(saved).isNotNull();

    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("T");

        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastName("Thompson");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Thompson");
    }

    @Test
    void testDeleteAuthor() {

        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            Author deleted = authorDao.getById(saved.getId());
        });

    }
}
