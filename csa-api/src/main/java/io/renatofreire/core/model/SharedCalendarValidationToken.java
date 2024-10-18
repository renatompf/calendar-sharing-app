package io.renatofreire.core.model;

import io.renatofreire.core.enums.CalendarPermissions;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "shared_calendar_validation_token")
public class SharedCalendarValidationToken {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "permissions")
    @Enumerated(EnumType.STRING)
    private CalendarPermissions calendarPermissions;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shared_with")
    private User sharedWith;

    public SharedCalendarValidationToken(){}

    public SharedCalendarValidationToken(String token, LocalDateTime createdAt, LocalDateTime confirmedAt, CalendarPermissions calendarPermissions, Calendar calendar, User sharedWith) {
        this.token = token;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
        this.calendarPermissions = calendarPermissions;
        this.calendar = calendar;
        this.sharedWith = sharedWith;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public User getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(User sharedWith) {
        this.sharedWith = sharedWith;
    }

    public CalendarPermissions getCalendarPermissions() {
        return calendarPermissions;
    }

    public void setCalendarPermissions(CalendarPermissions calendarPermissions) {
        this.calendarPermissions = calendarPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedCalendarValidationToken that = (SharedCalendarValidationToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token) && Objects.equals(createdAt, that.createdAt) && Objects.equals(confirmedAt, that.confirmedAt) && calendarPermissions == that.calendarPermissions && Objects.equals(calendar, that.calendar) && Objects.equals(sharedWith, that.sharedWith);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, confirmedAt, calendarPermissions, calendar, sharedWith);
    }
}
