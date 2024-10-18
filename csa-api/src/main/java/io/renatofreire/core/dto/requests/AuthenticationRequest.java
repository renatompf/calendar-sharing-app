package io.renatofreire.core.dto.requests;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull String email,
        @NotNull String password
) {
}