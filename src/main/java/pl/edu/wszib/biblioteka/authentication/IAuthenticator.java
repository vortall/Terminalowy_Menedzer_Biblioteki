package pl.edu.wszib.biblioteka.authentication;

import pl.edu.wszib.biblioteka.model.User;

public interface IAuthenticator {
    User authenticate(String username, String password);
}
