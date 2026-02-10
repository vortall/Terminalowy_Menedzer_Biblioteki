CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    release_year INT,
    rent BOOLEAN DEFAULT FALSE
);

-- Wstawianie użytkowników
INSERT INTO users (username, password, role)
SELECT 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password, role)
SELECT 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

-- Wstawianie książek
INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'W pustyni i w puszczy', 'Henryk Sienkiewicz', '1001', 1911, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'W pustyni i w puszczy');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Pan Tadeusz', 'Adam Mickiewicz', '1002', 1834, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Pan Tadeusz');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Lalka', 'Bolesław Prus', '1003', 1890, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Lalka');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Krzyżacy', 'Henryk Sienkiewicz', '1004', 1900, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Krzyżacy');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Ferdydurke', 'Witold Gombrowicz', '1005', 1937, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Ferdydurke');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Solaris', 'Stanisław Lem', '1006', 1961, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Solaris');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Dziady', 'Adam Mickiewicz', '1007', 1823, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Dziady');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Zbrodnia i kara', 'Fiodor Dostojewski', '1008', 1866, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Zbrodnia i kara');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Rok 1984', 'George Orwell', '1009', 1949, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Rok 1984');

INSERT INTO books (title, author, isbn, release_year, rent)
SELECT 'Hobbit', 'J.R.R. Tolkien', '1010', 1937, FALSE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Hobbit');
