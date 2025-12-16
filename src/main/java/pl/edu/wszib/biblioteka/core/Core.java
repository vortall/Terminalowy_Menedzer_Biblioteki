package pl.edu.wszib.biblioteka.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.authentication.IAuthenticator;
import pl.edu.wszib.biblioteka.database.IBookRepository;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookEx;
import pl.edu.wszib.biblioteka.gui.IGUI;
import pl.edu.wszib.biblioteka.model.User;

@Component
@RequiredArgsConstructor
public class Core implements ICore{
    private final IBookRepository bookRepository;
    private final IGUI gui;
    private final IAuthenticator authenticator;

    @Override
    public void run() {
        User user = gui.readLoginAndPassword();
        boolean isAuthenticated = authenticator.authenticate(
                user.getUsername(),
                user.getPassword());

        while(isAuthenticated) {
            switch (gui.showMenuAndReadChoose()) {
                case "1":
                    gui.listBooks(bookRepository.getBooks());
                    break;
                case "2":
                    try {
                        bookRepository.rentBook(gui.readBook());
                        gui.showRentSuccessMessage(true);
                    } catch (CanNotRentBookEx e) {
                        gui.showRentSuccessMessage(false);
                    }
                    break;
                case "3":
                    return;
                default:
                    gui.showWrongOptionMessage();
                    break;
            }
        }
    }
}