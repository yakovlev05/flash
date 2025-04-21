package ru.yakovlev05.school.flash.exception.handler;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String format, Object... args) {
        super(String.format(format, args));
    }
}
