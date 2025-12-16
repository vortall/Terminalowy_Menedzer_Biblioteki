package pl.edu.wszib.biblioteka.gui;

import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.Role;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

public interface IGUI {
    String showMenuAndReadChoose(Role role);
    void listBooks(List<Book> books);
    void showRentSuccessMessage(boolean success);
    void showReturnSuccessMessage(boolean success);

    void showWrongOptionMessage();
    User readLoginAndPassword();
    int readBook();
    String readBookAuthor();
    String readBookTitle();

}

