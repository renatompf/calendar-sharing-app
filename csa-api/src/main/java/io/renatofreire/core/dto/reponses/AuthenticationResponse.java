package io.renatofreire.core.dto.reponses;

import jakarta.validation.constraints.NotNull;

public record AuthenticationResponse(
        @NotNull String token,
        @NotNull String refreshToken
) {}