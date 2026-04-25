package dev.maria.dto;

import dev.maria.domain.ErrorCode;

public record ErrorResponse(String message, ErrorCode code) {}