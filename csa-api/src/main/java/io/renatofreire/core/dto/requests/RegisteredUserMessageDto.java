package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisteredUserMessageDto(
        @NotNull String name,
        @Email String email,
        @NotNull String validationToken
) {
}
