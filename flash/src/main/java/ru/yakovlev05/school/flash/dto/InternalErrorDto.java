package ru.yakovlev05.school.flash.dto;

import java.util.Date;

public record InternalErrorDto(Date timestamp, int status, String error, String path) {
}
