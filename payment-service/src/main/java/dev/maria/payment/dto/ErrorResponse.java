package dev.maria.payment.dto;

import dev.maria.payment.domain.ErrorCode;

public record ErrorResponse(String message, ErrorCode code) {}