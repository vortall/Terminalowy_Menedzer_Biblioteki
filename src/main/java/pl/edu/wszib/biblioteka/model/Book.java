package pl.edu.wszib.biblioteka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Book {
    private int book_id;
    private String title;
    private String author;
    private int isbn_number;
    private int release_year;
    private boolean rent;

    public Book() {}

    public Book(int book_id, String title, String author, int isbn_number, int release_year) {
        this(book_id, title, author, isbn_number, release_year, false);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.getBook_id())
                .append(" ")
                .append(this.getTitle())
                .append(" ")
                .append(this.getAuthor())
                .append(" ")
                .append(this.getIsbn_number())
                .append(" ")
                .append(this.getRelease_year())
                .append(" ")
                .append(this.isRent() ? "RENTED" : "AVAILABLE")
                .toString();
    }



}
