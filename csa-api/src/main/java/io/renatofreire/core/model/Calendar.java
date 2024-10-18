package io.renatofreire.core.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    public Calendar(){}

    public Calendar(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return Objects.equals(id, calendar.id) && Objects.equals(name, calendar.name) && Objects.equals(owner, calendar.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner);
    }
}
