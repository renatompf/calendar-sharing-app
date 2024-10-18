package io.renatofreire.core.dto.reponses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link io.renatofreire.core.model.Event}
 */
public record EventDto(UUID id, String title, String description, LocalDateTime startTime, LocalDateTime endTime,
                       String calendarName) implements Serializable {
}