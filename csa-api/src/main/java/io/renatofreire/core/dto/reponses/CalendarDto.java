package io.renatofreire.core.dto.reponses;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link io.renatofreire.core.model.Calendar}
 */
public record CalendarDto(UUID id, String name, String ownerFirstName, String ownerLastName) implements Serializable {
  }