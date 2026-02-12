package pl.edu.wszib.biblioteka.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.configuration.DatabaseConfig;
import pl.edu.wszib.biblioteka.exceptions.*;
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
    public void rentBook(int bookId, int userId) {
        String checkSql = "SELECT rent FROM books WHERE id = ?";
        String checkReservationsSql = "SELECT user_id FROM reservations WHERE book_id = ? ORDER BY reservation_date ASC LIMIT 1";
        String deleteReservationSql = "DELETE FROM reservations WHERE book_id = ? AND user_id = ?";
        String updateBookSql = "UPDATE books SET rent = TRUE WHERE id = ?";
        String insertRentalSql = "INSERT INTO rentals (user_id, book_id) VALUES (?, ?)";

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            
            try {
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, bookId);
                    ResultSet resultSet = checkStmt.executeQuery();

                    if (resultSet.next()) {
                        boolean isRented = resultSet.getBoolean("rent");
                        if (isRented) {
                            throw new CanNotRentBookEx();
                        }
                    } else {
                        throw new CanNotRentBookEx(); // Książka nie istnieje
                    }
                }

                try (PreparedStatement resStmt = connection.prepareStatement(checkReservationsSql)) {
                    resStmt.setInt(1, bookId);
                    ResultSet resSet = resStmt.executeQuery();
                    
                    if (resSet.next()) {
                        int reservingUserId = resSet.getInt("user_id");
                        if (reservingUserId != userId) {
                            // Ktoś inny zarezerwował
                            throw new BookReservedBySomeoneElseEx();
                        }

                        try (PreparedStatement delResStmt = connection.prepareStatement(deleteReservationSql)) {
                            delResStmt.setInt(1, bookId);
                            delResStmt.setInt(2, userId);
                            delResStmt.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement updateStmt = connection.prepareStatement(updateBookSql)) {
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }

                try (PreparedStatement insertStmt = connection.prepareStatement(insertRentalSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, bookId);
                    insertStmt.executeUpdate();
                }
                
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(int bookId) {
        String updateBookSql = "UPDATE books SET rent = FALSE WHERE id = ?";
        String updateRentalSql = "UPDATE rentals SET return_date = CURRENT_TIMESTAMP WHERE book_id = ? AND return_date IS NULL";

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            
            try {
                try (PreparedStatement updateStmt = connection.prepareStatement(updateBookSql)) {
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }

                try (PreparedStatement updateRentalStmt = connection.prepareStatement(updateRentalSql)) {
                    updateRentalStmt.setInt(1, bookId);
                    updateRentalStmt.executeUpdate();
                }
                
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
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

    @Override
    public List<Book> getBooksByUserId(int userId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b JOIN rentals r ON b.id = r.book_id WHERE r.user_id = ? AND r.return_date IS NULL";
        
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<String> getRentalHistory(int userId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT b.title, r.rent_date, r.return_date " +
                     "FROM rentals r JOIN books b ON r.book_id = b.id " +
                     "WHERE r.user_id = ? ORDER BY r.rent_date DESC";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                Timestamp rentDate = resultSet.getTimestamp("rent_date");
                Timestamp returnDate = resultSet.getTimestamp("return_date");
                
                String status = (returnDate == null) ? "RENTED" : "RETURNED on " + returnDate;
                history.add(String.format("'%s' (Rented: %s, Status: %s)", title, rentDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void reserveBook(int bookId, int userId) {
        String checkRentSql = "SELECT rent FROM books WHERE id = ?";
        String checkResSql = "SELECT id FROM reservations WHERE book_id = ? AND user_id = ?";
        String checkHeldSql = "SELECT id FROM rentals WHERE book_id = ? AND user_id = ? AND return_date IS NULL";
        String insertResSql = "INSERT INTO reservations (user_id, book_id) VALUES (?, ?)";

        try (Connection connection = databaseConfig.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(checkRentSql)) {
                stmt.setInt(1, bookId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    if (!rs.getBoolean("rent")) {
                        throw new BookNotRentedEx();
                    }
                } else {
                    throw new CanNotFindBookByIDEx();
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(checkResSql)) {
                stmt.setInt(1, bookId);
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    throw new BookAlreadyReservedEx();
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(checkHeldSql)) {
                stmt.setInt(1, bookId);
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    throw new BookAlreadyHeldByYouEx();
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(insertResSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, bookId);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> checkReservations(int userId) {
        List<String> notifications = new ArrayList<>();
        String myReservationsSql = "SELECT b.id, b.title, b.rent FROM reservations r JOIN books b ON r.book_id = b.id WHERE r.user_id = ?";
        String queueSql = "SELECT user_id FROM reservations WHERE book_id = ? ORDER BY reservation_date ASC LIMIT 1";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(myReservationsSql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id");
                String title = rs.getString("title");
                boolean isRented = rs.getBoolean("rent");

                if (!isRented) {
                    try (PreparedStatement qStmt = connection.prepareStatement(queueSql)) {
                        qStmt.setInt(1, bookId);
                        ResultSet qRs = qStmt.executeQuery();
                        if (qRs.next()) {
                            int firstUserId = qRs.getInt("user_id");
                            if (firstUserId == userId) {
                                notifications.add("Book '" + title + "' is now available for you!");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
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
