package io.renatofreire.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Column(name = "email", length = 120)
    private String email;

    @Past
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hash")
    private String password;

    @Column(name = "email_validated")
    private boolean emailValidated = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> tokens = new HashSet<>();

    public User(){}

    public User(UUID id, String firstName, String lastName, String email, LocalDate birthDate, String password, boolean emailValidated, Set<Role> roles, Set<Token> tokens) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
        this.emailValidated = emailValidated;
        this.roles = roles;
        this.tokens = tokens;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @Past LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@Past LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(birthDate, user.birthDate) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles) && Objects.equals(tokens, user.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, birthDate, password, roles, tokens);
    }

    // Builder pattern
    public static class UserBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDate birthDate;
        private String password;
        private boolean emailValidated = false;
        private Set<Role> roles = new HashSet<>();
        private Set<Token> tokens = new HashSet<>();

        public UserBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder emailValidated(boolean emailValidated) {
            this.emailValidated = emailValidated;
            return this;
        }

        public UserBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public UserBuilder tokens(Set<Token> tokens) {
            this.tokens = tokens;
            return this;
        }

        public User build() {
            return new User(id, firstName, lastName, email, birthDate, password, emailValidated, roles, tokens);
        }
    }

    // Static method to get a builder instance
    public static UserBuilder builder() {
        return new UserBuilder();
    }

}