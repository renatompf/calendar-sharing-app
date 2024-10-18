package io.renatofreire.core.model;

import io.renatofreire.core.enums.CalendarPermissions;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "shared_calendars", indexes = {
        @Index(name = "idx_shared_calendar", columnList = "calendar_id"),
        @Index(name = "idx_shared_user", columnList = "user_id")
})
public class SharedCalendars {

    @EmbeddedId
    private SharedCalendarPK id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CalendarPermissions permission;

    public SharedCalendars(){}

    public SharedCalendars(SharedCalendarPK id, CalendarPermissions permission) {
        this.id = id;
        this.permission = permission;
    }

    public SharedCalendarPK getId() {
        return id;
    }

    public void setId(SharedCalendarPK id) {
        this.id = id;
    }

    public CalendarPermissions getPermission() {
        return permission;
    }

    public void setPermission(CalendarPermissions permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedCalendars that = (SharedCalendars) o;
        return Objects.equals(id, that.id) && permission == that.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permission);
    }

}
