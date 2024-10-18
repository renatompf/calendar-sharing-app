package io.renatofreire.core.dto.reponses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link io.renatofreire.core.model.User}
 */
public record UserDto(UUID id, String firstName, String lastName, boolean emailValidated, @Email String email, @Past LocalDate birthDate) implements Serializable {
  }