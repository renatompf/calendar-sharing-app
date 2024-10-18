package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CalendarSharingDeletionDto(
        @NotNull UUID calendarId,
        @NotBlank @Email String email
) {
}
