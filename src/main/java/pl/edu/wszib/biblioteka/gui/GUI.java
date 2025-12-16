package pl.edu.wszib.biblioteka.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;

    @Override
    public String showMenuAndReadChoose() {
        System.out.println("1. List books");
        System.out.println("2. Rent book");
        System.out.println("3. Exit");

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
        return new User(login, this.scanner.nextLine());
    }

    @Override
    public int readBook() {
        System.out.println("Book ID:");

        return this.scanner.nextInt();
    }


}