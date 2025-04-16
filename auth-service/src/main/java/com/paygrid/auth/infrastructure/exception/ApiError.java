package com.paygrid.auth.infrastructure.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
public record ApiError (
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
){}
