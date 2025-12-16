package pl.edu.wszib.biblioteka.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.User;
import pl.edu.wszib.biblioteka.model.Role;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;

    @Override
    public String showMenuAndReadChoose(Role role) {
        System.out.println("Welcome to Biblioteka");

        if (role == Role.USER){
            System.out.println("1. List all books");
            System.out.println("2. List books by author");
            System.out.println("3. List books by title");
            System.out.println("4. Rent book");
            System.out.println("5. Return book");
            System.out.println("6. Exit");
        }
        else if (role == Role.ADMIN){
            System.out.println("1. List all books");
            System.out.println("2. Add book");
            System.out.println("3. Remove book");
            System.out.println("4. Edit book");
            System.out.println("5. Exit");
        }

        return this.scanner.nextLine();
    }

    @Override
    public void listBooks(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @Override
    public void showRentSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "Book rented successfully." :
                        "Cannot rent the book.");
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
    public int readBook() {
        System.out.println("Book ID:");

        return this.scanner.nextInt();
    }


}