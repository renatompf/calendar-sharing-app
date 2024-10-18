package io.renatofreire.csamailsender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShareCalendarMessageDto(
        @NotBlank String calendarName,
        @NotBlank String ownerId,
        @NotBlank @Email String sharedWithEmail,
        @NotBlank String sharedWithName,
        @NotBlank String token,
        @NotNull CalendarSharingMessageType type
) {
}
