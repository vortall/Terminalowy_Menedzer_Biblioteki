package pl.edu.wszib.biblioteka.database;

import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByAuthorEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByTitleEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookEx;
import pl.edu.wszib.biblioteka.model.Book;

import java.util.ArrayList;
import java.util.List;


@Component
public class BookRepository implements IBookRepository {
    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        this.books.add(new Book(1, "W pustyni i w puszczy", "Henryk Sienkiewicz", 1001, 1911));
        this.books.add(new Book(2, "Pan Tadeusz", "Adam Mickiewicz", 1002, 1834));
        this.books.add(new Book(3, "Lalka", "Bolesław Prus", 1003, 1890));
        this.books.add(new Book(4, "Krzyżacy", "Henryk Sienkiewicz", 1004, 1900));
        this.books.add(new Book(5, "Ferdydurke", "Witold Gombrowicz", 1005, 1937));
        this.books.add(new Book(6, "Solaris", "Stanisław Lem", 1006, 1961));
        this.books.add(new Book(7, "Dziady", "Adam Mickiewicz", 1007, 1823));
        this.books.add(new Book(8, "Zbrodnia i kara", "Fiodor Dostojewski", 1008, 1866));
        this.books.add(new Book(9, "Rok 1984", "George Orwell", 1009, 1949));
        this.books.add(new Book(10, "Hobbit", "J.R.R. Tolkien", 1010, 1937));
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();

        for (Book book : this.books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        if (result.isEmpty()) {
            throw new CanNotFindBookByAuthorEx();
        }
        return result;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();

        for (Book book : this.books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                result.add(book);
            }
        }
        if (result.isEmpty()) {
            throw new CanNotFindBookByTitleEx();
        }
        return result;
    }

    @Override
    public void rentBook(int id) {
        for (Book book : this.books) {
            if (book.getBook_id() == id && !book.isRent()) {
                book.setRent(true);
                return;
            }
        }

        throw new CanNotRentBookEx();
    }

    @Override
    public void returnBook(int id) {
        for (Book book : this.books) {
            if (book.getBook_id() == id && book.isRent()) {
                book.setRent(false);
                return;
            }
        }
    }




}
