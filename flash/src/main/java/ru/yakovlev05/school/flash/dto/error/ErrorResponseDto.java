package ru.yakovlev05.school.flash.dto.error;

import java.time.Instant;
import java.util.List;

public record ErrorResponseDto(
        Instant timestamp,
        Integer status,
        String error,
        String path,
        List<ViolationConstraintDto> violations
) {
}
