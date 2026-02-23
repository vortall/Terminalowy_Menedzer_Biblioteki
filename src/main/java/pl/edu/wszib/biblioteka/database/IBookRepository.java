package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.LibraryStatistics;

import java.util.List;

public interface IBookRepository {
    List<Book> getBooks();
    List<Book> getBooksByAuthor(String author);
    List<Book> getBooksByTitle(String title);
    void rentBook(int book_id, int user_id);
    void returnBook(int book_id);
    void addBook(Book book);
    void removeBook(int book_id);
    void updateBook(Book book);
    List<Book> getBooksByUserId(int userId);
    List<String> getRentalHistory(int userId);
    void reserveBook(int bookId, int userId);
    List<String> checkReservations(int userId);
    LibraryStatistics getStatistics();
}
