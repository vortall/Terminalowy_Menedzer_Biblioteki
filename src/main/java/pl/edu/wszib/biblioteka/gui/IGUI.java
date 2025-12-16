package pl.edu.wszib.biblioteka.gui;

import pl.edu.wszib.biblioteka.exceptions.InvalidBookInputEx;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.Role;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

public interface IGUI {
    User readLoginAndPassword();
    void listBooks(List<Book> books);
    Book readNewBook() throws InvalidBookInputEx;
    int readBook();
    String readBookAuthor();
    String readBookTitle();
    String showMenuAndReadChoose(Role role);
    void showFindAuthorFailMessage();
    void showFindTitleFailMessage();
    void showRentSuccessMessage(boolean success);
    void showReturnSuccessMessage(boolean success);
    void showAddingSuccessMessage(boolean success);
    void showRemovalSuccessMessage(boolean success);
    void showUpdateSuccessMessage(boolean success);
    void showWrongOptionMessage();



}

