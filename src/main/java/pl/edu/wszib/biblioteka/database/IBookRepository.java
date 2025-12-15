package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.model.Book;

import java.util.List;

public interface IBookRepository {
    void rentBook(int book_id);
    void returnBook(int book_id);
    void getBooks();
}
