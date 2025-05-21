package ru.yakovlev05.school.flash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ одноразового билета")
public record TicketResponse(
        @Schema(example = "db11be36-bd67-4192-a06b-f6d8d8dae90b")
        String value
) {
}
