package io.renatofreire.core.repository;

import io.renatofreire.core.model.SharedCalendarValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharedCalendarValidationTokenRepository extends JpaRepository<SharedCalendarValidationToken, UUID> {

    Optional<SharedCalendarValidationToken> findByToken(String token);

}
