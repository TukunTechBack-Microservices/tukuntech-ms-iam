package com.upc.tukuntechmsiam.iam.application.dto;

public record TokenRefreshResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String refreshToken
) {}