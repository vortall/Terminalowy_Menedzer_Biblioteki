package pl.edu.wszib.biblioteka.authentication;

public interface IAuthenticator {
    boolean authenticate(String username, String password);
}
