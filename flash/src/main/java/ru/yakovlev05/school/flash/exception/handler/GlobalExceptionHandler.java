package ru.yakovlev05.school.flash.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yakovlev05.school.flash.dto.InternalErrorDto;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public InternalErrorDto handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception. Internal server error", ex);
        return new InternalErrorDto(
                new Date(System.currentTimeMillis()),
                500,
                "Internal Server Error",
                request.getRequestURI()
        );
    }

}
