package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.model.Book;

import java.util.List;

public interface IBookRepository {
    List<Book> getBooks();
    List<Book> getBooksByAuthor(String author);
    List<Book> getBooksByTitle(String title);
    void rentBook(int book_id);
    void returnBook(int book_id);


}
