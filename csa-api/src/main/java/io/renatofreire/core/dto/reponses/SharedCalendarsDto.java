package io.renatofreire.core.dto.reponses;

import io.renatofreire.core.enums.CalendarPermissions;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link io.renatofreire.core.model.SharedCalendars}
 */
public record SharedCalendarsDto(UUID idCalendarId, String idCalendarName, String idUserEmail, CalendarPermissions permission) implements Serializable {
  }