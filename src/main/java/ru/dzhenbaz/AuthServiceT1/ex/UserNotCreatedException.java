package ru.dzhenbaz.AuthServiceT1.ex;

public class UserNotCreatedException extends RuntimeException {

    public UserNotCreatedException(String message) {
        super(message);
    }
}
