package ru.skypro.homework.exception;

public class UserAlredyExsistException extends RuntimeException{
    public UserAlredyExsistException() {
    }

    public UserAlredyExsistException(String message) {
        super(message);
    }

    public UserAlredyExsistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlredyExsistException(Throwable cause) {
        super(cause);
    }

    public UserAlredyExsistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
