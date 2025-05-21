package ru.yakovlev05.school.flash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.yakovlev05.school.flash.validation.BannedUsernames;

@Schema(description = "Запрос на регистрацию")
public record RegistrationRequest(
        @Schema(description = "Имя пользователя", example = "andrey")
        @BannedUsernames
        @Size(min = 4, message = "длина имени пользователя должна быть не менее 4")
        String username,

        @Schema(description = "Email пользователя", example = "andrey@mail.ru")
        @Email
        String email,

        @Schema(description = "Пароль", example = "#MyPassword")
        @Pattern(regexp = ".*[~@\"#№$;%^:&?*()<>/+=-_{}\\[\\]].*",
                message = "пароль должен содержать специальные символы")
        @Size(min = 8, message = "длина пароля должны быть не менее 8 символов")
        String password
) {
}
