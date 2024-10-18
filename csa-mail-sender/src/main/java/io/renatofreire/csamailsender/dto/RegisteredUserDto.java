package io.renatofreire.csamailsender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisteredUserDto(
        @NotNull String name,
        @Email String email,
        @NotNull String validationToken
) {
}
