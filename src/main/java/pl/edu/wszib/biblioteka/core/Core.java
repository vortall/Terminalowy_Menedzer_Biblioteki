package pl.edu.wszib.biblioteka.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.authentication.IAuthenticator;
import pl.edu.wszib.biblioteka.database.IBookRepository;
import pl.edu.wszib.biblioteka.exceptions.*;
import pl.edu.wszib.biblioteka.gui.IGUI;
import pl.edu.wszib.biblioteka.model.User;
import pl.edu.wszib.biblioteka.model.Role;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Core implements ICore{
    private final IBookRepository bookRepository;
    private final IGUI gui;
    private final IAuthenticator authenticator;

    @Override
    public void run() {
        User authenticatedUser = null;
        while (authenticatedUser == null) {
            User user = gui.readLoginAndPassword();
            authenticatedUser = authenticator.authenticate(
                    user.getUsername(),
                    user.getPassword());

            if (authenticatedUser == null) {
                gui.showWrongOptionMessage();
            }

            else if (authenticatedUser.getRole() == Role.USER) {
                List<String> notifications = bookRepository.checkReservations(authenticatedUser.getId());
                gui.showNotifications(notifications);
                
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
                                bookRepository.rentBook(gui.readBook(), authenticatedUser.getId());
                                gui.showRentSuccessMessage(true);
                            } catch (InvalidBookInputEx e) {
                                gui.showRentSuccessMessage(false);
                            } catch (CanNotRentBookEx e) {
                                System.out.println("Book is already rented or does not exist.");
                            } catch (BookReservedBySomeoneElseEx e) {
                                System.out.println("Book is reserved by someone else!");
                            }
                            break;
                        case "5":
                            try {
                                bookRepository.returnBook(gui.readBook());
                                gui.showReturnSuccessMessage(true);
                            } catch (InvalidBookInputEx | CanNotRentBookEx e) {
                                gui.showReturnSuccessMessage(false);
                            }
                            break;
                        case "6":
                            gui.listBooks(bookRepository.getBooksByUserId(authenticatedUser.getId()));
                            break;
                        case "7":
                            gui.showRentalHistory(bookRepository.getRentalHistory(authenticatedUser.getId()));
                            break;
                        case "8":
                            try {
                                bookRepository.reserveBook(gui.readBook(), authenticatedUser.getId());
                                gui.showReservationSuccessMessage(true);
                            } catch (InvalidBookInputEx e) {
                                gui.showReservationSuccessMessage(false);
                            } catch (BookNotRentedEx e) {
                                gui.showReservationFailMessage("Book is available, you can rent it directly.");
                            } catch (BookAlreadyReservedEx e) {
                                gui.showReservationFailMessage("You have already reserved this book.");
                            } catch (BookAlreadyHeldByYouEx e) {
                                gui.showReservationFailMessage("You already have this book!");
                            } catch (CanNotFindBookByIDEx e) {
                                gui.showReservationFailMessage("Book not found.");
                            }
                            break;
                        case "9":
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
                            try {
                                System.out.println("Add new book");
                                bookRepository.addBook(gui.readNewBook());
                                gui.showAddingSuccessMessage(true);
                            } catch (InvalidBookInputEx e) {
                                gui.showAddingSuccessMessage(false);
                            }
                            break;
                        case "3":
                            try {
                                bookRepository.removeBook(gui.readBook());
                                gui.showRemovalSuccessMessage(true);
                            } catch (InvalidBookInputEx | CanNotFindBookByIDEx e) {
                                gui.showRemovalSuccessMessage(false);
                            }
                            break;
                        case "4":
                            try {
                                System.out.println("Update book");
                                bookRepository.updateBook(gui.readNewBook());
                                gui.showUpdateSuccessMessage(true);
                            } catch (InvalidBookInputEx | CanNotFindBookByIDEx e) {
                                gui.showUpdateSuccessMessage(false);
                            }
                            break;
                        case "5":
                            gui.showStatistics(bookRepository.getStatistics());
                            break;
                        case "6":
                            return;
                        default:
                            gui.showWrongOptionMessage();
                            break;
                    }
                }
            }
        }
    }
}