package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

public interface IUserRepository {
    User findUserByLogin(String login);
    List<User> getUsers();
}