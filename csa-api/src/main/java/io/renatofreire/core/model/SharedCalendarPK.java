package io.renatofreire.core.model;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class SharedCalendarPK {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public SharedCalendarPK(){}

    public SharedCalendarPK(Calendar calendar, User user) {
        this.calendar = calendar;
        this.user = user;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User sharedWith) {
        this.user = sharedWith;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedCalendarPK that = (SharedCalendarPK) o;
        return Objects.equals(calendar, that.calendar) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar, user);
    }
}
