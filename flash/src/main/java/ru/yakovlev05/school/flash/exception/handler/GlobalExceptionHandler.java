package ru.yakovlev05.school.flash.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yakovlev05.school.flash.dto.error.ErrorResponseDto;
import ru.yakovlev05.school.flash.dto.error.ViolationConstraintDto;
import ru.yakovlev05.school.flash.exception.ConflictException;
import ru.yakovlev05.school.flash.exception.ForbiddenException;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.exception.UnauthorizedException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Hidden // Чтобы в сваггере не отображался как вариант ответа
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseDto handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception. Internal server error", ex);
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                request.getServletPath(),
                null
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Входные данные не соответствуют заданным ограничениям",
                request.getServletPath(),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> new ViolationConstraintDto(error.getField(), error.getDefaultMessage()))
                        .toList()
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponseDto handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getServletPath(),
                null
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponseDto handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getServletPath(),
                null
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponseDto handleConflictException(ConflictException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getServletPath(),
                null
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponseDto handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getServletPath(),
                null
        );
    }
}
