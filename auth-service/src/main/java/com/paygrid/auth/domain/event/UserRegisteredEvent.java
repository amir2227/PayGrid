package com.paygrid.auth.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserRegisteredEvent implements Serializable {
    private UUID userId;
    private String email;
}
