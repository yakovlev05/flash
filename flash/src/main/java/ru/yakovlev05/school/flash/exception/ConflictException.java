package ru.yakovlev05.school.flash.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String format, Object... args) {
        super(String.format(format, args));
    }
}
