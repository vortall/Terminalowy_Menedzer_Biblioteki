package pl.edu.wszib.biblioteka.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class LibraryStatistics {
    private int totalBooks;
    private int rentedBooks;
    private List<String> mostPopularBooks;
    private int activeUsersCount;
}
