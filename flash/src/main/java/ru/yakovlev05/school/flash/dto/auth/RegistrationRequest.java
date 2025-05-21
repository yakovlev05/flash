package ru.yakovlev05.school.flash.dto.auth;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.yakovlev05.school.flash.validation.BannedUsernames;

public record RegistrationRequest(
        @BannedUsernames
        @Size(min = 4, message = "длина имени пользователя должна быть не менее 4")
        String username,

        @Pattern(regexp = ".*[~@\"#№$;%^:&?*()<>/+=-_{}\\[\\]].*",
                message = "пароль должен содержать специальные символы")
        @Size(min = 8, message = "длина пароля должны быть не менее 8 символов")
        String password
) {
}
