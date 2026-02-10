package pl.edu.wszib.biblioteka.gui;

import pl.edu.wszib.biblioteka.exceptions.InvalidBookInputEx;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.Role;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

public interface IGUI {
    String showMenuAndReadChoose(Role role);
    void listBooks(List<Book> books);
    void showFindAuthorFailMessage();
    void showFindTitleFailMessage();
    void showRentSuccessMessage(boolean success);
    void showReturnSuccessMessage(boolean success);
    void showAddingSuccessMessage(boolean success);
    void showRemovalSuccessMessage(boolean success);
    void showUpdateSuccessMessage(boolean success);
    void showWrongOptionMessage();
    void showRentalHistory(List<String> history);
    User readLoginAndPassword();
    int readBook() throws InvalidBookInputEx;
    String readBookAuthor();
    String readBookTitle();
    Book readNewBook() throws InvalidBookInputEx;
}
