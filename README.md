# Terminalowy Menedżer Biblioteki z SQL

Aplikacja konsolowa do zarządzania biblioteką, wykorzystująca bazę danych H2 oraz JDBC. Projekt zrealizowany w ramach zadania edukacyjnego.

## Funkcjonalności

### Podstawowe (z Zadania 1)
*   Logowanie użytkowników (USER / ADMIN).
*   Przeglądanie listy książek.
*   Wyszukiwanie książek po tytule i autorze.
*   Dodawanie, usuwanie i edycja książek (tylko ADMIN).

### Nowe funkcjonalności (z Zadania 3)
1.  **System wypożyczeń:**
    *   Użytkownik może wypożyczyć książkę.
    *   Użytkownik widzi listę swoich aktualnie wypożyczonych książek.
    *   Historia wypożyczeń (data wypożyczenia i zwrotu).
2.  **System rezerwacji:**
    *   Możliwość rezerwacji wypożyczonej książki.
    *   Kolejka rezerwacji (FIFO).
    *   Powiadomienia o dostępności zarezerwowanej książki przy logowaniu.
    *   Blokada wypożyczenia dla innych osób, jeśli książka jest zarezerwowana.
3.  **Statystyki biblioteki (dla ADMINA):**
    *   Liczba wszystkich książek.
    *   Liczba aktualnie wypożyczonych książek.
    *   Ranking najpopularniejszych książek.
    *   Liczba aktywnych użytkowników (posiadających wypożyczone książki).

## Technologie
*   **Java 23**
*   **Spring Framework** (Context, DI)
*   **JDBC** (Java Database Connectivity)
*   **H2 Database** (baza danych in-memory/file-based)
*   **Lombok**
*   **Maven**

## Wymagania
*   JDK 23
*   Maven

## Instrukcja uruchomienia (IntelliJ IDEA)

1.  Otwórz folder projektu w **IntelliJ IDEA**.
2.  Poczekaj, aż Maven pobierze niezbędne biblioteki (pasek postępu w prawym dolnym rogu).
3.  W drzewie projektu po lewej stronie przejdź do:
    `src` -> `main` -> `java` -> `pl.edu.wszib.biblioteka`
4.  Kliknij prawym przyciskiem myszy na plik **`App.java`**.
5.  Wybierz opcję **`Run 'App.main()'`** (zielona strzałka).

## Dane logowania

Aplikacja automatycznie tworzy dwóch użytkowników przy pierwszym uruchomieniu:

| Rola  | Login | Hasło |
|-------|-------|-------|
| ADMIN | admin | admin |
| USER  | user  | user  |

## Struktura bazy danych

*   `users` - użytkownicy i ich role.
*   `books` - książki i ich status (rent).
*   `rentals` - historia wypożyczeń i aktualne wypożyczenia.
*   `reservations` - aktywne rezerwacje książek.
