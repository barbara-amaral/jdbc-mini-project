package com.jdbc.course.dao;

import com.jdbc.course.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao{

    private final DataSource source;
    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource source, AuthorDao authorDao) {
        this.source = source;
        this.authorDao = authorDao;
    }

    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO book (title, publisher, isbn, author_id) VALUES (?,?,?,?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getPublisher());
            preparedStatement.setString(3, book.getIsbn());

            if (book.getAuthor() != null) {
                preparedStatement.setLong(4, book.getAuthor().getId());
            }else {
                preparedStatement.setNull(4, -5);
            }

            preparedStatement.execute();

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");

            if(resultSet.next()) {
                long savedId = resultSet.getLong(1);
                return this.getById(savedId);
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM book WHERE id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                closeAll(connection, preparedStatement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE id = ?");
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getPublisher());
            preparedStatement.setString(3, book.getTitle());

            if (book.getAuthor() != null) {
                preparedStatement.setLong(4, book.getAuthor().getId());
            }

            preparedStatement.setLong(5, book.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeAll(connection, preparedStatement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.getById(book.getId());
    }

    @Override
    public Book findBookByTitle(String title) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE title = ?");
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Book book = new Book();
                book.setTitle(resultSet.getString("title"));
                book.setId(resultSet.getLong("id"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));

                return book;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setTitle(resultSet.getString("title"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));
        book.setId(resultSet.getLong("id"));
        return book;
    }

    private void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        if(connection != null) {
            connection.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (resultSet != null) {
            resultSet.close();
        }
    }
}
