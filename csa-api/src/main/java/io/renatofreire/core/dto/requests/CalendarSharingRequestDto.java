package io.renatofreire.core.dto.requests;

import io.renatofreire.core.enums.CalendarPermissions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CalendarSharingRequestDto(
        @NotNull UUID calendarId,
        @NotBlank @Email String sharedWith,
        @NotNull CalendarPermissions calendarPermissions
        ) {
}
