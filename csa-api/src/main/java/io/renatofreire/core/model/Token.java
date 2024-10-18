package io.renatofreire.core.model;

import io.renatofreire.core.enums.TokenType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", columnDefinition = "text", nullable = false)
    private String token;

    @Column(name = "type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Token(){}

    public Token(User user, boolean revoked, boolean expired, TokenType type, String token) {
        this.user = user;
        this.revoked = revoked;
        this.expired = expired;
        this.type = type;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return expired == token1.expired && revoked == token1.revoked && Objects.equals(id, token1.id) && Objects.equals(token, token1.token) && type == token1.type && Objects.equals(user, token1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, type, expired, revoked, user);
    }
}
