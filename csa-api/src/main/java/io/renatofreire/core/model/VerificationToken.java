package io.renatofreire.core.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_sequence")
    @SequenceGenerator(name = "verification_token_sequence", sequenceName = "verification_token_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(Long id, String token, LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime confirmedAt, User user) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
        this.user = user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerificationToken that = (VerificationToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token) && Objects.equals(createdAt, that.createdAt) && Objects.equals(expiresAt, that.expiresAt) && Objects.equals(confirmedAt, that.confirmedAt) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiresAt, confirmedAt, user);
    }

    public static class VerificationTokenBuilder {
        private Long id;
        private String token;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private LocalDateTime confirmedAt;
        private User user;

        public VerificationTokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public VerificationTokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public VerificationTokenBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public VerificationTokenBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public VerificationTokenBuilder confirmedAt(LocalDateTime confirmedAt) {
            this.confirmedAt = confirmedAt;
            return this;
        }

        public VerificationTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public VerificationToken build() {
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.id = this.id;
            verificationToken.token = this.token;
            verificationToken.createdAt = this.createdAt;
            verificationToken.expiresAt = this.expiresAt;
            verificationToken.confirmedAt = this.confirmedAt;
            verificationToken.user = this.user;
            return verificationToken;
        }
    }

    // Static method to get a builder instance
    public static VerificationTokenBuilder builder() {
        return new VerificationTokenBuilder();
    }
}