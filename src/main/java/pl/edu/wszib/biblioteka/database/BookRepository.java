package pl.edu.wszib.biblioteka.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.configuration.DatabaseConfig;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByAuthorEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByIDEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByTitleEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookEx;
import pl.edu.wszib.biblioteka.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookRepository implements IBookRepository {
    private final DatabaseConfig databaseConfig;

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) = LOWER(?)";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, author);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (books.isEmpty()) {
            throw new CanNotFindBookByAuthorEx();
        }
        return books;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(title) = LOWER(?)";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (books.isEmpty()) {
            throw new CanNotFindBookByTitleEx();
        }
        return books;
    }

    @Override
    public void rentBook(int id) {
        String checkSql = "SELECT rent FROM books WHERE id = ?";
        String updateSql = "UPDATE books SET rent = TRUE WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, id);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                boolean isRented = resultSet.getBoolean("rent");
                if (isRented) {
                    throw new CanNotRentBookEx();
                }
                
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, id);
                    updateStmt.executeUpdate();
                }
            } else {
                throw new CanNotRentBookEx();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(int id) {
        String sql = "UPDATE books SET rent = FALSE WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, release_year, rent) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getIsbn_number());
            statement.setInt(4, book.getRelease_year());
            statement.setBoolean(5, false);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBook(int book_id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, book_id);
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new CanNotFindBookByIDEx();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book updatedBook) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, release_year = ? WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, updatedBook.getTitle());
            statement.setString(2, updatedBook.getAuthor());
            statement.setInt(3, updatedBook.getIsbn_number());
            statement.setInt(4, updatedBook.getRelease_year());
            statement.setInt(5, updatedBook.getBook_id());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new CanNotFindBookByIDEx();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Book mapRowToBook(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                Integer.parseInt(resultSet.getString("isbn")),
                resultSet.getInt("release_year"),
                resultSet.getBoolean("rent")
        );
    }
}
