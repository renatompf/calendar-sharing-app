package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventDto(
        @NotBlank(message = "Title is mandatory")
        String title,

        String description,

        @NotNull(message = "Start time is mandatory")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "End time is mandatory")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime,

        @NotNull(message = "Calendar ID is mandatory")
        UUID calendarId
) {
}
