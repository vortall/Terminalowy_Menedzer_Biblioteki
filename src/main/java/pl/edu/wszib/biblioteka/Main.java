package pl.edu.wszib.biblioteka;

import pl.edu.wszib.biblioteka.database.BookRepository;
import pl.edu.wszib.biblioteka.model.Book;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepository();


        bookRepository.rentBook(1);
        bookRepository.returnBook(1);
        bookRepository.getBooks();

    }
}
