package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record RegistrationRequest(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull @Email String email,
        @Past LocalDate birthDate,
        @NotNull String password
) {
}