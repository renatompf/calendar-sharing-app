package io.renatofreire.core.repository;

import io.renatofreire.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);


    @Query("SELECT (count(u) > 0) FROM User u WHERE u.email = :email AND u.emailValidated = true")
    boolean isUserVerified(String email);

}
