package ru.yakovlev05.school.flash.exception.handler;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
