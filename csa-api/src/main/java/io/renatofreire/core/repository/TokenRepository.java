package io.renatofreire.core.repository;

import io.renatofreire.core.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    List<Token> findActiveTokensByUserId(UUID id);
}
