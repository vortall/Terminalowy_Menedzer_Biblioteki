package pl.edu.wszib.biblioteka.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.authentication.IAuthenticator;
import pl.edu.wszib.biblioteka.database.IBookRepository;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByAuthorEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotFindBookByTitleEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookEx;
import pl.edu.wszib.biblioteka.exceptions.CanNotReturnBookEx;
import pl.edu.wszib.biblioteka.gui.IGUI;
import pl.edu.wszib.biblioteka.model.User;
import pl.edu.wszib.biblioteka.model.Role;

@Component
@RequiredArgsConstructor
public class Core implements ICore{
    private final IBookRepository bookRepository;
    private final IGUI gui;
    private final IAuthenticator authenticator;

    @Override
    public void run() {
        User user = gui.readLoginAndPassword();
        User authenticatedUser = authenticator.authenticate(
                user.getUsername(),
                user.getPassword());

        if (authenticatedUser == null) {
            gui.showWrongOptionMessage();
            return;
        }

        if (authenticatedUser.getRole() == Role.USER) {
            while(true) {

                switch (gui.showMenuAndReadChoose(authenticatedUser.getRole())) {
                    case "1":
                        gui.listBooks(bookRepository.getBooks());
                        break;
                    case "2":
                        try {
                            gui.listBooks(bookRepository.getBooksByAuthor(gui.readBookAuthor()));
                        } catch (CanNotFindBookByAuthorEx e) {
                            gui.showFindAuthorFailMessage();
                        }
                        break;
                    case "3":
                        try {
                            gui.listBooks(bookRepository.getBooksByTitle(gui.readBookTitle()));
                        } catch (CanNotFindBookByTitleEx e) {
                            gui.showFindTitleFailMessage();
                        }
                            break;
                    case "4":
                        try {
                            bookRepository.rentBook(gui.readBook());
                            gui.showRentSuccessMessage(true);
                        } catch (CanNotRentBookEx e) {
                            gui.showRentSuccessMessage(false);
                        }
                        break;
                    case "5":
                        try {
                            bookRepository.returnBook(gui.readBook());
                            gui.showReturnSuccessMessage(true);
                        } catch (CanNotReturnBookEx e) {
                            gui.showReturnSuccessMessage(false);
                        }
                        break;
                    case "6":
                        return;
                    default:
                        gui.showWrongOptionMessage();
                        break;
                }
            }
        }

        else if (authenticatedUser.getRole() == Role.ADMIN) {
            while(true) {

                switch (gui.showMenuAndReadChoose(authenticatedUser.getRole())) {
                    case "1":
                        gui.listBooks(bookRepository.getBooks());
                        break;
                    case "2":
                        System.out.println("Add new book");
                        bookRepository.addBook(gui.readNewBook());
                        break;
                    case "3":
                        bookRepository.removeBook(gui.readBook());
                        break;
                    case "4":
                        System.out.println("Update book");
                        bookRepository.updateBook(gui.readNewBook());
                        break;
                    case "5":
                        return;
                    default:
                        gui.showWrongOptionMessage();
                        break;
                }
            }
        }

    }
}