package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.NotNull;

public record CreateCalendarDto(
        @NotNull String name
) {
}
