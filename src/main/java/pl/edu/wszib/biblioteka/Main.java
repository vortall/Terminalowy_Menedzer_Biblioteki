package pl.edu.wszib.biblioteka;

import pl.edu.wszib.biblioteka.model.Book;

public class Main {
    public static void main(String[] args) {
        Book book1 = new Book( 1, "W pustyni i puszczy", "Henio Sienkiewicz", 123, 1980);
        System.out.println(book1.toString());

    }
}
