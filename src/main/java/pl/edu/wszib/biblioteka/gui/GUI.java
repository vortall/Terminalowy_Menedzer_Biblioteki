package pl.edu.wszib.biblioteka.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.LibraryStatistics;
import pl.edu.wszib.biblioteka.model.User;
import pl.edu.wszib.biblioteka.model.Role;
import pl.edu.wszib.biblioteka.exceptions.InvalidBookInputEx;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;

    @Override
    public String showMenuAndReadChoose(Role role) {
        System.out.println("\nWelcome to Biblioteka");

        if (role == Role.USER){
            System.out.println("1. List all books");
            System.out.println("2. List books by author");
            System.out.println("3. List books by title");
            System.out.println("4. Rent book");
            System.out.println("5. Return book");
            System.out.println("6. Show my rented books");
            System.out.println("7. Show rental history");
            System.out.println("8. Reserve book");
            System.out.println("9. Exit");
        }
        else if (role == Role.ADMIN){
            System.out.println("1. List all books");
            System.out.println("2. Add book");
            System.out.println("3. Remove book");
            System.out.println("4. Update book");
            System.out.println("5. Show statistics");
            System.out.println("6. Exit");
        }

        return this.scanner.nextLine();
    }

    @Override
    public void listBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    @Override
    public void showFindAuthorFailMessage(){
        System.out.println("No books found for the given author.");
    }

    @Override
    public void showFindTitleFailMessage(){
        System.out.println("No books found for the given title.");
    }

    @Override
    public void showRentSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "Book rented successfully." :
                        "Cannot rent the book.");
    }

    @Override
    public void showReturnSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "Book returned successfully." :
                        "Cannot return the book.");
    }

    public void showAddingSuccessMessage(boolean success){
        System.out.println(
                success ?
                        "Book added successfully." :
                        "Cannot add the book.");
    }

    public void showRemovalSuccessMessage(boolean success){
        System.out.println(
                success ?
                        "Book removed successfully." :
                        "Cannot remove the book.");
    }

    public void showUpdateSuccessMessage(boolean success){
        System.out.println(
                success ?
                        "Book updated successfully." :
                        "Cannot update the book.");
    }



    @Override
    public void showWrongOptionMessage() {
        System.out.println("Wrong option. Please try again.");
    }



    @Override
    public User readLoginAndPassword() {
        System.out.println("Login: ");
        String login = this.scanner.nextLine();
        System.out.println("Password: ");
        String password = this.scanner.nextLine();
        return new User(login, password);
    }

    @Override
    public int readBook() throws InvalidBookInputEx {
        System.out.println("Book ID:");
        try{
            int id = this.scanner.nextInt();
            this.scanner.nextLine();
            return id;
        } catch (InputMismatchException e){
            this.scanner.nextLine();
            throw new InvalidBookInputEx();
        }
    }

    @Override
    public String readBookAuthor() {
        System.out.println("Author: ");
        return this.scanner.nextLine();
    }

    @Override
    public String readBookTitle() {
        System.out.println("Title: ");
        return this.scanner.nextLine();
    }

    @Override
    public Book readNewBook() throws InvalidBookInputEx {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Title: ");
            String title = scanner.nextLine();

            System.out.print("Author: ");
            String author = scanner.nextLine();

            System.out.print("ISBN: ");
            int isbn = Integer.parseInt(scanner.nextLine());

            System.out.print("Release year: ");
            int year = Integer.parseInt(scanner.nextLine());

            return Book.builder()
                    .book_id(id)
                    .title(title)
                    .author(author)
                    .isbn_number(isbn)
                    .release_year(year)
                    .rent(false)
                    .build();

        } catch (NumberFormatException e) {
            throw new InvalidBookInputEx();
        }
    }

    @Override
    public void showRentalHistory(List<String> history) {
        if (history.isEmpty()) {
            System.out.println("No rental history found.");
        } else {
            System.out.println("Rental History:");
            for (String entry : history) {
                System.out.println(entry);
            }
        }
    }

    @Override
    public void showReservationSuccessMessage(boolean success) {
        System.out.println(success ? "Book reserved successfully." : "Cannot reserve book.");
    }

    @Override
    public void showReservationFailMessage(String reason) {
        System.out.println("Cannot reserve book: " + reason);
    }

    @Override
    public void showNotifications(List<String> notifications) {
        if (!notifications.isEmpty()) {
            System.out.println("\n--- NOTIFICATIONS ---");
            for (String note : notifications) {
                System.out.println(note);
            }
            System.out.println("---------------------\n");
        }
    }

    @Override
    public void showStatistics(LibraryStatistics stats) {
        System.out.println("\n--- LIBRARY STATISTICS ---");
        System.out.println("Total books: " + stats.getTotalBooks());
        System.out.println("Currently rented books: " + stats.getRentedBooks());
        System.out.println("Active users (with rented books): " + stats.getActiveUsersCount());
        
        System.out.println("\nMost Popular Books:");
        if (stats.getMostPopularBooks().isEmpty()) {
            System.out.println("- No data");
        } else {
            for (String book : stats.getMostPopularBooks()) {
                System.out.println("- " + book);
            }
        }
        System.out.println("--------------------------\n");
    }
}