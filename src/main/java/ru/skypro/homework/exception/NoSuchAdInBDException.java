package ru.skypro.homework.exception;

public class NoSuchAdInBDException extends RuntimeException{
    public NoSuchAdInBDException() {
        System.out.println("Ошибка: объявления с таким id в базе данных не найдено.");
    }

    public NoSuchAdInBDException(String message) {
        super(message);
    }

    public NoSuchAdInBDException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchAdInBDException(Throwable cause) {
        super(cause);
    }

    public NoSuchAdInBDException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
