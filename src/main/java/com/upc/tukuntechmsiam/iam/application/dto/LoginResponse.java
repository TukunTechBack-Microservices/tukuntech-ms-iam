package com.upc.tukuntechmsiam.iam.application.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String refreshToken,
        UserSummary user
) {}
