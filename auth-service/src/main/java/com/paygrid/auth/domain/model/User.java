package com.paygrid.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{

    private String id;
    private String email;
    private String password;
    private Set<Role> roles;
}